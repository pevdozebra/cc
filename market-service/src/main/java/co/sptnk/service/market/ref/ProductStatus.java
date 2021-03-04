package co.sptnk.service.market.ref;

public enum ProductStatus {
    DRAFT,
    ACTIVE,
    ARCHIVED;

    public String getName() {
        return this.name();
    }
}
