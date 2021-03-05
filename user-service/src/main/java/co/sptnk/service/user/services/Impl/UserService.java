package co.sptnk.service.user.services.Impl;

import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import co.sptnk.service.user.common.KeycloakProvider;
import co.sptnk.service.user.common.MessageProducer;
import co.sptnk.service.user.common.PageableCreator;
import co.sptnk.service.user.model.Interest;
import co.sptnk.service.user.model.Role;
import co.sptnk.service.user.model.User;
import co.sptnk.service.user.model.UserDetails;
import co.sptnk.service.user.model.dto.UserSignUpData;
import co.sptnk.service.user.repositories.InterestRepo;
import co.sptnk.service.user.repositories.UsersRepo;
import co.sptnk.service.user.services.IUserService;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private InterestRepo interestRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private Environment environment;
    @Autowired
    private KeycloakProvider keycloakProvider;
    @Autowired
    private MessageProducer message;


    @Override
    public User add(User user) {
        return null;
    }

    @Override
    @Transactional
    public User update(User user) {
        if (user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setDeleted(null);
        User exist = usersRepo.findUserByIdAndDeletedFalse(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(user, exist);
        updateUserInKeyclock(exist, false);
        message.sendLogMessage(
                EventCode.USER_EDIT_PROFILE,
                EventType.INFO,
                EventCode.USER_EDIT_PROFILE.getDescription() + "с id: " + exist.getId().toString()
        );
        return exist;
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        User user = usersRepo.findById(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        blockUserInKeyclock(user);
        user.setDeleted(true);
        message.sendLogMessage(
                EventCode.USER_BLOCK,
                EventType.INFO,
                EventCode.USER_BLOCK.getDescription() + "с id: " + uuid.toString()
        );
    }

    @Override
    @Transactional
    public User getOneById(UUID uuid) {
        User user = usersRepo.findUserByIdAndDeletedFalse(uuid).orElse(null);
        if (user == null) {
            User deletedUser = usersRepo.findById(uuid).orElse(null);
            RealmResource realmResource = keycloakProvider.get().realm(environment.getProperty("keycloak.realm"));
            UserResource userResource = realmResource.users().get(uuid.toString());
            user = getUserFromKeyclock(userResource);
            if (user != null && !user.getBlocked() && deletedUser == null) {
                usersRepo.save(user);
                usersRepo.flush();
                user.setRoles(getUserRoleFromKeyclock(userResource, user));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        return user;
    }

    @Override
    public List<User> getAll(Map<String, String> params) {
        Page<User> page;
        try {
            page = usersRepo.findAll(getSpecification(params), PageableCreator.getPageable(params));
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ArrayList<>(page.getContent());
    }

    @Override
    public List<User> getAllNotDeleted() {
        return new ArrayList<>(usersRepo.findAllByDeletedFalse());
    }

    @Override
    @Transactional
    public Set<Interest> addInterests(Set<Long> ids, UUID userId) {
        User user = usersRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Set<Interest> interests = new HashSet<>(interestRepo.findByDeletedFalseAndIdIn(ids));
        user.getInterests().addAll(interests);
        message.sendLogMessage(
                EventCode.USER_EDIT_INTERESTS,
                EventType.INFO,
                "Добавление интересов пользователю с id: " + userId.toString()
        );
        return user.getInterests();
    }

    @Override
    @Transactional
    public Set<Interest> deleteInterests(Set<Long> ids, UUID userId) {
        User user = usersRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Set<Interest> interests = new HashSet<>(interestRepo.findByDeletedFalseAndIdIn(ids));
        user.getInterests().removeAll(interests);
        message.sendLogMessage(
                EventCode.USER_EDIT_INTERESTS,
                EventType.INFO,
                "Удаление интересов пользователя с id: " + userId.toString()
        );
        return user.getInterests();
    }

    @Override
    @Transactional
    public Set<Role> addRoles(Set<String> roles, UUID userId) {
        User user = usersRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Set<Role> givenRoles = roles.stream().map(r->new Role(null, user, r)).collect(Collectors.toSet());
        if (user.getRoles() != null) {
            user.getRoles().addAll(givenRoles);
        }
        else {
            user.setRoles(givenRoles);
        }
        updateUserInKeyclock(user, true);
        message.sendLogMessage(
                EventCode.USER_EDIT_ROLES,
                EventType.INFO,
                "Добавление ролей пользователя с id: " + userId.toString()
        );
        return user.getRoles();
    }

    @Override
    @Transactional
    public Set<Role> deleteRoles(Set<String> roles, UUID userId) {
        User user = usersRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Set<Role> givenRoles = user.getRolesByName(roles);
        if (user.getRoles() != null) {
            user.getRoles().removeAll(givenRoles);
            updateUserInKeyclock(user, true);
            message.sendLogMessage(
                    EventCode.USER_EDIT_ROLES,
                    EventType.INFO,
                    "Добавление ролей пользователя с id: " + userId.toString()
            );
        }
        return user.getRoles();
    }


    public User createUser(UserSignUpData userSignUpData){
        if (usersRepo.findUserByUsername(userSignUpData.getPhone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = new UserDetails(userSignUpData);
        User user = new User(userSignUpData);
        String userId = createKeycloakUser(user);

        user.setId(UUID.fromString(userId));
        userDetails.setId(UUID.fromString(userId));
        user.setUserDetails(userDetails);
        return usersRepo.save(user);
    }

    private String createKeycloakUser(User user) {
        String userId;
        try {
            Keycloak keycloak = keycloakProvider.get();
            RealmResource realmResource = keycloak.realm(environment.getProperty("keycloak.realm"));
            UserRepresentation userRepresentation = realmResource
                    .users()
                    .search(user.getUsername())
                    .stream()
                    .findFirst()
                    .orElse(null);

            if  (userRepresentation !=null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                userRepresentation = new UserRepresentation();
            }

            userRepresentation.setEnabled(true);
            userRepresentation = updateUserRepresentationFromUser(userRepresentation, user);
            Response response = realmResource.users().create(userRepresentation);
            userId = CreatedResponseUtil.getCreatedId(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return userId;
    }

    private User getUserFromKeyclock(UserResource userResource){
        User user = null;
        if (userResource != null) {
            user = new User(userResource);
        }
        return user;
    }

    private Set<Role> getUserRoleFromKeyclock(UserResource userResource, User user){
        Set<Role> roles = new HashSet<>();
        if (userResource != null) {
            roles = userResource.roles().realmLevel().listAll()
                    .stream().map(r-> new Role(null, user, r.getName())).collect(Collectors.toSet());
        }
        return roles;
    }


    private void updateUserInKeyclock(User user, Boolean updateRoles){
        RealmResource realmResource = keycloakProvider.get().realm(environment.getProperty("keycloak.realm"));
        UserResource userResource = realmResource.users().get(user.getId().toString());
        UserRepresentation userRepresentation = updateUserRepresentationFromUser(userResource.toRepresentation(), user);
        userResource.update(userRepresentation);
        if (updateRoles) {updateUserRoleInKeyclock(user, realmResource, userResource);}
    }
    private void updateUserRoleInKeyclock(User user, RealmResource realmResource, UserResource userResource){
        if (user.getRoles() != null) {
            Set<String> givenRoles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
            List<RoleRepresentation> roleRepresentationList = realmResource.roles().list()
                    .stream().filter(r->givenRoles.contains(r.getName())).collect(Collectors.toList());
            userResource.roles().realmLevel().remove(realmResource.roles().list());
            userResource.roles().realmLevel().add(roleRepresentationList);
        }
    }

    private void blockUserInKeyclock(User user) {
        RealmResource realmResource = keycloakProvider.get().realm(environment.getProperty("keycloak.realm"));
        UserResource userResource = realmResource.users().get(user.getId().toString());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(false);
        userResource.update(userRepresentation);
    }

    private UserRepresentation updateUserRepresentationFromUser(UserRepresentation userRepresentation, User user) {
        if (userRepresentation != null && user != null) {
            userRepresentation.setUsername(user.getUsername());
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setEnabled(user.getBlocked() == null || !user.getBlocked());
        }
        return userRepresentation;
    }

    public Specification<User> getSpecification(Map<String, String> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new Specification<User>() {
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (params.get("id") != null) {
                    predicates.add(cb.equal(root.get("id"), UUID.fromString(params.get("id"))));
                }
                if (params.get("firstname") != null) {
                    predicates.add(cb.equal(root.get("firstName"), params.get("firstname")));
                }
                if (params.get("lastname") != null) {
                    predicates.add(cb.equal(root.get("lastName"), params.get("lastname")));
                }
                if (params.get("username") != null) {
                    predicates.add(cb.equal(root.get("username"), params.get("username")));
                }
                if (params.get("email") != null) {
                    predicates.add(cb.equal(root.get("email"), params.get("email")));
                }
                if (params.get("birthDateFrom") != null) {
                    predicates.add(cb.greaterThan(root.get("birthDate"), LocalDate.parse(params.get("birthDateFrom"), formatter)));
                }
                if (params.get("birthDateTo") != null) {
                    predicates.add(cb.lessThan(root.get("birthDate"), LocalDate.parse(params.get("birthDateTo"), formatter)));
                }
                if (params.containsKey("interests")) {
                    Join<User, Interest> joinUserInterest = root.join("interests", JoinType.LEFT);
                    Set<Long> givenInterests = Arrays.stream(params.get("interests").split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toSet());
                    List<Interest> interestList = new ArrayList<>(interestRepo.findByDeletedFalseAndIdIn(givenInterests));
                    predicates.add(joinUserInterest.in(interestList));
                }
                if (params.containsKey("roles")) {
                    Join<User, Role> joinUserRole = root.join("roles", JoinType.LEFT);
                    List<String> roleList = Arrays.asList(params.get("roles").split(","));
                    predicates.add(joinUserRole.get("name").in(roleList));
                }
                if (params.get("blocked") != null) {
                    predicates.add(cb.equal(root.get("deleted"), Boolean.parseBoolean(params.get("blocked"))));
                }
                if (params.get("deleted") != null) {
                    predicates.add(cb.equal(root.get("deleted"), Boolean.parseBoolean(params.get("deleted"))));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
