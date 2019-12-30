package com.moodle.analytics.error.exception;

import com.moodle.analytics.error.CommonErrorCode;
import com.moodle.analytics.error.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Common bad request exception
 * 
 * @author aleksandar.kovachev
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "An invalid request was submitted.")
public class BadRequestException extends BaseException {

	private static final long serialVersionUID = 2057857451095552373L;

	private BadRequestException(ErrorCode errorCode, Throwable cause) {
		super(HttpStatus.BAD_REQUEST, errorCode, cause);
	}

	static BadRequestException newInstance(BadRequestException.Builder builder) {
		BadRequestException badRequestException = new BadRequestException(builder.errorCode, builder.cause);
		if (builder.getDetails().length != 0) {
			badRequestException.setDetails(builder.getDetails());
		}
		return badRequestException;
	}

	public static Builder builder() {
		return new BadRequestException.Builder();
	}

	public static class Builder extends BaseException.AbstractBuilder<BadRequestException, Builder> {
		public Builder unsupportedOperation() {
			return errorCode(CommonErrorCode.UNSUPPORTED_OPERATION);
		}

		public Builder requestNotParsable() {
			return errorCode(CommonErrorCode.REQUEST_NOT_PARSEABLE);
		}

		public Builder maxUploadSizeExceeded() {
			return errorCode(CommonErrorCode.MAX_UPLOAD_SIZE_EXCEEDED);
		}

		@Override
		public BadRequestException build() {
			return newInstance(this);
		}
	}

}
