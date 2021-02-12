package co.sptnk.service.services.Impl;

import co.sptnk.service.model.PerformerRating;
import co.sptnk.service.model.User;
import co.sptnk.service.repositories.PerformerRatingsRepo;
import co.sptnk.service.repositories.UsersRepo;
import co.sptnk.service.services.IPerformerRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public List<PerformerRating> findAllByPerformer(UUID userId) {
        User user = usersRepo.findUserByIdAndDeletedFalse(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ArrayList<>(performerRatingsRepo.findPerformerRatingByRatedAndDeletedFalse(user));
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
        PerformerRating exist = performerRatingsRepo.findPerformerRatingByIdAndDeletedFalse(performerRating.getId()).orElse(null);
        if (exist == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return performerRatingsRepo.save(performerRating);
    }

    @Override
    public void delete(Long id) {
        PerformerRating performerRating = performerRatingsRepo.findById(id).orElse(null);
        if (performerRating == null || (performerRating.getDeleted() != null && performerRating.getDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        performerRating.setDeleted(true);
        performerRatingsRepo.save(performerRating);

    }

    @Override
    public PerformerRating getOneById(Long id){
        PerformerRating performerRating = performerRatingsRepo.findPerformerRatingByIdAndDeletedFalse(id).orElse(null);
        if (performerRating == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return performerRating;
    }

    @Override
    public List<PerformerRating> getAll(Map<String, String> params) {
        return performerRatingsRepo.findAll();
    }

    @Override
    public List<PerformerRating> getAllNotDeleted() {
      return new ArrayList<>(performerRatingsRepo.findAllByDeletedFalse());
    }
}
