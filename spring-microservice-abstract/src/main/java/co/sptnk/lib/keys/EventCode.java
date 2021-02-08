package co.sptnk.lib.keys;

public enum EventCode {
    UNKNOWN("Код не определен"),
    // События пользователя
    USER_REGISTRATION("Регистрация пользователя"),
    USER_EDIT_PROFILE("Редактирование профиля пользователя"),
    USER_AUTHORISATION("Авторизация пользователя"),
    USER_BLOCK("Блокировка пользователя"),
    USER_UNBLOCK("Разблокировка пользователя"),
    // События продуктов
    PRODUCT_CREAT("Создание продукта"),
    PRODUCT_EDIT("Редактирование продукта");
    // События взаимодействия p2p
    // Системные события

    private final String description;

    EventCode(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public String getName() {
        return this.name();
    }
}
