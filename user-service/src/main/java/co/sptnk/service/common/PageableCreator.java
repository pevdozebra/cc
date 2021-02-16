package co.sptnk.service.common;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class PageableCreator  {
    public static final Integer PAGE_DEFAULT = 0;
    public static final Integer SIZE_DEFAULT = 10;

    public static Pageable getPageable(Map<String, String> params) throws NumberFormatException {
        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")):PAGE_DEFAULT;
        Integer entryQuantity = params.get("size") != null ? Integer.parseInt(params.get("size")):SIZE_DEFAULT;
        return  PageRequest.of(page, entryQuantity);
    }
}


