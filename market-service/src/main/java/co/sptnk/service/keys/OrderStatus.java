package co.sptnk.service.keys;

public enum OrderStatus {
    NEW,
    CONFIRMED,
    DECLINED,
    CANCELED,
    FINISHED;

    public String getName() {
        return this.name();
    }
}
