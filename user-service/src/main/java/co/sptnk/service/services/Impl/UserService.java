package co.sptnk.service.services.Impl;

import co.sptnk.service.exceptions.UserServiceExeption;
import co.sptnk.service.model.Card;
import co.sptnk.service.model.PerformerRating;
import co.sptnk.service.model.User;
import co.sptnk.service.repositories.CardsRepo;
import co.sptnk.service.repositories.PerformerRatingsRepo;
import co.sptnk.service.repositories.UsersRepo;
import co.sptnk.service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private CardsRepo cardsRepo;

    @Autowired
    private PerformerRatingsRepo performerRatingsRepo;

    @Override
    public User add(User user) throws UserServiceExeption {
        if (user.getId() != null) {
            throw new UserServiceExeption("ID объекта должен быть пуст");
        }
        return usersRepo.save(user);
    }

    @Override
    public User update(User user) throws UserServiceExeption {
        if (user.getId() == null) {
            throw new UserServiceExeption("Невозможно идентифицировать сохраняемый объект");
        }
        User exist = usersRepo.findUserByIdAndDeletedFalse(user.getId()).orElse(null);
        if (exist == null) {
            throw new UserServiceExeption("Объект для сохранения не найден");
        }
        return usersRepo.save(user);
    }

    @Override
    public void delete(UUID uuid) throws UserServiceExeption {
        User user = usersRepo.findById(uuid).orElse(null);
        if (user == null || (user.getDeleted() != null && user.getDeleted())) {
            String error = "Не найден удаляемый пользователь с id " + uuid;
            throw new UserServiceExeption(error);
        }
        List<Card> cards = (List<Card>) cardsRepo.findCardByUserAndDeletedFalse(user);
        cards.forEach(card -> {
            card.setDeleted(true);
            cardsRepo.save(card);
        });
        List<PerformerRating> ratings = (List<PerformerRating>) performerRatingsRepo.findPerformerRatingByRatedAndDeletedFalse(user);
        ratings.forEach(rating -> {
            rating.setDeleted(true);
            performerRatingsRepo.save(rating);
        });

        user.getInterests().clear();
        user.setDeleted(true);
        usersRepo.save(user);
    }

    @Override
    public User getOneById(UUID uuid) throws UserServiceExeption {
        User user = usersRepo.findUserByIdAndDeletedFalse(uuid).orElse(null);
        if (user == null) {
            throw new UserServiceExeption("Объект не найден");
        }
        return user;
    }

    @Override
    public List<User> getAll(Map<String, String> params) {
        return usersRepo.findAll();
    }

    @Override
    public List<User> getAllNotDeleted() {
        return new ArrayList<>(usersRepo.findAllByDeletedFalse());
    }
}
