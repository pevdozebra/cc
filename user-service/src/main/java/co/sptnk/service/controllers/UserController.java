package co.sptnk.service.controllers;

import co.sptnk.lib.base.AbstractCHController;
import co.sptnk.lib.exceptions.ServiceException;
import co.sptnk.lib.keys.AllowedLinksMethods;
import co.sptnk.service.model.User;
import co.sptnk.service.services.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "UserController", description = "API объекта User (Пользователь)")
@RestController
@RequestMapping("users")
public class UserController extends AbstractCHController<User, UUID> {

    @Autowired
    private IUserService service;

    @GetMapping("/{id}")
    public ResponseEntity<User> getOneById(@PathVariable("id") UUID uuid) {
        User entity;
        try {
            entity = createLinks(service.getOneById(uuid), uuid, AllowedLinksMethods.GET);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @Hidden
    public ResponseEntity<User> add(User user) {
        return null;
    }

    @Override
    public ResponseEntity<User> update(@RequestBody User user) {
        User result;
        try {
            result = createLinks(service.update(user), user.getId(), AllowedLinksMethods.PUT);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> delete(@RequestBody UUID id) {
        try {
            service.delete(id);
        } catch (ServiceException e) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getAll(Map<String, String> map) {
        return new ResponseEntity<>(service.getAllNotDeleted(), HttpStatus.OK);
    }

    @Override
    protected Class<? extends AbstractCHController<User, UUID>> getSelfClass() {
        return this.getClass();
    }
}
