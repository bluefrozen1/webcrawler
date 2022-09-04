package com.lifementos.webcrawler.common.code;

public enum ErrorCode {
    CRAWLING_ERROR("0001");

    private String code;

    private ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public static ErrorCode parseCode(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
}
