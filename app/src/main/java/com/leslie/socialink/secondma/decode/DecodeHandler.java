/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leslie.socialink.secondma.decode;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.leslie.socialink.secondma.android.CaptureActivity;

import java.util.Map;

public final class DecodeHandler extends Handler {

    private static final String TAG = DecodeHandler.class.getSimpleName();

    public static final int DECODE = 1;
    public static final int DECODE_FAILED = 2;
    public static final int DECODE_SUCCEEDED = 3;

    public static final int QUIT = 5;
    public static final int RESTART_PREVIEW = 6;
    public static final int RETURN_SCAN_RESULT = 7;

    public static final String INTENT_ZXING_CONFIG = "zxingConfig";

    private final CaptureActivity activity;
    private final MultiFormatReader multiFormatReader;
    private boolean running = true;

    DecodeHandler(CaptureActivity activity, Map<DecodeHintType, Object> hints) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message message) {
        if (!running) {
            return;
        }
        switch (message.what) {
            case DECODE:

                decode((byte[]) message.obj, message.arg1, message.arg2);
                break;
            case QUIT:
                running = false;
                Looper.myLooper().quit();
                break;
        }
    }

    /**
     * 解码
     */
    private void decode(byte[] data, int width, int height) {

        Result rawResult = null;

        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;
        data = rotatedData;

        PlanarYUVLuminanceSource source = activity.getCameraManager()
                .buildLuminanceSource(data, width, height);


        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {

                //Log.i("解码异常",re.toString());
            } finally {
                multiFormatReader.reset();
            }
        }


        Handler handler = activity.getHandler();
        if (rawResult != null) {

            if (handler != null) {
                Message message = Message.obtain(handler,
                        DECODE_SUCCEEDED, rawResult);
                message.sendToTarget();
            }
        } else {
            if (handler != null) {
                Message message = Message.obtain(handler, DECODE_FAILED);
                message.sendToTarget();
            }
        }
    }

}
