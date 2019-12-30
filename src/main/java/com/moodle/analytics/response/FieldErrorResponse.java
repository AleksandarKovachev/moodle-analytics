package com.moodle.analytics.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

/**
 * Field error response
 * 
 * @author aleksandar.kovachev
 */
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldErrorResponse implements Serializable {

	private static final long serialVersionUID = -4325518918999290012L;

	private String field;

	private String error;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getError() {
		return error;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FieldError [field=");
		builder.append(field);
		builder.append(", error=");
		builder.append(error);
		builder.append("]");
		return builder.toString();
	}

	public void setError(String error) {
		this.error = error;
	}

}
