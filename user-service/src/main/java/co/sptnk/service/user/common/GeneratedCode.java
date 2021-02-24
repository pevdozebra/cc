package co.sptnk.service.user.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

@Getter
@Setter
@NoArgsConstructor
public class GeneratedCode {

    private String code;

    public GeneratedCode(int length) {
        this.code = RandomStringUtils.randomNumeric(length);
    }

    public GeneratedCode(String code) {
        this.code = code;
    }

    public String hash() {
        return Hex.encodeHexString(DigestUtils.sha256(code));
    }

}
