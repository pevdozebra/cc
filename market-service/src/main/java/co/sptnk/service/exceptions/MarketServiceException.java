package co.sptnk.service.exceptions;

import co.sptnk.lib.exceptions.ServiceException;

/**
 * Исключение для сервиса заказов и продуктов
 */
public class MarketServiceException extends ServiceException {
    public MarketServiceException(String errorMessage) {
        super(errorMessage);
    }
}
