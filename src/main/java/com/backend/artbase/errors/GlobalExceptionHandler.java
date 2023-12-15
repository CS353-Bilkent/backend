package com.backend.artbase.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.backend.artbase.entities.ApiResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleServiceExceptions(BaseRuntimeException exception) {
        log.error("An exception occurred " + exception.getMessage());

        return createErrorResponse(exception.getHttpStatus(), exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleInternalServerErrors(RuntimeException exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        String stacktrace = sw.toString();

        log.error("Unknown Internal Error: {}", stacktrace);

        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error.");
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse<Object>> handleMultipartException(MultipartException exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        String stacktrace = sw.toString();

        log.error("Unknown Internal Error: {}", stacktrace);

        return createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleSizeLimitException(MaxUploadSizeExceededException exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        String stacktrace = sw.toString();

        log.error("Unknown Internal Error: {}", stacktrace);

        return createErrorResponse(HttpStatus.BAD_REQUEST, "File size cannot exceed 5MB");
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).body((Object) createErrorApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).body((Object) createErrorApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).body((Object) createErrorApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        return ResponseEntity.status(status).body((Object) createErrorApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).body((Object) createErrorApiResponse(HttpStatus.BAD_REQUEST,
                "Request is not readable, body may be malformed or does not exist."));
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        return ResponseEntity.status(status).body((Object) createErrorApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).body((Object) createErrorApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status).body((Object) createErrorApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        List<Object> errors = Arrays.asList(Objects.requireNonNull(ex.getDetailMessageArguments()));
        List<String> stringErrors = errors.stream().map(Object::toString).collect(Collectors.toList());
        List<String> finalErrors = new ArrayList<>();

        stringErrors.forEach(e -> finalErrors.addAll(Arrays.asList(e.trim().substring(1, e.length() - 1).split(", "))));

        return ResponseEntity.status(status)
                .body((Object) createErrorApiResponseWithList(HttpStatus.BAD_REQUEST, "Validation errors", finalErrors));
    }

    private ResponseEntity<ApiResponse<Object>> createErrorResponse(HttpStatus httpStatus, String errorMessage) {
        return ResponseEntity.status(httpStatus).body(createErrorApiResponse(httpStatus, errorMessage));
    }

    private ApiResponse<Object> createErrorApiResponse(HttpStatus errorCode, String errorMessage) {
        return ApiResponse.builder().operationResult(ApiResponse.OperationResult.builder().returnCode(Integer.toString(errorCode.value()))
                .returnMessage(errorMessage).returnErrors(null).build()).operationResultData(null).build();
    }

    private ApiResponse<Object> createErrorApiResponseWithList(HttpStatus errorCode, String errorMessage, List<String> errors) {
        return ApiResponse.builder().operationResult(ApiResponse.OperationResult.builder().returnCode(Integer.toString(errorCode.value()))
                .returnMessage(errorMessage).returnErrors(errors).build()).operationResultData(null).build();
    }
}
