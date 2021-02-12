package co.sptnk.service.services.Impl;

import co.sptnk.service.model.PerformerVerification;
import co.sptnk.service.repositories.PerformerVerificationsRepo;
import co.sptnk.service.services.IPerformerVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PerformerVerificationService implements IPerformerVerificationService {

    @Autowired
    private PerformerVerificationsRepo performerVerificationsRepo;

    @Override
    public PerformerVerification add(PerformerVerification performerVerification) {
        if (performerVerification.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return performerVerificationsRepo.save(performerVerification);
    }

    @Override
    public PerformerVerification update(PerformerVerification performerVerification) {
        if (performerVerification.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        PerformerVerification exist = performerVerificationsRepo.findPerformerVerificationByIdAndDeletedFalse(performerVerification.getId()).orElse(null);
        if (exist == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return performerVerificationsRepo.save(performerVerification);
    }

    @Override
    public void delete(Long id) {
        PerformerVerification performerVerification = performerVerificationsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (performerVerification.getDeleted() != null && performerVerification.getDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        performerVerification.setDeleted(true);
        performerVerificationsRepo.save(performerVerification);

    }

    @Override
    public PerformerVerification getOneById(Long id) {
        PerformerVerification performerVerification = performerVerificationsRepo.findPerformerVerificationByIdAndDeletedFalse(id).orElse(null);
        if (performerVerification == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
