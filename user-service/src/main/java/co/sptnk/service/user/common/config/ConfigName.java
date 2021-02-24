package co.sptnk.service.user.common.config;

public enum ConfigName {

    // Настройки валидации sms
    VALIDATION_SMS_SEND_MAX,
    VALIDATION_SMS_SEND_BLOCK_PERIOD,
    VALIDATION_SMS_TIMEOUT,
    VALIDATION_SMS_TEST_MODE,
    VALIDATION_SMS_TEST_CODE,
    VALIDATION_SMS_CODE_LENGTH;

    public String getName() {
        return this.name();
    }
}
