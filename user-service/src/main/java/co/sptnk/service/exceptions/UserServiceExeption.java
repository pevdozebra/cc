package co.sptnk.service.exceptions;


import co.sptnk.lib.exceptions.ServiceException;

/**
 * Исключение для сервиса пользователей
 */
public class UserServiceExeption extends ServiceException {
    public UserServiceExeption(String errorMessage) {
        super(errorMessage);
    }
}
