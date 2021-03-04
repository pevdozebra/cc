package co.sptnk.service.user.services;


import co.sptnk.lib.service.AbstractCrudService;
import co.sptnk.service.user.model.PerformerVerification;

import java.util.List;


public interface IPerformerVerificationService extends AbstractCrudService<PerformerVerification, Long> {
    /**
     * Получение списка не удаленных верификаций
     * @return - список не удаленных верификаций
     */
    List<PerformerVerification> getAllNotDeleted();
}
