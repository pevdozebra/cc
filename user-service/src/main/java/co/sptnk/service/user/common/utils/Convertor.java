package co.sptnk.service.user.common.utils;

import co.sptnk.service.user.common.config.ParamType;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class Convertor {


    private static Map<ParamType, Function<String, Map<Class<?>, Object>>> map =
            new HashMap<ParamType, Function<String, Map<Class<?>, Object>>>() {{
        put(ParamType.BOOL, s -> new HashMap<Class<?>, Object>() {{
           put(ParamType.BOOL.getValueClass(), s.equals("true"));
        }});
        put(ParamType.INT, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.INT.getValueClass(), Integer.parseInt(s));
        }});
        put(ParamType.LONG, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.LONG.getValueClass(), Long.parseLong(s));
        }});
        put(ParamType.STRING, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.STRING.getValueClass(), s);
        }});
        put(ParamType.DATE, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.DATE.getValueClass(), LocalDate.parse(s));
            put(ParamType.STRING.getValueClass(), s);
        }});
        put(ParamType.TIME, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.TIME.getValueClass(), OffsetTime.parse(s));
            put(ParamType.LONG.getValueClass(), OffsetTime.parse(s).getLong(ChronoField.NANO_OF_DAY));
        }});
        put(ParamType.DATE_TIME, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.DATE_TIME.getValueClass(), OffsetDateTime.parse(s));
            put(ParamType.LONG.getValueClass(), OffsetDateTime.parse(s).getLong(ChronoField.ERA));
        }});
        put(ParamType.SECOND, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.LONG.getValueClass(), Long.parseLong(s));
            put(ParamType.SECOND.getValueClass(), Duration.ofSeconds(Long.parseLong(s)));
        }});
        put(ParamType.MINUTE, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.LONG.getValueClass(), Long.parseLong(s));
            put(ParamType.MINUTE.getValueClass(), Duration.ofMinutes(Long.parseLong(s)));
        }});
        put(ParamType.HOUR, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.LONG.getValueClass(), Long.parseLong(s));
            put(ParamType.HOUR.getValueClass(), Duration.ofHours(Long.parseLong(s)));
        }});
        put(ParamType.DAY, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.INT.getValueClass(), Integer.parseInt(s));
            put(ParamType.DAY.getValueClass(), Duration.ofDays(Long.parseLong(s)));
        }});
        put(ParamType.WEEK, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.WEEK.getValueClass(), Integer.parseInt(s));
        }});
        put(ParamType.MONTH, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.MONTH.getValueClass(), Integer.parseInt(s));
        }});
        put(ParamType.YEAR, s -> new HashMap<Class<?>, Object>() {{
            put(ParamType.YEAR.getValueClass(), Integer.parseInt(s));
        }});
    }};

    public static Map<Class<?>, Object> convert(String value, ParamType type) {
        return map.get(type).apply(value);
    }
}
