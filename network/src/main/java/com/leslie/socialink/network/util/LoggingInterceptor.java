package com.leslie.socialink.network.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

public class LoggingInterceptor implements Interceptor {

    private static final String TAG = "[Http_Log]";

    private static final int READ_BUF_LIMIT_FOR_LOG = 1024 * 4;

    private static final AtomicLong SEQUENCE_NUMBER = new AtomicLong(1);

    private final ByteArrayPool byteArrayPool = new ByteArrayPool(READ_BUF_LIMIT_FOR_LOG * 16);

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String prefix = "#" + SEQUENCE_NUMBER.getAndAdd(1) + " ";

        RequestBody requestBody = request.body();
        String startMessage = prefix + request.method() + ' ' + request.url();
        Log.i(TAG, startMessage);

        if (TextUtils.isEmpty(request.header("Not-Log-Request-Body"))) {
            if (requestBody != null && !bodyEncoded(request.headers())) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF_8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF_8);
                }

                if (isPlaintext(buffer)) {
                    Log.i(TAG, prefix + buffer.readString(charset));
                }
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            Log.e(TAG, prefix + "HTTP FAILED: " + e, e);
            throw e;
        }

        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        String endMessage = prefix + response.code()
                + (!response.isSuccessful() ? (' ' + response.message()) : "")
                + " (" + tookMs + "ms)";

        // If Content-Encoding is gzip in response, we can not log response body when
        // installed as a network interceptor. Because unzip gzip-body in BridgeInterceptor.
        if (TextUtils.isEmpty(request.header("Not-Log-Response-Body"))) {
            if (HttpHeaders.hasBody(response) && !bodyEncoded(response.headers())) {
                ResponseBody responseBody = response.body();
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();

                Charset charset;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF_8);
                } else {
                    charset = UTF_8;
                }
                if (isPlaintext(buffer)) {
                    // Unzip gzip-body will remove Content-Length in BridgeInterceptor. Default
                    // Content-Length is -1, so also can log.
                    if (responseBody.contentLength() != 0) {
                        Buffer clonedBuf = buffer.clone();
                        long size = clonedBuf.size();

                        // To much text leads to memory and performance bug, so limit the allocation of memory and size of the output text.
                        int contentBytes = (int) Math.min(size, READ_BUF_LIMIT_FOR_LOG);
                        byte[] buf = byteArrayPool.getBuf(contentBytes);
                        clonedBuf.read(buf);
                        endMessage += " ";
                        endMessage += (charset == null ? new String(buf, 0, contentBytes) : new String(buf, 0, contentBytes, charset));
                        byteArrayPool.returnBuf(buf);
                    }
                }
            }
        }
        Log.i(TAG, endMessage);

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private static boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

}