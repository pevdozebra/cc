package co.sptnk.service.keys;

public enum PaymentStatus {
    NEW,
    PROCESSING,
    FINISHED,
    FAILED,
    CANCELED;

    public String getName() {
        return this.name();
    }
}
