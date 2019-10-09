package com.hojak99.slack.config;

import com.hojak99.slack.service.SlackSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
@Configuration
public class SecurityInterceptor implements HandlerInterceptor {
    private static final String REQUEST_TIMESTAMP = "X-Slack-Request-Timestamp";
    private static final String SLACK_SIGNATURE = "X-Slack-Signature";

    private final SlackSecurityService slackSecurityService;

    public SecurityInterceptor(SlackSecurityService slackSecurityService) {
        this.slackSecurityService = slackSecurityService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String requestBody = parameterMapToString(request.getParameterMap());
            log.info("[SecurityInterceptor.preHandle] request body to String: {}", requestBody);

            Long requestTimeStamp = Long.parseLong(request.getHeader(REQUEST_TIMESTAMP));
            return slackSecurityService.isValidByRequestDate(requestTimeStamp) &&
                    slackSecurityService.isValidRequestByHmac(request.getHeader(SLACK_SIGNATURE), requestTimeStamp, requestBody);
        } catch (Exception e) {
            log.error("[SecurityInterceptor.preHandle] error for request", e);
            throw new Exception("비정상적인 요청입니다.");
        }
    }

    /**
     * HttpServletRequest 의 getInputStream() 을 이용하여 request body 를 얻을 수 있으나 controller 에서
     * 매핑을 받지 못함. 그렇기 때문에 HttpServletRequestWrapper 상속하는 클래스를 따로 생성하고 getInputStream() 을
     * overriding 하여 구현할 수 있음. 본 예제에서는 그 부분은 다루지 않기 때문에 좋은 코드는 아니나 간단히 코딩하도록 함.
     *
     * @param requestParameterMap
     * @return
     */
    private String parameterMapToString(Map<String, String[]> requestParameterMap) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;

        for (Map.Entry<String, String[]> entry : requestParameterMap.entrySet()) {
            String value = entry.getValue()[0];

            if (entry.getKey().equals("response_url") || entry.getKey().equals("command") || entry.getKey().equals("text")) {
                value = URLEncoder.encode(value, "UTF-8");
            }

            stringBuilder
                    .append(entry.getKey())
                    .append("=")
                    .append(value);

            count += 1;
            if (count != requestParameterMap.size()) {
                stringBuilder.append("&");
            }
        }

        return stringBuilder.toString();
    }
}
