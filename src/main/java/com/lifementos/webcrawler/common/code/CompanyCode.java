package com.lifementos.webcrawler.common.code;

public enum CompanyCode {
    NAVER("naver");

    private String code;

    private CompanyCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public static CompanyCode parseCode(String code) {
        for (CompanyCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
}
