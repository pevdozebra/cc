package co.sptnk.service.user.services.Impl;

import co.sptnk.service.user.common.PageableCreator;
import co.sptnk.service.user.model.PerformerRating;
import co.sptnk.service.user.model.User;
import co.sptnk.service.user.repositories.PerformerRatingsRepo;
import co.sptnk.service.user.repositories.UsersRepo;
import co.sptnk.service.user.services.IPerformerRatingService;
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
public class PerformerRatingService implements IPerformerRatingService {

    @Autowired
    private PerformerRatingsRepo performerRatingsRepo;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PerformerRating> findAllByPerformer(UUID userId) {
        return new ArrayList<>(performerRatingsRepo.findPerformerRatingByRated_IdAndDeletedFalse(userId));
    }

    @Override
    public PerformerRating add(PerformerRating performerRating) {
        if (performerRating.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User rated = null;
        User rater = null;
        if (performerRating.getRated() != null && performerRating.getRated().getId() != null &&
                performerRating.getRater() != null && performerRating.getRater().getId() != null) {
                 rated = usersRepo.findUserByIdAndDeletedFalse(performerRating.getRated().getId()).orElse(null);
                 rater = usersRepo.findUserByIdAndDeletedFalse(performerRating.getRater().getId()).orElse(null);
        }
        if (rated == null || rater == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        performerRating.setDeleted(null);
        return performerRatingsRepo.save(performerRating);
    }

    @Override
    @Transactional
    public PerformerRating update(PerformerRating performerRating) {
        if (performerRating.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        performerRating.setDeleted(null);
        PerformerRating exist = performerRatingsRepo.findPerformerRatingByIdAndDeletedFalse(performerRating.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(performerRating, exist);
        return exist;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PerformerRating performerRating = performerRatingsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        performerRating.setDeleted(true);
    }

    @Override
    public PerformerRating getOneById(Long id){
        PerformerRating performerRating = performerRatingsRepo.findPerformerRatingByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return performerRating;
    }

    @Override
    public List<PerformerRating> getAll(Map<String, String> params){
        Page<PerformerRating> page;
        try {
            page = performerRatingsRepo.findAll(getExample(params), PageableCreator.getPageable(params));
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ArrayList<>(page.getContent());
    }

    @Override
    public List<PerformerRating> getAllNotDeleted() {
      return new ArrayList<>(performerRatingsRepo.findAllByDeletedFalse());
    }

    private Example<PerformerRating> getExample(Map<String, String> params){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        PerformerRating performerRating = new PerformerRating();
        performerRating.setId(params.get("id") != null ? Long.parseLong(params.get("id")):null);
        performerRating.setRated(params.get("ratedId") != null ? usersRepo.getOne(UUID.fromString(params.get("ratedId"))):null);
        performerRating.setRater(params.get("raterId") != null ? usersRepo.getOne(UUID.fromString(params.get("raterId"))):null);
        performerRating.setReasonId(params.get("reasonId") != null ? Long.parseLong(params.get("reasonId")):null);
        performerRating.setRating(params.get("rating") != null ? Integer.parseInt(params.get("rating")):null);
        performerRating.setDate(params.get("date") != null ? LocalDateTime.parse(params.get("date"), formatter):null);
        performerRating.setDeleted(params.get("deleted") != null ? Boolean.parseBoolean(params.get("deleted")): null);
        return Example.of(performerRating);
    }
}
