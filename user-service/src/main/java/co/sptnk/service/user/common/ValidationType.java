package co.sptnk.service.user.common;

public enum ValidationType {
    SING_IN_SMS,
    SIGN_UP_SMS;

    public String getName() {
        return this.name();
    }
}
