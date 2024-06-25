package com.example.satto.global.common.exception.handler;

import com.example.satto.global.common.code.BaseErrorCode;
import com.example.satto.global.common.exception.GeneralException;

public class FollowHandler extends GeneralException {
    public FollowHandler(BaseErrorCode code) {
        super(code);
    }
}
