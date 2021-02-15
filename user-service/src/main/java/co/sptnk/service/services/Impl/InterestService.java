package co.sptnk.service.services.Impl;

import co.sptnk.service.common.EntityMapper;
import co.sptnk.service.common.PageableCreator;
import co.sptnk.service.model.Interest;
import co.sptnk.service.model.User;
import co.sptnk.service.repositories.InterestRepo;
import co.sptnk.service.repositories.UsersRepo;
import co.sptnk.service.services.IInterestService;
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
import java.util.UUID;

@Service
public class InterestService implements IInterestService {

    @Autowired
    private InterestRepo interestRepo;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private EntityMapper<Interest, Interest> mapper;
    @Autowired
    private PageableCreator pageableCreator;

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
        return interestRepo.save(interest);
    }

    @Override
    public Interest update(Interest interest){
        if (interest.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Interest exist = interestRepo.findInterestByIdAndDeletedFalse(interest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return interestRepo.save(mapper.toEntity(interest, exist));
    }

    @Override
    @Transactional
    public void delete(Long id) {
       Interest interest = interestRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
       interestRepo.findAllByParent(interest).forEach(subInterest -> {
               subInterest.setDeleted(true);
        });
       interest.setDeleted(true);
    }

    @Override
    public Interest getOneById(Long id) {
        Interest interest = interestRepo.findInterestByIdAndDeletedFalse(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return interest;
    }

    @Override
    public List<Interest> getAll(Map<String, String> params) {
        Page<Interest> page = interestRepo.findAll(getExample(params),pageableCreator.getPageable(params,0,10));
        return new ArrayList<>(page.getContent());
    }

    private Example<Interest> getExample(Map<String, String> params) {
        Interest interest = new Interest();
        interest.setId(params.get("id") != null ? Long.parseLong(params.get("id")):null);
        interest.setTitle(params.get("title"));
        interest.setParent(params.get("parent_id") != null ? interestRepo.findById(Long.parseLong(params.get("parent_id"))).orElse(null):null);
        interest.setDeleted(params.get("deleted") != null ? Boolean.parseBoolean(params.get("deleted")): false);
        if (params.get("userId") != null) {
            User user = usersRepo.findUserByIdAndDeletedFalse(UUID.fromString(params.get("userId"))).orElse(null);
            List<User> list = new ArrayList<>();
            list.add(user);
            interest.setUsers(list);
        }
        return Example.of(interest);
    }

    @Override
    public List<Interest> getAllNotDeleted() {
        return new ArrayList<>(interestRepo.findAllByDeletedFalse());
    }
}
