package com.hojak99.slack.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class SlackSecurityService {
    private static final String HMAC_SHA256 = "HmacSHA256";

    private final Mac macSha256;

    public SlackSecurityService(Mac macSha256) {
        this.macSha256 = macSha256;
    }

    /**
     * replay 공격을 막기 위해 '현재 시간' - '5분' 한 시간보다 과거의 요청은 막도록 함
     *
     * @param timeStamp
     * @return
     */
    public boolean isValidByRequestDate(Long timeStamp) {
        Date requestDate = Date.from(Instant.ofEpochSecond(timeStamp));
        LocalDateTime requestLocalDateTime = LocalDateTime.ofInstant(requestDate.toInstant(), ZoneId.systemDefault());
        return !requestLocalDateTime.isBefore(LocalDateTime.now().minusMinutes(5));
    }

    /**
     * Hmac 인증기법을 사용하여 슬랙에서 요청한 정상적인 request 인지 체크하도록 함
     * @param slackSignature
     * @param timeStamp
     * @param requestBody
     * @return
     */
    public boolean isValidRequestByHmac(String slackSignature, Long timeStamp, String requestBody) {
        String baseString = "v0:" + timeStamp + ":" + requestBody;
        return slackSignature.equals("v0=" + encodeBaseStringByHmac256(baseString));
    }

    private String encodeBaseStringByHmac256(String baseString) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec("slack app 의 Signing Secret key를 넣도록 함".getBytes(), HMAC_SHA256);
            macSha256.init(secretKeySpec);
            byte[] bytes = macSha256.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(bytes);
        } catch (InvalidKeyException e) {
            log.error("[SlackSecurityService.encodeBaseStringByHmac256] encoding error", e);
        }

        return "";
    }

}
