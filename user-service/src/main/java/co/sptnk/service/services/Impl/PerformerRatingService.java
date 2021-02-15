package co.sptnk.service.services.Impl;

import co.sptnk.service.common.EntityMapper;
import co.sptnk.service.common.PageableCreator;
import co.sptnk.service.model.PerformerRating;
import co.sptnk.service.model.User;
import co.sptnk.service.repositories.PerformerRatingsRepo;
import co.sptnk.service.repositories.UsersRepo;
import co.sptnk.service.services.IPerformerRatingService;
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
    private EntityMapper<PerformerRating, PerformerRating> mapper;
    @Autowired
    private PageableCreator pageableCreator;

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
        return performerRatingsRepo.save(performerRating);
    }

    @Override
    public PerformerRating update(PerformerRating performerRating) {
        if (performerRating.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        PerformerRating exist = performerRatingsRepo.findPerformerRatingByIdAndDeletedFalse(performerRating.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return performerRatingsRepo.save(mapper.toEntity(performerRating, exist));
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
        Page<PerformerRating> page = performerRatingsRepo.findAll(getExample(params),pageableCreator.getPageable(params));
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
        performerRating.setRated(params.get("ratedId") != null ? usersRepo.findById(UUID.fromString(params.get("ratedId"))).orElse(null):null);
        performerRating.setRater(params.get("raterId") != null ? usersRepo.findById(UUID.fromString(params.get("raterId"))).orElse(null):null);
        performerRating.setReasonId(params.get("reasonId") != null ? Long.parseLong(params.get("reasonId")):null);
        performerRating.setRating(params.get("rating") != null ? Integer.parseInt(params.get("rating")):null);
        performerRating.setDate(params.get("date") != null ? LocalDateTime.parse(params.get("date"), formatter):null);
        performerRating.setDeleted(params.get("deleted") != null ? Boolean.parseBoolean(params.get("deleted")): false);
        return Example.of(performerRating);
    }
}
