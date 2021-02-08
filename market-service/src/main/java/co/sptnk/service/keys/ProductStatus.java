package co.sptnk.service.keys;

public enum ProductStatus {
    DRAFT,
    ACTIVE,
    ARCHIVED;

    public String getName() {
        return this.name();
    }
}
