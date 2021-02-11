package co.sptnk.service.ref;

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
