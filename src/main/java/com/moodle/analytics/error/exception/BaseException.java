package com.moodle.analytics.error.exception;

import com.moodle.analytics.error.CommonErrorCode;
import com.moodle.analytics.error.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Root of the hierarchy exception
 * 
 * @author aleksandar.kovachev
 */
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -4039254942991717097L;

	private HttpStatus status;
	private ErrorCode errorCode;
	private List<String> details;
	private List<FieldError> fieldErrorList;

	public BaseException(HttpStatus status, ErrorCode errorCode) {
		this(status, errorCode, null);
	}

	public BaseException(HttpStatus status, ErrorCode errorCode, Throwable cause) {
		super(errorCode == null ? CommonErrorCode.INTERNAL_ERROR.getMessage() : errorCode.getMessage(), cause);

		ErrorCode code = errorCode == null ? CommonErrorCode.INTERNAL_ERROR : errorCode;

		this.errorCode = code;
		this.status = status;
		details = new ArrayList<>();
		if (code.getDescription() != null) {
			details.add(code.getDescription());
		}
		fieldErrorList = new ArrayList<>();
	}

	public BaseException(HttpStatus status, List<FieldError> fieldErrorList, ErrorCode errorCode) {
		this(status, errorCode, fieldErrorList, null);
	}

	public BaseException(HttpStatus status, ErrorCode errorCode, List<FieldError> fieldErrorList, Throwable cause) {
		super(errorCode == null ? CommonErrorCode.INTERNAL_ERROR.getMessage() : errorCode.getMessage(), cause);
		ErrorCode code = errorCode == null ? CommonErrorCode.INTERNAL_ERROR : errorCode;
		this.errorCode = code;
		this.status = status;
		details = new ArrayList<>();
		if (code.getDescription() != null) {
			details.add(code.getDescription());
		}
		this.fieldErrorList = fieldErrorList;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

	public ErrorCode getErrorCode() {
		return this.errorCode;
	}

	public String[] getDetails() {
		return details == null ? new String[0] : details.toArray(new String[details.size()]);
	}

	public void setDetails(String... details) {
		if (details != null) {
			this.details = Arrays.asList(details);
		}
	}

	public List<FieldError> getFieldErrorList() {
		return fieldErrorList;
	}

	public void setFieldErrorList(List<FieldError> fieldErrorList) {
		if (fieldErrorList != null) {
			this.fieldErrorList = fieldErrorList;
		}
	}

	@SuppressWarnings("rawtypes")
	public abstract static class AbstractBuilder<T extends BaseException, B extends AbstractBuilder> {
		protected ErrorCode errorCode;
		protected Throwable cause;
		protected List<String> details = new ArrayList<>();

		public B cause(Throwable cause) {
			this.cause = cause;
			return returnBuilder();
		}

		public Throwable getCause() {
			return this.cause;
		}

		public B details(String... details) {
			if (details != null) {
				this.details = Arrays.asList(details);
			}
			return returnBuilder();
		}

		public B detail(String format, Object... args) {
			String formattedString = String.format(format, args);
			details.add(formattedString);
			return returnBuilder();
		}

		public String[] getDetails() {
			return details == null ? new String[0] : details.toArray(new String[details.size()]);
		}

		public B errorCode(ErrorCode errorCode) {
			this.errorCode = errorCode;
			return returnBuilder();
		}

		protected ErrorCode getErrorCode() {
			return this.errorCode;
		}

		public abstract T build();

		@SuppressWarnings("unchecked")
		protected B returnBuilder() {
			return (B) this;
		}

	}

}
