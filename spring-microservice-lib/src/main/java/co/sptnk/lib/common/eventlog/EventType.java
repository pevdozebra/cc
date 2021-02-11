package co.sptnk.lib.common.eventlog;

public enum EventType{
    INFO,
    WARNING,
    ERROR;

    public String getName() {
        return this.name();
    }
}
