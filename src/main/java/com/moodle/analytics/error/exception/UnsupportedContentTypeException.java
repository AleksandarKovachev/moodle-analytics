package com.moodle.analytics.error.exception;

import com.moodle.analytics.error.CommonErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Common unsupported content type exception
 * 
 * @author aleksandar.kovachev
 */
@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE, reason = "Unsupported format content type format.")
public class UnsupportedContentTypeException extends BaseException {

	private static final long serialVersionUID = 6904901406744449505L;

	public UnsupportedContentTypeException() {
		super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, CommonErrorCode.UNSUPPORTED_CONTENT_TYPE);
	}

}
