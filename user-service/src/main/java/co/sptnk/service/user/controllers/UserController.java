package co.sptnk.service.user.controllers;

import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
import co.sptnk.service.user.model.User;
import co.sptnk.service.user.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.Map;
import java.util.Set;
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
        return new ResponseEntity<>(createLinks(service.update(user), user.getId(), AllowedLinksMethods.PUT),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> delete(@PathVariable UUID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity getAll(@RequestParam  Map<String, String> map) {
        return new ResponseEntity<>(service.getAll(map), HttpStatus.OK);
    }


    @PostMapping("/{id}/interests")
    @Operation(description = "Добавление интересов для пользователя", parameters = {
            @Parameter(name = "id", description = "Идентификатор пользователя")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен объект"),
            @ApiResponse(responseCode = "404", description = "Объект не найден")
    }
    )
    public ResponseEntity addInterests(@PathVariable("id") UUID userId, @RequestBody Set<Long> ids) {
        return new ResponseEntity<>(service.addInterests(ids, userId), HttpStatus.OK);
    }


    @DeleteMapping("/{id}/interests")
    @Operation(description = "Удаление интересов у пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Объект успешно удален"),
            @ApiResponse(responseCode = "404", description = "Объект не найден")
    }
    )
    public ResponseEntity deleteInterests(@PathVariable("id") UUID userId, @RequestBody Set<Long> ids){
        return new ResponseEntity<>(service.deleteInterests(ids, userId), HttpStatus.NO_CONTENT);
    }

    @Override
    protected Class<? extends AbstractCrudHateoasController<User, UUID>> getSelfClass() {
        return this.getClass();
    }
}
