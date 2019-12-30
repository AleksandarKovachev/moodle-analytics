package com.moodle.analytics.error;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.moodle.analytics.error.exception.*;
import com.moodle.analytics.response.ErrorResultResponse;
import com.moodle.analytics.util.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.ModelMap;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Web controller advice
 *
 * @author aleksandar.kovachev
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleInternalServerErrorException(Locale locale, Exception e) {
        log.error("An error has occurred: ", e);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("code", "500");
        modelMap.addAttribute("title", messageSource.getMessage("internal.error.title", null, locale));
        modelMap.addAttribute("message", messageSource.getMessage("internal.error.message", null, locale));
        return new ModelAndView("error", modelMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException(Locale locale, Exception e) {
        log.error("An error has occurred: ", e);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("code", "404");
        modelMap.addAttribute("title", messageSource.getMessage("not.found.error.title", null, locale));
        modelMap.addAttribute("message", messageSource.getMessage("not.found.error.message", null, locale));
        return new ModelAndView("error", modelMap, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBadRequestException(Locale locale, Exception e) {
        log.error("An error has occurred: ", e);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("code", "400");
        modelMap.addAttribute("title", messageSource.getMessage("bad.request.error.title", null, locale));
        modelMap.addAttribute("message", messageSource.getMessage("bad.request.error.message", null, locale));
        return new ModelAndView("error", modelMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseException.class)
    public final ResponseEntity<Object> handleException(BaseException exception) {
        log.warn("Caught exception: ", exception);
        return createResponseEntity(exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        BaseException exception = new UnauthorizedException();
        ResponseEntity<Object> resultResponseEntity = createResponseEntity(exception);
        ErrorResultResponse result = (ErrorResultResponse) resultResponseEntity.getBody();

        // @formatter:off
        String response =
                "{\n" + "    \"error\": {\n" + "        \"code\": \""
                        + CommonErrorCode.UNAUTHORIZED_ACCESS.getCode() + "\",\n"
                        + "        \"message\": \""
                        + CommonErrorCode.UNAUTHORIZED_ACCESS.getMessage() + "\", \n"
                        + "        \"details\": [\n" + "            \""
                        + CommonErrorCode.UNAUTHORIZED_ACCESS.getDescription()
                        + "\"\n" + "        ]\n" + "    }\n" + "}";
        // @formatter:on
        response = RestUtil.objectToJson(result);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.UNAUTHORIZED);
    }

    static ResponseEntity<Object> createResponseEntity(BaseException exception) {
        return createResponseEntity(exception, exception.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        BaseException exception = InternalErrorException.builder().internalError().cause(ex).build();
        exception.setDetails(ex.getMessage());
        return createResponseEntity(exception, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        BaseException exception = new UnsupportedContentTypeException();
        return createResponseEntity(exception);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("handleHttpMessageNotReadable", ex);
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
            if (isTargetTypeEnum(invalidFormatException)) {
                return handleInvalidFormatEnumException(invalidFormatException);
            }
        }
        return createResponseEntity(BadRequestException.builder().requestNotParsable().cause(ex).build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        BaseException exception = new InvalidFieldException(fieldErrorList);
        return createResponseEntity(exception);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        BaseException exception = new NotSupportedException();
        exception.setDetails(ex.getMessage());
        return createResponseEntity(exception);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("object", ex.getParameterName(), "Value is required."));
        BaseException exception = new InvalidFieldException(fieldErrors);
        return createResponseEntity(exception);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {
        if (ex.getMostSpecificCause() instanceof IllegalArgumentException) {
            BaseException exception = NotFoundException.builder().entityNotFound()
                    .details(ex.getMostSpecificCause().getMessage()).build();
            return createResponseEntity(exception);
        }

        BaseException exception = BadRequestException.builder().requestNotParsable()
                .details(ex.getMostSpecificCause().getMessage()).build();
        return createResponseEntity(exception);
    }

    private static ResponseEntity<Object> createResponseEntity(BaseException exception, HttpStatus httpStatus) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(ErrorCode.X_APPLICATION_ERROR_CODE, exception.getErrorCode().getCode());
        responseHeaders.set(ErrorCode.X_APPLICATION_ERROR_MESSAGE, exception.getErrorCode().getMessage());
        responseHeaders.set(ErrorCode.X_APPLICATION_ERROR_DESCRIPTION, exception.getErrorCode().getDescription());

        ErrorResultResponse errorResult = ErrorResultResponse.from(exception);
        return new ResponseEntity<>(errorResult, responseHeaders, httpStatus);
    }

    private static boolean isTargetTypeEnum(InvalidFormatException invalidFormatException) {
        Class<?> targetType = invalidFormatException.getTargetType();
        return targetType != null && targetType.isEnum();
    }

    private ResponseEntity<Object> handleInvalidFormatEnumException(InvalidFormatException invalidFormatException) {
        Class<?> targetType = invalidFormatException.getTargetType();
        List<FieldError> fieldErrorList = retrieveFieldErrorList(invalidFormatException, targetType);
        return createResponseEntity(new InvalidFieldException(fieldErrorList));
    }

    private static List<FieldError> retrieveFieldErrorList(InvalidFormatException invalidFormatException,
                                                           Class<?> targetType) {
        String invalidValue = invalidFormatException.getValue().toString();
        String msg = String.format("invalid value '" + invalidValue + "', valid values are %s",
                Arrays.asList(targetType.getEnumConstants()));
        List<FieldError> fieldErrorList = new ArrayList<>();
        fieldErrorList.add(
                new FieldError(invalidValue, retrieveInvalidFormatExceptionFieldName(invalidFormatException), msg));
        return fieldErrorList;
    }

    static String retrieveInvalidFormatExceptionFieldName(final InvalidFormatException ifx) {
        StringBuilder sb = new StringBuilder();

        List<Reference> referenceList = ifx.getPath();
        if (referenceList != null) {
            boolean isFirst = true;
            for (Reference ref : referenceList) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append('.');
                }
                sb.append(ref.getFieldName());
            }
        }

        return sb.toString();
    }

}
