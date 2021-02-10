package co.sptnk.service.ref;

public enum ProductStatus {
    DRAFT,
    ACTIVE,
    ARCHIVED;

    public String getName() {
        return this.name();
    }
}
