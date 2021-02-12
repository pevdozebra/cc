package co.sptnk.service.controllers;

import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
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
public class UserController extends AbstractCrudHateoasController<User, UUID> {

    @Autowired
    private IUserService service;

    @GetMapping("/{id}")
    public ResponseEntity<User> getOneById(@PathVariable("id") UUID uuid) {
        return new ResponseEntity<>(createLinks(service.getOneById(uuid), uuid, AllowedLinksMethods.GET),
                HttpStatus.OK);
    }

    @Hidden
    public ResponseEntity<User> add(User user) {
        return null;
    }

    @Override
    public ResponseEntity<User> update(@RequestBody User user) {
        return new ResponseEntity<>( createLinks(service.update(user), user.getId(), AllowedLinksMethods.PUT),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> delete(@PathVariable UUID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getAll(Map<String, String> map) {
        return new ResponseEntity<>(service.getAllNotDeleted(), HttpStatus.OK);
    }

    @Override
    protected Class<? extends AbstractCrudHateoasController<User, UUID>> getSelfClass() {
        return this.getClass();
    }
}
