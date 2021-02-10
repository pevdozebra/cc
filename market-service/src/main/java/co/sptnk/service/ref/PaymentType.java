package co.sptnk.service.ref;

public enum PaymentType {
    FROM_CUSTOMER,
    SYSTEM_FEE,
    TO_PERFORMER;

    public String getName() {
        return this.name();
    }
}
