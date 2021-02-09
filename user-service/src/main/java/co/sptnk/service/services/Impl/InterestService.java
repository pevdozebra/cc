package co.sptnk.service.services.Impl;

import co.sptnk.service.exceptions.UserServiceExeption;
import co.sptnk.service.model.Interest;
import co.sptnk.service.repositories.InterestRepo;
import co.sptnk.service.services.IInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InterestService implements IInterestService {

    @Autowired
    private InterestRepo interestRepo;

    @Override
    public List<Interest> findAllByParent(Long id) throws UserServiceExeption {
        Interest interest = interestRepo.findInterestByIdAndDeletedFalse(id).orElse(null);
        if (interest == null) {
            throw new UserServiceExeption("Объект не найден");
        }
        return new ArrayList<>(interestRepo.findAllByParent(interest));
    }

    @Override
    public Interest add(Interest interest) throws UserServiceExeption {
        if (interest.getId() != null) {
            throw new UserServiceExeption("ID объекта должен быть пуст");
        }
        return interestRepo.save(interest);
    }

    @Override
    public Interest update(Interest interest) throws UserServiceExeption {
        if (interest.getId() == null) {
            throw new UserServiceExeption("Невозможно идентифицировать сохраняемый объект");
        }
        Interest exist = interestRepo.findInterestByIdAndDeletedFalse(interest.getId()).orElse(null);
        if (exist == null) {
            throw new UserServiceExeption("Объект для сохранения не найден");
        }
        return interestRepo.save(interest);
    }

    @Override
    public void delete(Long id) throws UserServiceExeption {
        Interest interest = interestRepo.findById(id).orElse(null);
        if (interest == null) {
            String error = "Не найден удаляемый интерес с id " + id;
            throw new UserServiceExeption(error);
        }
       interestRepo.findAllByParent(interest).forEach(subInterest -> {
               subInterest.setDeleted(true);
               interestRepo.save(subInterest);
        });
       interest.setDeleted(true);
       interestRepo.save(interest);
    }

    @Override
    public Interest getOneById(Long id) throws UserServiceExeption {
        Interest interest = interestRepo.findInterestByIdAndDeletedFalse(id).orElse(null);
        if (interest == null) {
            throw new UserServiceExeption("Объект не найден");
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
