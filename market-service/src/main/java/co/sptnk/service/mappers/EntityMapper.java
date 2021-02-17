package co.sptnk.service.mappers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper<OBJ, ENTITY> {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Перенос измененых полей в сущность
     * @param object - переданный запросов объекта
     * @param entity - найденная сущность под объект
     * @return - обновленная сущность
     */
    public ENTITY toEntity(OBJ object, ENTITY entity) {
        if (object != null && entity != null) {
            modelMapper.map(object, entity);
        }
        return entity;
    }
}
