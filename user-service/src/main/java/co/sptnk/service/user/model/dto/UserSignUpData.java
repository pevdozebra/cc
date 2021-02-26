package co.sptnk.service.user.model.dto;

import co.sptnk.service.user.model.ref.RegistrationType;
import co.sptnk.service.user.model.ref.SocialMediaType;
import co.sptnk.service.user.model.ref.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserSignUpData {

    /**
     * Тип пользователя (заказчик/исполнитель)
     */
    private UserType userType;

    /**
     * Имя
     */
    private String firstName;

    /**
     * Фамилия
     */
    private String lastName;

    /**
     * Телефон
     */
    private String phone;

    /**
     * email
     */
    private String email;

    /**
     * Псевдоним
     */
    private String penName;

    /**
     * Тип соцсети
     */
    private SocialMediaType socialMediaType;

    /**
     * Ссылка на соцсеть
     */
    private String socialMediaUrl;
    /**
     * Регистрация через представителя/лично
     */
    private RegistrationType registrationType;
}
