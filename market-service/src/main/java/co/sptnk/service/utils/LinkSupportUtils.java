package co.sptnk.service.utils;


import co.sptnk.service.controllers.ProductsListController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public class LinkSupportUtils {

    public static String getAllProductsPageableSupport(Integer page, Integer offset) {
        return linkTo(
                methodOn(ProductsListController.class).getAll(page, offset)).withSelfRel().getHref();
    }
}
