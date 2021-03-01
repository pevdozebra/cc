package co.sptnk.lib.common.eventlog;

public enum EventCode {
    UNKNOWN("Код не определен"),
    // События пользователя
    USER_REGISTRATION("Регистрация пользователя"),
    USER_EDIT_PROFILE("Редактирование профиля пользователя"),
    USER_EDIT_INTERESTS("Редактирование интересов пользователя"),
    USER_AUTHORISATION("Авторизация пользователя"),
    USER_BLOCK("Блокировка пользователя"),
    USER_UNBLOCK("Разблокировка пользователя"),
    // События заказов
    ORDER_CREATE("Создание заказа"),
    ORDER_EDIT("Редактирование заказа"),
    ORDER_DELETE("Удаление заказа"),
    // События интересов
    INTEREST_CREATE("Создание интереса"),
    INTEREST_EDIT("Редактирование интереса"),
    INTEREST_DELETE("Удаление интереса"),
    // События оценок
    RATING_CREATE("Создание оценки"),
    RATING_EDIT("Редактирование оценки"),
    RATING_DELETE("Удаление оценки"),
    // События карточек
    CARD_CREATE("Создание карточки"),
    CARD_EDIT("Редактирование карточки"),
    CARD_DELETE("Удаление карточки"),
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
    PRODUCT_DELETE("Удаление продукта"),
    // События взаимодействия p2p
    // Системные события
    USER_SERVICE_CONFIG_UPDATE("Обновление кофигурации user-service через api."),
    MARKET_SERVICE_CONFIG_UPDATE("Обновление конфигурации market-service через api.");

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
