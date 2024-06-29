package com.example.satto.global.common.exception.handler;

import com.example.satto.global.common.code.BaseErrorCode;
import com.example.satto.global.common.exception.GeneralException;

public class UsersHandler extends GeneralException {
    public UsersHandler(BaseErrorCode code) {
        super(code);
    }
}
