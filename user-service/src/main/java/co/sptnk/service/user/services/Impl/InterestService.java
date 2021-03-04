package co.sptnk.service.user.services.Impl;


import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import co.sptnk.service.user.common.MessageProducer;
import co.sptnk.service.user.common.PageableCreator;
import co.sptnk.service.user.model.Interest;
import co.sptnk.service.user.repositories.InterestRepo;
import co.sptnk.service.user.repositories.UsersRepo;
import co.sptnk.service.user.services.IInterestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InterestService implements IInterestService {

    @Autowired
    private InterestRepo interestRepo;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MessageProducer message;

    @Override
    public List<Interest> findAllByParent(Long id) {
        Interest interest = interestRepo.findInterestByIdAndDeletedFalse(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ArrayList<>(interestRepo.findAllByParent(interest));
    }

    @Override
    public Interest add(Interest interest){
        if (interest.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        interest.setDeleted(null);
        Interest newInterest = interestRepo.save(interest);
        message.sendLogMessage(
                EventCode.INTEREST_CREATE,
                EventType.INFO,
                EventCode.INTEREST_CREATE.getDescription(newInterest.getId())
        );
        return newInterest;
    }

    @Override
    @Transactional
    public Interest update(Interest interest){
        if (interest.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        interest.setDeleted(null);
        Interest exist = interestRepo.findInterestByIdAndDeletedFalse(interest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(interest, exist);
        message.sendLogMessage(
                EventCode.INTEREST_EDIT,
                EventType.INFO,
                EventCode.INTEREST_EDIT.getDescription(exist.getId())
        );
        return exist;
    }

    @Override
    @Transactional
    public void delete(Long id) {
       Interest interest = interestRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
       interest.setDeleted(true);
        message.sendLogMessage(
                EventCode.INTEREST_DELETE,
                EventType.INFO,
                EventCode.INTEREST_DELETE.getDescription(id)
        );
    }

    @Override
    public Interest getOneById(Long id) {
        Interest interest = interestRepo.findInterestByIdAndDeletedFalse(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return interest;
    }

    @Override
    public List<Interest> getAll(Map<String, String> params) {
        Page<Interest> page;
        try {
            page = interestRepo.findAll(getExample(params), PageableCreator.getPageable(params));
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ArrayList<>(page.getContent());
    }

    private Example<Interest> getExample(Map<String, String> params) {
        Interest interest = new Interest();
        interest.setId(params.get("id") != null ? Long.parseLong(params.get("id")):null);
        interest.setTitle(params.get("title"));
        interest.setParent(params.get("parent_id") != null ? interestRepo.getOne(Long.parseLong(params.get("parent_id"))):null);
        interest.setDeleted(params.get("deleted") != null ? Boolean.parseBoolean(params.get("deleted")): null);
        return Example.of(interest);
    }

    @Override
    public List<Interest> getAllNotDeleted() {
        return new ArrayList<>(interestRepo.findAllByDeletedFalse());
    }
}
