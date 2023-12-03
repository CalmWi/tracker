package edu.grsu.tracker.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class TrackerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    private static final String PATH = "path";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";

    @ExceptionHandler({TrackerExceptoin.class, ResponseStatusException.class})
    public ResponseEntity<Object> handleGDSProcessorException(TrackerExceptoin exception, WebRequest request) {
        return buildErrorResponse(exception.getStatusCode(), exception.getUserMessage(), exception.getReason(),
                request);
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatusCode status,
                                                      String message,
                                                      String details,
                                                      WebRequest request) {
        return buildErrorResponse(status, message, details, request.getContextPath());
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatusCode status,
                                                      String message,
                                                      String details,
                                                      String path) {
        Map<String, Object> errors = Map.of(
                TIMESTAMP, LocalDateTime.now(),
                PATH, path,
                STATUS, status.value(),
                MESSAGE, message,
                ERROR, details);
        return ResponseEntity.status(status).body(errors);
    }

}
