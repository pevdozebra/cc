package co.sptnk.service.mappers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper<Obj, Entity> {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Перенос измененых полей в сущность
     * @param object - переданный запросов объекта
     * @param entity - найденная сущность под объект
     * @return - обновленная сущность
     */
    public Entity toEntity(Obj object, Entity entity) {
        if (object != null && entity != null) {
            modelMapper.map(object, entity);
        }
        return entity;
    }
}
