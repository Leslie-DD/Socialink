package com.hnu.heshequ.network.util;

import android.content.Context;

import androidx.annotation.StringRes;

import com.hnu.heshequ.network.R;

import java.util.HashMap;

public enum ErrorCode {
    SUCCESS(0, 0),
    UNKNOWN_ERROR(1, 0),
    HTTP_ERROR(2, R.string.error_http_error),
    CONNECT_ERROR(3, 0),
    INVALIDATE_DATA_FORMAT(4, 0),
    CONNECT_TIMEOUT(5, 0),
    PARSE_ERROR(6, 0),


    APP_SONG_LRC_URL_EMPTY(10000, 0);

    private static HashMap<Integer, ErrorCode> sCodeMap;
    private final int mCode;
    private final @StringRes
    int mMsgRes;

    ErrorCode(int code, @StringRes int msgRes) {
        mCode = code;
        mMsgRes = msgRes;
    }

    public int getCode() {
        return mCode;
    }

    public static ErrorCode valueOf(int code) {
        if (sCodeMap == null) {
            sCodeMap = new HashMap<>();

            ErrorCode[] values = ErrorCode.values();
            for (ErrorCode value : values) {
                sCodeMap.put(value.getCode(), value);
            }
        }

        return sCodeMap.getOrDefault(code, ErrorCode.UNKNOWN_ERROR);
    }

    public @StringRes
    int getMsgRes() {
        return mMsgRes;
    }

    public String getMsgString(Context context) {
        if (mMsgRes > 0) {
            return context.getString(mMsgRes);
        } else {
            return context.getString(R.string.error_msg_unknown, getCode());
        }
    }

    public ApiError toApiError(String message) {
        return new ApiError(getCode(), message);
    }

}