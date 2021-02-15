package co.sptnk.service.common;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PageableCreator  {
    public static final Integer pageDefault = 0;
    public static final Integer rowsNumberDefault = 10;

    public Pageable getPageable(Map<String, String> params) {
        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")):pageDefault;
        Integer entryQuantity = params.get("entryQuantity") != null ? Integer.parseInt(params.get("entryQuantity")):rowsNumberDefault;
        return  PageRequest.of(page, entryQuantity);
    }
}


