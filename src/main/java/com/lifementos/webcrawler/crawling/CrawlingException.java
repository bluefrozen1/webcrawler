package com.lifementos.webcrawler.crawling;

import com.lifementos.webcrawler.common.code.ErrorCode;
import com.lifementos.webcrawler.common.exception.BaseException;

public class CrawlingException extends BaseException {

    public CrawlingException( ErrorCode errorCode ) {
        super( errorCode );
    }

    public CrawlingException( ErrorCode errorCode, String msg ) {
        super( errorCode, msg );
        this.errorCode = errorCode;
    }

    public CrawlingException( ErrorCode errorCode, Throwable throwable ) {
        super( errorCode, throwable );
        this.errorCode = errorCode;
    }

    public CrawlingException( ErrorCode errorCode, String msg, Throwable throwable ) {
        super( errorCode, msg, throwable );
        this.errorCode = errorCode;
    }
}
