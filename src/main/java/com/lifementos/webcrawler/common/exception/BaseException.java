package com.lifementos.webcrawler.common.exception;

import com.lifementos.webcrawler.common.code.ErrorCode;

public abstract class BaseException extends Exception {

	private static final long serialVersionUID = 6697553987008675632L;

	protected ErrorCode errorCode;

	public BaseException(ErrorCode errorCode ) {
		this.errorCode = errorCode;
	}

	public BaseException(ErrorCode errorCode, String msg ) {
		super( msg );
		this.errorCode = errorCode;
	}

	public BaseException(ErrorCode errorCode, Throwable throwable ) {
		super( throwable );
		this.errorCode = errorCode;
	}

	public BaseException(ErrorCode errorCode, String msg, Throwable throwable ) {
		super( msg, throwable );
		this.errorCode = errorCode;
	}

}
