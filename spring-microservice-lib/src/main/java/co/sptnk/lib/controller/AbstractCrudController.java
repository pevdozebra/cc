package co.sptnk.lib.controller;

import co.sptnk.lib.service.AbstractCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Базовый контроллера для CRUD операции
 * @param <ENTITY> - сущность БД
 */
public abstract class AbstractCrudController<ENTITY, ID> {

    private final AbstractCrudService<ENTITY, ID> service;

    public AbstractCrudController(AbstractCrudService<ENTITY, ID> service) {
        this.service = service;
    }

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
    public ResponseEntity<ENTITY> add(@RequestBody ENTITY entity) {
        return new ResponseEntity<>(service.add(entity), HttpStatus.CREATED);
    }

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
    public ResponseEntity<ENTITY> update(@RequestBody ENTITY entity) {
        return new ResponseEntity<>(service.update(entity),
                HttpStatus.OK);
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
    public ResponseEntity<ENTITY> getOneById(@PathVariable("id") ID id) {
        return new ResponseEntity<>(service.getOneById(id),
                HttpStatus.OK);
    }

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
    public ResponseEntity<ENTITY> delete(@PathVariable("id") ID id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Получение списка продуктов
     * @param params - параметры запроса
     * @return список продуктов
     */
    @GetMapping
    @Operation(parameters = {
            @Parameter(name = "page", description = "Номер страницы", required = false),
            @Parameter(name = "count", description = "Количество элементов на страницу", required = false),
            @Parameter(name = "Другие опциональные параметры", description = "Описаны в контроллере"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список объектов"),
            @ApiResponse(responseCode = "400", description = "Неверно заданы параметры")
    }
    )
    public ResponseEntity<?> getAll(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(service.getAll(params), HttpStatus.ACCEPTED);
    }
}
