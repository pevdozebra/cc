package co.sptnk.lib.keys;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EventType{
    INFO,
    WARNING,
    ERROR;


    @JsonCreator
    public static EventType fromNode(JsonNode node) {
        if (!node.has("type"))
            return null;
        String name = node.get("type").asText();
        return EventType.valueOf(name);
    }
    @JsonProperty
    public String getName() {
        return name();
    }

}
