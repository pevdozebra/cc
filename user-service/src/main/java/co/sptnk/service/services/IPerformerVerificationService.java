package co.sptnk.service.services;

import co.sptnk.lib.base.AbstractCHService;
import co.sptnk.service.model.PerformerVerification;
import co.sptnk.service.model.User;

import java.util.List;


public interface IPerformerVerificationService extends AbstractCHService<PerformerVerification, Long> {
    /**
     * Получение списка не удаленных верификаций
     * @return - список не удаленных верификаций
     */
    public List<PerformerVerification> getAllNotDeleted();
}
