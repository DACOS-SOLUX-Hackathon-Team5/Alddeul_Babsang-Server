package com.example.alddeul_babsang.apiPayload.exception.handler;


import com.example.alddeul_babsang.apiPayload.code.BaseErrorCode;
import com.example.alddeul_babsang.apiPayload.exception.GeneralException;
import lombok.Getter;

@Getter
public class TempHandler extends GeneralException {
    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}