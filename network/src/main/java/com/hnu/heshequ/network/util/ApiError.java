package com.hnu.heshequ.network.util;

public class ApiError extends RuntimeException {
    private int mCode;

    public ApiError(int code, String message) {
        super(message);

        mCode = code;
    }

    public ApiError(ErrorCode errorCode) {
        mCode = errorCode.getCode();
    }

    public int getCode() {
        return mCode;
    }

    @Override
    public String toString() {
        return String.format("ApiError: %d %s", getCode(), getMessage());
    }
}
