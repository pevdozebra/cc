package co.sptnk.service.services;


import co.sptnk.lib.service.AbstractCrudService;
import co.sptnk.service.model.PerformerVerification;

import java.util.List;


public interface IPerformerVerificationService extends AbstractCrudService<PerformerVerification, Long> {
    /**
     * Получение списка не удаленных верификаций
     * @return - список не удаленных верификаций
     */
    public List<PerformerVerification> getAllNotDeleted();
}
