package co.sptnk.service.services.Impl;

import co.sptnk.service.exceptions.UserServiceExeption;
import co.sptnk.service.model.Card;
import co.sptnk.service.model.PerformerRating;
import co.sptnk.service.model.User;
import co.sptnk.service.repositories.CardsRepo;
import co.sptnk.service.repositories.UsersRepo;
import co.sptnk.service.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CardService implements ICardService {

    @Autowired
    private CardsRepo cardsRepo;
    @Autowired
    private UsersRepo usersRepo;

    @Override
    public List<Card> getAllForUser(UUID userId) throws UserServiceExeption {

        User user = usersRepo.findUserByIdAndDeletedFalse(userId).orElse(null);
        if (user == null) {
            throw new UserServiceExeption(String.format("Пользователя с таким Id %s не существует", userId));
        }
        return new ArrayList<>(cardsRepo.findCardByUserAndDeletedFalse(user));
    }

    @Override
    public Card add(Card card) throws UserServiceExeption {
        if (card.getId() != null) {
            throw new UserServiceExeption("ID объекта должен быть пуст");
        }
        return cardsRepo.save(card);
    }

    @Override
    public Card update(Card card) throws UserServiceExeption {
        if (card.getId() == null) {
            throw new UserServiceExeption("Невозможно идентифицировать сохраняемый объект");
        }
        Card exist = cardsRepo.findCardByIdAndDeletedFalse(card.getId()).orElse(null);
        if (exist == null) {
            throw new UserServiceExeption("Объект для сохранения не найден");
        }
        return cardsRepo.save(card);
    }

    @Override
    public void delete(Long id) throws UserServiceExeption {
        Card card = cardsRepo.findById(id).orElse(null);
        if (card == null || (card.getDeleted() != null && card.getDeleted())) {
            String error = "Не найден удаляемый пользователь с id " + id;
            throw new UserServiceExeption(error);
        }
        card.setDeleted(true);
        cardsRepo.save(card);
    }

    @Override
    public Card getOneById(Long id) throws UserServiceExeption {
        Card card = cardsRepo.findCardByIdAndDeletedFalse(id).orElse(null);
        if (card == null) {
            throw new UserServiceExeption("Объект не найден");
        }
        return card;
    }

    @Override
    public List<Card> getAll(Map<String, String> params) {
        return cardsRepo.findAll();
    }

    @Override
    public List<Card> getAllNotDeleted() {
        return new ArrayList<>(cardsRepo.findAllByDeletedFalse());
    }
}
