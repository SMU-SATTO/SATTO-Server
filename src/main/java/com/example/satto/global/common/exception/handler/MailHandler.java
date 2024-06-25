package com.example.satto.global.common.exception.handler;

import com.example.satto.global.common.code.BaseErrorCode;
import com.example.satto.global.common.exception.GeneralException;

public class MailHandler extends GeneralException {
    public MailHandler(BaseErrorCode code) {
        super(code);
    }
}
