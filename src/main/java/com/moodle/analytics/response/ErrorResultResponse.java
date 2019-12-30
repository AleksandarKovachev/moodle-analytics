package com.moodle.analytics.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.moodle.analytics.error.ErrorCode;
import com.moodle.analytics.error.exception.BaseException;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Error result response
 *
 * @author aleksandar.kovachev
 */
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResultResponse {

    private ErrorResponse error;

    static ErrorResultResponse newInstance(final ErrorResultResponse.Builder builder) {
        ErrorResultResponse errorResult = new ErrorResultResponse();

        ErrorResponse error = new ErrorResponse();
        error.setCode(builder.errorCode.getCode());
        error.setMessage(builder.errorCode.getMessage());
        error.setDetails(builder.detailList);
        error.setFieldErrorsSpring(builder.fieldErrors);
        errorResult.error = error;

        return errorResult;
    }

    public static ErrorResultResponse from(BaseException exception) {
        return builder().errorCode(exception.getErrorCode()).details(exception.getDetails())
                .fieldErrors(exception.getFieldErrorList()).build();
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public static Builder builder() {
        return new ErrorResultResponse.Builder();
    }

    public static class Builder {
        ErrorCode errorCode;
        List<String> detailList = null;
        List<FieldError> fieldErrors = null;

        public Builder errorCode(ErrorCode errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder details(String... details) {
            if (details != null && details.length > 0) {
                detailList = new ArrayList<>();
                detailList.addAll(Arrays.asList(details));
            }
            return this;
        }

        public Builder fieldError(FieldError fieldError) {
            if (fieldError != null) {
                fieldErrors = new ArrayList<>();
                fieldErrors.add(fieldError);
            }
            return this;
        }

        public Builder fieldErrors(List<FieldError> fieldErrors) {
            if (fieldErrors != null && !fieldErrors.isEmpty()) {
                this.fieldErrors = fieldErrors;
            }
            return this;
        }

        public ErrorResultResponse build() {
            return ErrorResultResponse.newInstance(this);
        }
    }

}
