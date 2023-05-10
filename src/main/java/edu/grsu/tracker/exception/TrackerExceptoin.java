package edu.grsu.tracker.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class TrackerExceptoin extends ResponseStatusException {

    private final String userMessage;

    public TrackerExceptoin(String userMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, userMessage);
        this.userMessage = userMessage;
    }

    public TrackerExceptoin(final String userMessage, final String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
        this.userMessage = userMessage;
    }

    public TrackerExceptoin(final String userMessage, final String reason, final Exception e) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason, e);
        this.userMessage = userMessage;
    }

    public TrackerExceptoin(final HttpStatus httpStatus, final String userMessage, final String reason) {
        super(httpStatus, reason);
        this.userMessage = userMessage;
    }

    public TrackerExceptoin(final HttpStatus httpStatus,
                                 final String userMessage,
                                 final String reason,
                                 final Exception e) {
        super(httpStatus, reason, e);
        this.userMessage = userMessage;
    }

}
