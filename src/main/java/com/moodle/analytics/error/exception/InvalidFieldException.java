package com.moodle.analytics.error.exception;

import com.moodle.analytics.error.CommonErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Common invalid field exception
 * 
 * @author aleksandar.kovachev
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "An invalid or missing field was found.")
public class InvalidFieldException extends BaseException {

	private static final long serialVersionUID = -1011574344737817088L;

	public InvalidFieldException(List<FieldError> fieldErrorList) {
		this(fieldErrorList, null);
	}

	public InvalidFieldException(List<FieldError> fieldErrorList, Throwable cause) {
		super(HttpStatus.BAD_REQUEST, CommonErrorCode.INVALID_FIELD, fieldErrorList, cause);
	}

}
