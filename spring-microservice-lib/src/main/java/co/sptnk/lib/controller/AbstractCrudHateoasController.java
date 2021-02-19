package co.sptnk.lib.controller;

import co.sptnk.lib.constant.AllowedLinksMethods;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Базовый контроллера для CRUD операции с поддержкой HATEOAS
 * @param <ENTITY> - сущность БД
 */
public abstract class AbstractCrudHateoasController<ENTITY extends RepresentationModel<ENTITY>, ID> {

    /**
     * Получение продукта по идентификатору
     * @param id - идентификатор объекта
     * @return объект продукта по идентификатору
     */
    @GetMapping("/{id}")
    @Operation(parameters = {
            @Parameter(name = "id", description = "Идентификатор"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен объект"),
            @ApiResponse(responseCode = "404", description = "Объект не найден")
    }
    )
    public abstract ResponseEntity<ENTITY> getOneById(@PathVariable("id") ID id);

    /**
     * Добавление объекта
     * @param entity - объект
     * @return Добавленный объект
     */
    @PostMapping
    @Operation(description = "Сохранение объекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Объект успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Объект уже существует")
    }
    )
    public abstract ResponseEntity<ENTITY> add(@RequestBody ENTITY entity);

    /**
     * Обновление объекта
     * @param entity - объект
     */
    @PutMapping
    @Operation(description = "Обновление объекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объект успешно сохранен"),
            @ApiResponse(responseCode = "400", description = "Объект не существует")
    }
    )
    public abstract ResponseEntity<ENTITY> update(@RequestBody ENTITY entity);

    /**
     * Удаление объекта по идентификатору
     * @param id - идетификатор объекта
     * @return ResponseEntity с кодом ошибки или успеха
     */
    @DeleteMapping("/{id}")
    @Operation(description = "Удаление объекта по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Объект успешно удален"),
            @ApiResponse(responseCode = "404", description = "Объект не найден")
    }
    )
    public abstract ResponseEntity<ENTITY> delete(@PathVariable("id") ID id);


    @GetMapping
    public abstract ResponseEntity<?> getAll(@RequestParam Map<String, String> map);

    /**
     * В реализации вернуть this.getClass()
     * @return класс контроллера
     */
    protected abstract Class<? extends AbstractCrudHateoasController<ENTITY, ID>> getSelfClass();

    protected ENTITY createLinks(ENTITY entity, ID id, AllowedLinksMethods method) {
        Map<AllowedLinksMethods, Link> links = new HashMap<AllowedLinksMethods, Link>(){{
            put(AllowedLinksMethods.POST,
                    linkTo(methodOn(getSelfClass()).add(entity)).withSelfRel().withName("add").withType(HttpMethod.POST.name()));
            put(AllowedLinksMethods.PUT,
                    linkTo(methodOn(getSelfClass()).update(entity)).withSelfRel().withName("update").withType(HttpMethod.PUT.name()));
            put(AllowedLinksMethods.DELETE,
                    linkTo(methodOn(getSelfClass()).delete(id)).withSelfRel().withName("delete").withType(HttpMethod.DELETE.name()));
            put(AllowedLinksMethods.GET,
                    linkTo(methodOn(getSelfClass()).getOneById(id)).withSelfRel().withName("get").withType(HttpMethod.GET.name()));
        }};
        links.keySet().forEach(key -> {
            if (key != method) {
                entity.add(links.get(key));
            }
        });
        return entity;
    }
}
