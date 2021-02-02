package co.sptnk.service.services;

import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.model.Product;

import java.util.List;

public interface IProductsService {

    /**
     * Получение всех продуктов для пользователя
     * @param userId - идентификатор пользователя
     * @return - список продуктов для пользователя
     */
    List<Product> getAllForUser(Long userId);

    /**
     * Сохранение продукта
     * @param product - объект продукта
     */
    void save(Product product);

    /**
     * Удаление продукта (помечает продукт признаком удаления)
     * @param id - индетификатор продукта
     * @throws MarketServiceException - если продукта нет в БД
     */
    void delete(Long id) throws MarketServiceException;
}
