package com.moodle.analytics.error.exception;

import com.moodle.analytics.error.CommonErrorCode;
import com.moodle.analytics.error.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Common internal error exception
 * 
 * @author aleksandar.kovachev
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An internal error occured.")
public class InternalErrorException extends BaseException {

	private static final long serialVersionUID = -8537035069329900376L;

	private InternalErrorException(ErrorCode errorCode, Throwable cause) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, cause);
	}

	static InternalErrorException newInstance(InternalErrorException.Builder builder) {
		InternalErrorException internalErrorException = new InternalErrorException(builder.errorCode, builder.cause);
		if (builder.getDetails().length != 0) {
			internalErrorException.setDetails(builder.getDetails());
		}
		return internalErrorException;
	}

	public static Builder builder() {
		return new InternalErrorException.Builder();
	}

	public static class Builder extends BaseException.AbstractBuilder<InternalErrorException, Builder> {

		public Builder internalError() {
			return errorCode(CommonErrorCode.INTERNAL_ERROR);
		}

		public Builder databaseError() {
			return errorCode(CommonErrorCode.DATABASE_ERROR);
		}

		@Override
		public InternalErrorException build() {
			return newInstance(this);
		}

	}

}
