package com.moodle.analytics.error;

/**
 * Common error codes
 * 
 * @author aleksandar.kovachev
 */
public enum CommonErrorCode implements ErrorCode {

	// HTTP status: 400
	UNSUPPORTED_OPERATION("4000", "Unsupported operation", "Invalid or unsupported operation type."),
	REQUEST_NOT_PARSEABLE("4001", "Request body not parsable", "The request cannot be parsed."),
	INVALID_FIELD("4002", "Field error(s)", "Missing a mandatory field or invalid format of field value."),
	MAX_UPLOAD_SIZE_EXCEEDED("4003", "Maximum upload size exceeded", "The maximum upload size is exceeded"),

	// HTTP status: 401
	UNAUTHORIZED_ACCESS("4010", "Unauthorized access", "The requested data is restricted and requires authentication."),

	// HTTP status 404
	URL_NOT_FOUND("4040", "URL not found", "The requested URL was not found"),
	ENTITY_NOT_FOUND("4041", "Entity not found", "Entity not found with the specified data."),

	// HTTP status: 405
	NOT_SUPPORTED("5281", "Not supported", "The request method is not supported."),

	// HTTP status: 415
	UNSUPPORTED_CONTENT_TYPE("4150", "Unsupported 'Content-Type'", "Unsupported 'Content-Type' format."),

	// HTTP status: 500
	INTERNAL_ERROR("5000", "Internal Error", "An internal error occurred."),
	DATABASE_ERROR("5001", "Database Error", "An internal database error occurred.");

	private String code;
	private String message;
	private String description;

	CommonErrorCode(String code, String message, String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
