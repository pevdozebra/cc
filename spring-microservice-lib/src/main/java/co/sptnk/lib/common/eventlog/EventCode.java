package co.sptnk.lib.common.eventlog;

public enum EventCode {
    UNKNOWN("Код не определен"),
    // События пользователя
    USER_REGISTRATION("Регистрация пользователя"),
    USER_EDIT_PROFILE("Редактирование профиля пользователя"),
    USER_AUTHORISATION("Авторизация пользователя"),
    USER_BLOCK("Блокировка пользователя"),
    USER_UNBLOCK("Разблокировка пользователя"),
    // События заказов
    ORDER_CREATE("Создание заказа"),
    ORDER_EDIT("Редактирование заказа"),
    ORDER_DELETE("Удаление заказа"),
    // События платежей
    PAYMENT_CREATE("Создание платежа"),
    PAYMENT_EDIT("Редактирование платежа"),
    PAYMENT_DELETE("Удаление платежа"),
    // События продуктовых типов
    PRODUCT_TYPE_CREATE("Создание нового типа продукта"),
    PRODUCT_TYPE_EDIT("Редактирование типа продукта"),
    PRODUCT_TYPE_DELETE("Удаление типа продукта"),
    // События продуктов
    PRODUCT_CREATE("Создание продукта"),
    PRODUCT_EDIT("Редактирование продукта"),
    PRODUCT_DELETE("Удаление продукта");
    // События взаимодействия p2p
    // Системные события

    private final String description;

    EventCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getDescription(Long id) {
        String result = description + " с id = %s";
        return String.format(result, id);
    }

    public String getName() {
        return this.name();
    }
}
