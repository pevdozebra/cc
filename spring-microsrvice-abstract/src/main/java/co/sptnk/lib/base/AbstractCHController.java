package co.sptnk.lib.base;

import co.sptnk.lib.keys.AllowedLinksMethods;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Базовый контроллера для CRUD операции с поддержкой HATEOAS
 * @param <Entity> - сущность БД
 */
public abstract class AbstractCHController<Entity extends RepresentationModel<Entity>, ID> {

    private AbstractCHController<Entity, ID> controller ;

    protected void init(AbstractCHController<Entity, ID> controller) {
        this.controller = controller;
    }

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
    public abstract ResponseEntity<Entity> getOneById(ID id);

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
    public abstract ResponseEntity<Entity> add(Entity entity);

    /**
     * Обновление объекта
     * @param entity - объект
     */
    @PutMapping
    @Operation(description = "Обновление объекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объект успешно сохранен"),
            @ApiResponse(responseCode = "404", description = "Объект для сохранения не найден")
    }
    )
    public abstract ResponseEntity<Entity> update(Entity entity);

    /**
     * Удаление объекта по идентификатору
     * @param id - идетификатор объекта
     * @return ResponseEntity с кодом ошибки или успеха
     */
    @DeleteMapping
    @Operation(description = "Удаление объекта по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Объект успешно удален"),
            @ApiResponse(responseCode = "404", description = "Объект не найден")
    }
    )
    public abstract ResponseEntity<Entity> delete(ID id);

    @SuppressWarnings("rawtypes")
    @GetMapping
    public abstract ResponseEntity getAll(Map<String, String> map);

    public Entity createLinks(Entity entity, ID id, AllowedLinksMethods method) {
        Map<AllowedLinksMethods, Link> links = new HashMap<AllowedLinksMethods, Link>(){{
            put(AllowedLinksMethods.POST,
                    linkTo(methodOn(controller.getClass()).add(entity)).withSelfRel().withName("add").withType(HttpMethod.POST.name()));
            put(AllowedLinksMethods.PUT,
                    linkTo(methodOn(controller.getClass()).update(entity)).withSelfRel().withName("update").withType(HttpMethod.PUT.name()));
            put(AllowedLinksMethods.DELETE,
                    linkTo(methodOn(controller.getClass()).delete(id)).withSelfRel().withName("delete").withType(HttpMethod.DELETE.name()));
            put(AllowedLinksMethods.GET,
                    linkTo(methodOn(controller.getClass()).getOneById(id)).withSelfRel().withName("get").withType(HttpMethod.GET.name()));
        }};
        links.keySet().forEach(key -> {
            if (key != method) {
                entity.add(links.get(key));
            }
        });
        return entity;
    }
}
