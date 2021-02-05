package co.sptnk.service.exceptions;

import co.sptnk.lib.exceptions.ServiceException;

public class LoggingServiceException extends ServiceException {
    public LoggingServiceException(String errorMessage) {
        super(errorMessage);
    }
}
