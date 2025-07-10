package com.my_shop.authservice.src.exception;

import static com.my_shop.authservice.src.util.exceptionUtil.buildResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception, WebRequest request) {
        log.error("Internal error: {}", exception.getMessage(), exception);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", exception.getMessage());
        body.put("path", request.getDescription(false).replace("uri=",""));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(Exception exception, WebRequest request) {
        log.error("Lỗi RunTime: {}", exception.getMessage(), exception);

        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, WebRequest request) {
        log.warn("The parameter is of the wrong type at [{}] - Param '{}': get '{}', required '{}'",
                request.getDescription(false),
                exception.getName(),
                exception.getValue(),
                exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "unknown"
        );
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(AuthFailedException.class)
    public ResponseEntity<?> handleAuthFailedException(AuthFailedException exception, WebRequest request) {
        log.error("Auth Failed: {}", exception.getMessage(), exception);
        return buildResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), request);
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<?> handleCustomNotFoundException(CustomNotFoundException exception, WebRequest request) {
        log.error("Custom Not Found: {}", exception.getMessage(), exception);
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }
}
