package co.sptnk.service.services.Impl;

import co.sptnk.service.exceptions.UserServiceExeption;
import co.sptnk.service.model.PerformerVerification;
import co.sptnk.service.repositories.PerformerVerificationsRepo;
import co.sptnk.service.services.IPerformerVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PerformerVerificationService implements IPerformerVerificationService {

    @Autowired
    private PerformerVerificationsRepo performerVerificationsRepo;

    @Override
    public PerformerVerification add(PerformerVerification performerVerification) throws UserServiceExeption {
        if (performerVerification.getId() != null) {
            throw new UserServiceExeption("ID объекта должен быть пуст");
        }
        return performerVerificationsRepo.save(performerVerification);
    }

    @Override
    public PerformerVerification update(PerformerVerification performerVerification) throws UserServiceExeption {
        if (performerVerification.getId() == null) {
            throw new UserServiceExeption("Невозможно идентифицировать сохраняемый объект");
        }
        PerformerVerification exist = performerVerificationsRepo.findPerformerVerificationByIdAndDeletedFalse(performerVerification.getId()).orElse(null);
        if (exist == null) {
            throw new UserServiceExeption("Объект для сохранения не найден");
        }
        return performerVerificationsRepo.save(performerVerification);
    }

    @Override
    public void delete(Long id) throws UserServiceExeption {
        PerformerVerification performerVerification = performerVerificationsRepo.findById(id).orElse(null);
        if (performerVerification == null || (performerVerification.getDeleted() != null && performerVerification.getDeleted())) {
            String error = "Не найден удаляемый пользователь с id " + id;
            throw new UserServiceExeption(error);
        }
        performerVerification.setDeleted(true);
        performerVerificationsRepo.save(performerVerification);

    }

    @Override
    public PerformerVerification getOneById(Long id) throws UserServiceExeption {
        PerformerVerification performerVerification = performerVerificationsRepo.findPerformerVerificationByIdAndDeletedFalse(id).orElse(null);
        if (performerVerification == null) {
            throw new UserServiceExeption("Объект не найден");
        }
        return performerVerification;
    }

    @Override
    public List<PerformerVerification> getAll(Map<String, String> params) {
        return performerVerificationsRepo.findAll();
    }

    @Override
    public List<PerformerVerification> getAllNotDeleted() {
       return new ArrayList<>(performerVerificationsRepo.findAllByDeletedFalse());
    }
}
