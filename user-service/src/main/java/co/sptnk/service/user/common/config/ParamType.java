package co.sptnk.service.user.common.config;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

public enum ParamType {

    BOOL(Boolean.class),
    INT(Integer.class),
    LONG(Long.class),
    STRING(String.class),
    DATE(LocalDate.class),
    TIME(OffsetTime.class),
    DATE_TIME(OffsetDateTime.class),
    SECOND(Duration.class),
    MINUTE(Duration.class),
    HOUR(Duration.class),
    DAY(Duration.class),
    WEEK(Integer.class),
    MONTH(Integer.class),
    YEAR(Integer.class);

    private final Class<?> valueClass;

    ParamType(Class<?> object) {
        this.valueClass = object;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }

    public String getName() {
        return this.name();
    }
}
