package co.sptnk.service.user.services.Impl;

import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import co.sptnk.service.user.common.MessageProducer;
import co.sptnk.service.user.common.PageableCreator;
import co.sptnk.service.user.model.Card;
import co.sptnk.service.user.repositories.CardsRepo;
import co.sptnk.service.user.repositories.UsersRepo;
import co.sptnk.service.user.services.ICardService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CardService implements ICardService {

    @Autowired
    private CardsRepo cardsRepo;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MessageProducer message;

    @Override
    public List<Card> getAllForUser(UUID userId)  {
        return new ArrayList<>(cardsRepo.findCardByUser_IdAndDeletedFalse(userId));
    }

    @Override
    public Card add(Card card) {
        if (card.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        card.setDeleted(null);
        Card newCard = cardsRepo.save(card);
        message.sendLogMessage(
                EventCode.CARD_CREATE,
                EventType.INFO,
                EventCode.CARD_CREATE.getDescription(newCard.getId())
        );
        return newCard;
    }

    @Override
    @Transactional
    public Card update(Card card) {
        if (card.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        card.setDeleted(null);
        Card exist = cardsRepo.findCardByIdAndDeletedFalse(card.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(card, exist);
        message.sendLogMessage(
                EventCode.CARD_EDIT,
                EventType.INFO,
                EventCode.CARD_EDIT.getDescription(card.getId())
        );
        return exist;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Card card = cardsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        card.setDeleted(true);
        message.sendLogMessage(
                EventCode.CARD_DELETE,
                EventType.INFO,
                EventCode.CARD_DELETE.getDescription(id)
        );
    }

    @Override
    public Card getOneById(Long id) {
        Card card = cardsRepo.findCardByIdAndDeletedFalse(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return card;
    }

    @Override
    public List<Card> getAll(Map<String, String> params) {
        Page<Card> page;
        try {
            page = cardsRepo.findAll(getExample(params), PageableCreator.getPageable(params));
        }
        catch (Exception e){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ArrayList<>(page.getContent());
    }

    @Override
    public List<Card> getAllNotDeleted() {
        return new ArrayList<>(cardsRepo.findAllByDeletedFalse());
    }

    private Example<Card> getExample(Map<String, String> params){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Card card = new Card();
        card.setId(params.get("id") != null ? Long.parseLong(params.get("id")):null);
        card.setUser(params.get("userId") != null ? usersRepo.getOne(UUID.fromString(params.get("userId"))):null);
        card.setBindDate(params.get("bindDate") != null ? OffsetDateTime.parse(params.get("bindDate"), formatter):null);
        card.setMaskedNumber(params.get("maskedNumber"));
        card.setAquireUrl(params.get("aquireUrl"));
        card.setArchived(params.get("archived") != null ? Boolean.parseBoolean(params.get("archived")): null);
        card.setDeleted(params.get("deleted") != null ? Boolean.parseBoolean(params.get("deleted")): null);
        return Example.of(card);
    }
}
