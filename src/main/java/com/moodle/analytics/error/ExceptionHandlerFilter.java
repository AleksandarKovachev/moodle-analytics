package com.moodle.analytics.error;

import com.fasterxml.jackson.core.JsonParseException;
import com.moodle.analytics.error.exception.BadRequestException;
import com.moodle.analytics.error.exception.BaseException;
import com.moodle.analytics.error.exception.InternalErrorException;
import com.moodle.analytics.response.ErrorResultResponse;
import com.moodle.analytics.util.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Exception handler filter
 * 
 * @author aleksandar.kovachev
 */
@Configuration
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerFilter.class);

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (BaseException exception) {
			handleException(response, exception, exception);
		} catch (MaxUploadSizeExceededException e) {
			BaseException exception = BadRequestException.builder().maxUploadSizeExceeded().cause(e).build();
			handleException(response, e, exception);
		} catch (RuntimeException e) {
			BaseException exception;
			if (e.getCause() instanceof JsonParseException) {
				exception = BadRequestException.builder().requestNotParsable().cause(e).build();
			} else {
				logger.error("Internal error", e);
				exception = InternalErrorException.builder().internalError().cause(e).build();
			}
			handleException(response, e, exception);
		}
	}

	private void handleException(HttpServletResponse response, RuntimeException rte, BaseException exception) {
		ErrorResultResponse errorResult = ErrorResultResponse.builder().errorCode(exception.getErrorCode())
				.details(exception.getDetails()).fieldErrors(exception.getFieldErrorList()).build();
		try {
			setHttpResponseHeaders(response, exception);
			response.getWriter().write(RestUtil.objectToJson(errorResult));
		} catch (IOException ioe) {
			logger.error("Error setting http response {} ", rte.getMessage());
		}
	}

	public static void setHttpResponseHeaders(HttpServletResponse response, BaseException exception) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(exception.getStatus().value());
		response.addHeader(ErrorCode.X_APPLICATION_ERROR_CODE, exception.getErrorCode().getCode());
		response.addHeader(ErrorCode.X_APPLICATION_ERROR_MESSAGE, exception.getErrorCode().getMessage());
		response.addHeader(ErrorCode.X_APPLICATION_ERROR_DESCRIPTION, exception.getErrorCode().getDescription());
	}

}
