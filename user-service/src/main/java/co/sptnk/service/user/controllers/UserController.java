package co.sptnk.service.user.controllers;

import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
import co.sptnk.service.user.common.GeneratedCode;
import co.sptnk.service.user.model.Role;
import co.sptnk.service.user.model.dto.Auth;
import co.sptnk.service.user.common.KeycloakProvider;
import co.sptnk.service.user.model.User;
import co.sptnk.service.user.services.IAuthService;
import co.sptnk.service.user.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Hidden;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.annotation.security.RolesAllowed;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Collections;


@Tag(name = "UserController", description = "API объекта User (Пользователь)")
@RestController
@RequestMapping("users")
public class UserController extends AbstractCrudHateoasController<User, UUID> {

    @Autowired
    private IUserService service;

    @Autowired
    private IAuthService authService;

    @Autowired
    private KeycloakProvider keycloakProvider;

    @GetMapping("/{id}")
    public ResponseEntity<User> getOneById(@PathVariable("id") UUID uuid) {
        return new ResponseEntity<>(createLinks(service.getOneById(uuid), uuid, AllowedLinksMethods.GET), HttpStatus.OK);
    }

    @Hidden
    public ResponseEntity<User> add(User user) {
        return null;
    }


    @Override
    @RolesAllowed({Role.ADMIN,Role.PERFORMER,Role.CUSTOMER,Role.MANAGER})
    public ResponseEntity<User> update(@RequestBody User user) {
        ResponseEntity responseEntity;
        if (isUserAdminOrHimself(SecurityContextHolder.getContext(), user.getId()!=null?user.getId().toString():null)){
            responseEntity = new ResponseEntity<>(createLinks(service.update(user), user.getId(), AllowedLinksMethods.PUT), HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @Override
    @RolesAllowed({Role.ADMIN,Role.PERFORMER,Role.CUSTOMER,Role.MANAGER})
    public ResponseEntity<User> delete(@PathVariable UUID id) {
        ResponseEntity responseEntity;
        if (isUserAdminOrHimself(SecurityContextHolder.getContext(), id.toString())){
            service.delete(id);
            responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @GetMapping()
    public ResponseEntity getAll(@RequestParam Map<String, String> map) {
        if (!isUserAdmin(SecurityContextHolder.getContext())){
            map.remove("roles");
            map.put("roles",Role.PERFORMER);
        }
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
    @RolesAllowed({Role.ADMIN,Role.PERFORMER,Role.CUSTOMER,Role.MANAGER})
    public ResponseEntity addInterests(@PathVariable("id") UUID userId, @RequestBody Set<Long> ids) {
        ResponseEntity responseEntity;
        if (isUserAdminOrHimself(SecurityContextHolder.getContext(), userId.toString())){
            responseEntity = new ResponseEntity<>(service.addInterests(ids, userId), HttpStatus.OK);     }
        else {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }


    @DeleteMapping("/{id}/interests")
    @Operation(description = "Удаление интересов у пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Объект успешно удален"),
            @ApiResponse(responseCode = "404", description = "Объект не найден")
    }
    )
    @RolesAllowed({Role.ADMIN,Role.PERFORMER,Role.CUSTOMER,Role.MANAGER})
    public ResponseEntity deleteInterests(@PathVariable("id") UUID userId, @RequestBody Set<Long> ids){
        ResponseEntity responseEntity;
        if (isUserAdminOrHimself(SecurityContextHolder.getContext(), userId.toString())){
            responseEntity = new ResponseEntity<>(service.deleteInterests(ids, userId), HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping("/{id}/setRolePerformer")
    @RolesAllowed({Role.ADMIN,Role.MANAGER})
    public ResponseEntity setRolePerformer(@PathVariable("id") UUID userId) {
        return  new ResponseEntity<>(service.addRoles(Collections.singleton(Role.PERFORMER), userId), HttpStatus.OK);
    }

    @PutMapping("/{id}/cancelRolePerformer")
    @RolesAllowed({Role.ADMIN,Role.PERFORMER,Role.MANAGER})
    public ResponseEntity cancelRolePerformer(@PathVariable("id") UUID userId) {
        ResponseEntity responseEntity;
        if (getUserRole(SecurityContextHolder.getContext()).contains(Role.PERFORMER) && !isUserHimself(SecurityContextHolder.getContext(), userId.toString())) {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            responseEntity = new ResponseEntity<>(service.deleteRoles(Collections.singleton(Role.PERFORMER), userId), HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping("/signin")
    public ResponseEntity<GeneratedCode> signIn(@RequestBody Auth auth) {
        return new ResponseEntity<>(authService.signIn(auth), HttpStatus.OK);
    }

//    @GetMapping("/getToken")
//    public ResponseEntity<AccessTokenResponse> getToken(@RequestParam String userID) {
//        return new ResponseEntity<>(keycloakProvider.getAccessTokenForUser(userID), HttpStatus.OK);
//    }

    @Override
    protected Class<? extends AbstractCrudHateoasController<User, UUID>> getSelfClass() {
        return this.getClass();
    }

    private Boolean isUserAdminOrHimself(SecurityContext context, String userId) {
        SimpleKeycloakAccount simpleKeycloakAccount = getSimpleKeycloakAccount(context);
        return simpleKeycloakAccount != null &&
                (simpleKeycloakAccount.getRoles().contains(Role.ADMIN) || simpleKeycloakAccount.getPrincipal().getName().equals(userId));
    }

    private Boolean isUserHimself(SecurityContext context, String userId) {
        SimpleKeycloakAccount simpleKeycloakAccount = getSimpleKeycloakAccount(context);
        return simpleKeycloakAccount != null &&
                simpleKeycloakAccount.getPrincipal().getName().equals(userId);
    }

    private Boolean isUserAdmin(SecurityContext context) {
        SimpleKeycloakAccount simpleKeycloakAccount = getSimpleKeycloakAccount(context);
        return simpleKeycloakAccount != null &&
                simpleKeycloakAccount.getRoles().contains(Role.ADMIN);
    }

    private Set<String> getUserRole(SecurityContext context) {
        SimpleKeycloakAccount simpleKeycloakAccount = getSimpleKeycloakAccount(context);
        Set<String> roles;
        if (simpleKeycloakAccount != null){
            roles = simpleKeycloakAccount.getRoles();
        }
        else {
            roles = Collections.singleton(Role.ANONYMOUS);
        }
        return roles;
    }

    private SimpleKeycloakAccount getSimpleKeycloakAccount(SecurityContext context) {
        SimpleKeycloakAccount simpleKeycloakAccount = null;
        if (!context.getAuthentication().getName().equals(Role.ANONYMOUS)) {
            simpleKeycloakAccount = (SimpleKeycloakAccount) context.getAuthentication().getDetails();
        }
        return simpleKeycloakAccount;
    }


}
