package co.sptnk.service.user.common.config;

import co.sptnk.service.user.common.utils.Convertor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("singleton")
public class ConfigStore {

    private final Map<ConfigName, Map<ParamType, String>> configuration = new HashMap<>();

    public void putConfig(ConfigName config, ParamType type, String value) {
        configuration.remove(config);
        configuration.put(config, new HashMap<ParamType, String>(){{
            put(type, value);
        }});
    }

    public Map<Class<?>, Object> config(ConfigName config) {
        Map<ParamType, String> configValue = configuration.get(config);
        ParamType type = configValue.keySet().stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неверная конфигурация"));
        return Convertor.convert(configValue.get(type), type);
    }

    public <D> D getConfig(ConfigName name, Class<D> type) {
        Map<Class<?>, Object> result = this.config(name);
        if (result != null) {
            if (result.containsKey(type)) {
                return (D) result.get(type);
            }
        } else {
            throw new IllegalArgumentException("Аргумент name имеет неправильное значение");
        }
        throw new IllegalArgumentException("Аргумент type имеет неправильное значение");
    }
}
