package com.moodle.analytics.error.exception;

import com.moodle.analytics.error.CommonErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Common unauthorized exception
 * 
 * @author aleksandar.kovachev
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized request.")
public class UnauthorizedException extends BaseException {

	private static final long serialVersionUID = -7501141883975781300L;

	public UnauthorizedException() {
		super(HttpStatus.UNAUTHORIZED, CommonErrorCode.UNAUTHORIZED_ACCESS);
	}

}
