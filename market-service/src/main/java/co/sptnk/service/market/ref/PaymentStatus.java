package co.sptnk.service.market.ref;

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
