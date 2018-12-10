package com.ibm.cogads.domain.model.exceptions;

public class ResponseException{

    private String exceptionCode;
    private String exceptionReason;

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }

    public void setExceptionReason(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    public ResponseException(String exceptionCode, String exceptionReason) {
        this.exceptionCode = exceptionCode;
        this.exceptionReason = exceptionReason;
    }
}
