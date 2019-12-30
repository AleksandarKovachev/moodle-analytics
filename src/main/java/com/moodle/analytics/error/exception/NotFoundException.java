package com.moodle.analytics.error.exception;

import com.moodle.analytics.error.CommonErrorCode;
import com.moodle.analytics.error.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Common not found exception
 * 
 * @author aleksandar.kovachev
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The requested URL was not found.")
public class NotFoundException extends BaseException {

	private static final long serialVersionUID = 8906478250479467000L;

	private NotFoundException(ErrorCode errorCode, Throwable cause) {
		super(HttpStatus.NOT_FOUND, errorCode, cause);
	}

	static NotFoundException newInstance(NotFoundException.Builder builder) {
		NotFoundException notFoundException = new NotFoundException(builder.errorCode, builder.cause);
		if (builder.getDetails().length != 0) {
			notFoundException.setDetails(builder.getDetails());
		}
		return notFoundException;
	}

	public static Builder builder() {
		return new NotFoundException.Builder();
	}

	public static class Builder extends BaseException.AbstractBuilder<NotFoundException, Builder> {
		public Builder entityNotFound() {
			return errorCode(CommonErrorCode.ENTITY_NOT_FOUND);
		}

		public Builder uriNotFound() {
			return errorCode(CommonErrorCode.URL_NOT_FOUND);
		}

		@Override
		public NotFoundException build() {
			return newInstance(this);
		}
	}

}
