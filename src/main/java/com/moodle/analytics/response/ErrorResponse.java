package com.moodle.analytics.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * Error response
 * 
 * @author aleksandar.kovachev
 */
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

	private String code;
	private String message;
	private List<String> details;
	private List<FieldErrorResponse> fieldErrors;

	ErrorResponse() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getDetails() {
		return this.details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	public List<FieldErrorResponse> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldErrorResponse> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public void setFieldErrorsSpring(List<FieldError> fieldErrors) {
		if (fieldErrors != null) {
			List<FieldErrorResponse> fieldErrorList = new ArrayList<>();
			for (FieldError fieldError : fieldErrors) {
				FieldErrorResponse fieldErrorResponse = new FieldErrorResponse();
				fieldErrorResponse.setField(fieldError.getField());
				fieldErrorResponse.setError(fieldError.getDefaultMessage());
				fieldErrorList.add(fieldErrorResponse);
			}
			this.fieldErrors = fieldErrorList;
		}
	}

}
