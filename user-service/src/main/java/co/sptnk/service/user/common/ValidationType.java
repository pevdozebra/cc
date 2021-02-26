package co.sptnk.service.user.common;

public enum ValidationType {
    SIGN_IN_SMS,
    SIGN_UP_SMS;

    public String getName() {
        return this.name();
    }
}
