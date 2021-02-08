package co.sptnk.lib.keys;

public enum EventType{
    INFO,
    WARNING,
    ERROR;

    public String getName() {
        return this.name();
    }
}
