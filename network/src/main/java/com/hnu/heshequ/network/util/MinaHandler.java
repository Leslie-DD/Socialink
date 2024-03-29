package com.hnu.heshequ.network.util;

import androidx.annotation.Nullable;

public class MinaHandler implements IHandler {

    @Nullable
    @Override
    public Object getResponse(Object object) throws RuntimeException {
        if (object instanceof MinaResponse) {
            MinaResponse response = (MinaResponse) object;
            if (response.code > 0) {
//                throw new ApiError(response.code, response.msg);
                // TODO: handle api error message
                return null;
            } else {
                return response.data;
//                if (response.data == null) {
//                    throw new ApiError(ErrorCode.INVALIDATE_DATA_FORMAT.getCode(), "data is null");
//                } else {
//                    return response.data;
//                }
            }
        } else {
            throw new ApiError(ErrorCode.INVALIDATE_DATA_FORMAT.getCode(), "");
        }
    }
}