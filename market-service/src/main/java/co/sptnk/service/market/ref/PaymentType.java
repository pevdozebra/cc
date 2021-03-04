package co.sptnk.service.market.ref;

public enum PaymentType {
    FROM_CUSTOMER,
    SYSTEM_FEE,
    TO_PERFORMER;

    public String getName() {
        return this.name();
    }
}
