package co.sptnk.service.services;

import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.model.Product;
import co.sptnk.service.transfers.ProductPageTO;

import java.util.List;
import java.util.UUID;

public interface IProductsService {

    /**
     * Получение всех продуктов для пользователя
     * @param performerUuid - идентификатор пользователя
     * @return - список продуктов для пользователя
     */
    List<Product> getAllForUser(UUID performerUuid);

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

    ProductPageTO getAll(Integer page, Integer size);
}
