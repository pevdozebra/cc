package co.sptnk.service.ref;

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
