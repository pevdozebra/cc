package co.sptnk.service.services.Impl;

import co.sptnk.service.model.Card;
import co.sptnk.service.model.User;
import co.sptnk.service.repositories.CardsRepo;
import co.sptnk.service.repositories.UsersRepo;
import co.sptnk.service.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public List<Card> getAllForUser(UUID userId)  {
        User user = usersRepo.findUserByIdAndDeletedFalse(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ArrayList<>(cardsRepo.findCardByUserAndDeletedFalse(user));
    }

    @Override
    public Card add(Card card) {
        if (card.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return cardsRepo.save(card);
    }

    @Override
    public Card update(Card card) {
        if (card.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Card exist = cardsRepo.findCardByIdAndDeletedFalse(card.getId()).orElse(null);
        if (exist == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return cardsRepo.save(card);
    }

    @Override
    public void delete(Long id) {
        Card card = cardsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (card.getDeleted() != null && card.getDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        card.setDeleted(true);
        cardsRepo.save(card);
    }

    @Override
    public Card getOneById(Long id) {
        Card card = cardsRepo.findCardByIdAndDeletedFalse(id).orElse(null);
        if (card == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
