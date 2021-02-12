package co.sptnk.service.services.Impl;

import co.sptnk.service.model.Interest;
import co.sptnk.service.repositories.InterestRepo;
import co.sptnk.service.services.IInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InterestService implements IInterestService {

    @Autowired
    private InterestRepo interestRepo;

    @Override
    public List<Interest> findAllByParent(Long id) {
        Interest interest = interestRepo.findInterestByIdAndDeletedFalse(id).orElse(null);
        if (interest == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
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
        Interest exist = interestRepo.findInterestByIdAndDeletedFalse(interest.getId()).orElse(null);
        if (exist == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return interestRepo.save(interest);
    }

    @Override
    public void delete(Long id) {
        Interest interest = interestRepo.findById(id).orElse(null);
        if (interest == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
       interestRepo.findAllByParent(interest).forEach(subInterest -> {
               subInterest.setDeleted(true);
               interestRepo.save(subInterest);
        });
       interest.setDeleted(true);
       interestRepo.save(interest);
    }

    @Override
    public Interest getOneById(Long id) {
        Interest interest = interestRepo.findInterestByIdAndDeletedFalse(id).orElse(null);
        if (interest == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return interest;
    }

    @Override
    public List<Interest> getAll(Map<String, String> params) {
        return interestRepo.findAll();
    }

    @Override
    public List<Interest> getAllNotDeleted() {
        return new ArrayList<>(interestRepo.findAllByDeletedFalse());
    }
}
