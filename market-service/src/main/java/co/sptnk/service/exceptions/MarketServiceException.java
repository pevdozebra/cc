package co.sptnk.service.exceptions;


/**
 * Исключение для сервиса заказов и продуктов
 */
public class MarketServiceException extends Exception {
    public MarketServiceException(String errorMessage) {
        super(errorMessage);
    }
}
