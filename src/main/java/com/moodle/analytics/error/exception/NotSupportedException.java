package com.moodle.analytics.error.exception;

import com.moodle.analytics.error.CommonErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Common not supported exception
 * 
 * @author aleksandar.kovachev
 */
@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED, reason = "The requested functionality is not supported.")
public class NotSupportedException extends BaseException {

	private static final long serialVersionUID = 8793741416546389051L;

	public NotSupportedException() {
		super(HttpStatus.METHOD_NOT_ALLOWED, CommonErrorCode.NOT_SUPPORTED);
	}

}
