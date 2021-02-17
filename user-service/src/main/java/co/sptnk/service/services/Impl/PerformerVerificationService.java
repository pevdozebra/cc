package co.sptnk.service.services.Impl;

import co.sptnk.service.common.PageableCreator;
import co.sptnk.service.model.PerformerVerification;
import co.sptnk.service.repositories.PerformerVerificationsRepo;
import co.sptnk.service.repositories.UsersRepo;
import co.sptnk.service.services.IPerformerVerificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PerformerVerificationService implements IPerformerVerificationService {

    @Autowired
    private PerformerVerificationsRepo performerVerificationsRepo;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PerformerVerification add(PerformerVerification performerVerification) {
        if (performerVerification.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        performerVerification.setDeleted(null);
        return performerVerificationsRepo.save(performerVerification);
    }

    @Override
    @Transactional
    public PerformerVerification update(PerformerVerification performerVerification) {
        if (performerVerification.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        performerVerification.setDeleted(null);
        PerformerVerification exist = performerVerificationsRepo.findPerformerVerificationByIdAndDeletedFalse(performerVerification.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(performerVerification, exist);
        return exist;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PerformerVerification performerVerification = performerVerificationsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        performerVerification.setDeleted(true);
    }

    @Override
    public PerformerVerification getOneById(Long id) {
        PerformerVerification performerVerification = performerVerificationsRepo.findPerformerVerificationByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return performerVerification;
    }

    @Override
    public List<PerformerVerification> getAll(Map<String, String> params) {
        Page<PerformerVerification> page;
        try {
            page = performerVerificationsRepo.findAll(getExample(params), PageableCreator.getPageable(params));
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ArrayList<>(page.getContent());
    }

    @Override
    public List<PerformerVerification> getAllNotDeleted() {
       return new ArrayList<>(performerVerificationsRepo.findAllByDeletedFalse());
    }

    private Example<PerformerVerification> getExample(Map<String, String> params){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        PerformerVerification performerVerification = new PerformerVerification();
        performerVerification.setId(params.get("id") != null ? Long.parseLong(params.get("id")):null);
        performerVerification.setPerformer(params.get("performerId") != null ? usersRepo.getOne(UUID.fromString(params.get("performerId"))):null);
        performerVerification.setVerifier(params.get("verifierId") != null ? usersRepo.getOne(UUID.fromString(params.get("verifierId"))):null);
        performerVerification.setCreateDate(params.get("createDate") != null ? LocalDateTime.parse(params.get("createDate"), formatter):null);
        performerVerification.setDecisionDate(params.get("decisionDate") != null ? LocalDateTime.parse(params.get("decisionDate"), formatter):null);
        performerVerification.setSuccessDecision(params.get("successDecision") != null ? Boolean.parseBoolean(params.get("successDecision")): null);
        performerVerification.setDeleted(params.get("deleted") != null ? Boolean.parseBoolean(params.get("deleted")): false);
        return Example.of(performerVerification);
    }
}
