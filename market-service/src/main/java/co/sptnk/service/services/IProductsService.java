package co.sptnk.service.services;

import co.sptnk.lib.base.AbstractCHService;
import co.sptnk.service.model.Product;

import java.util.List;
import java.util.UUID;

public interface IProductsService extends AbstractCHService<Product, Long> {

    /**
     * Все продукты для исполнителя
     * @param performerUuid - идентификатор исполнителя
     * @return - список продуктов для исполнителя
     */
    List<Product> getAllForUser(UUID performerUuid);

}
