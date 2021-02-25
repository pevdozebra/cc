package co.sptnk.service.user.model;

import co.sptnk.service.user.model.dto.UserSignUpData;
import co.sptnk.service.user.model.ref.RegistrationType;
import co.sptnk.service.user.model.ref.SocialMediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_details")
public class UserDetails {

    @Id
    private UUID id;

    @Column(name = "average_rating")
    private BigDecimal averageRating;
    private Double averageRating;

    /**
     * Ссылка на соцсеть
     */
    @Column(name = "socialmedia_url ")
    private String socialMediaUrl;

    /**
     * Тип соцсети
     */
    @Column(name = "socialmedia_type")
    @Enumerated(EnumType.STRING)
    private SocialMediaType socialMediaType;

    /**
     * Псевдоним
     */
    @Column(name = "pen_name ")
    private String penName;

    /**
     * Регистрация через представителя/лично
     */
    @Column(name = "registration_type")
    @Enumerated(EnumType.STRING)
    private RegistrationType registrationType;


    public UserDetails(UserSignUpData userSignUpData) {
        this.socialMediaUrl = userSignUpData.getSocialMediaUrl();
        this.socialMediaType = userSignUpData.getSocialMediaType();
        this.penName = userSignUpData.getPenName();
        this.registrationType = userSignUpData.getRegistrationType();
    }
}

