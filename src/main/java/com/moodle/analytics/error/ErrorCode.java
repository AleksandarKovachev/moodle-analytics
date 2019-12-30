package com.moodle.analytics.error;

/**
 * Error code
 * 
 * @author aleksandar.kovachev
 */
public interface ErrorCode {

	String X_APPLICATION_ERROR_CODE = "X-Application-Error-Code";
	String X_APPLICATION_ERROR_MESSAGE = "X-Application-Error-Message";
	String X_APPLICATION_ERROR_DESCRIPTION = "X-Application-Error-Description";

	String getCode();

	String getMessage();

	String getDescription();

}
