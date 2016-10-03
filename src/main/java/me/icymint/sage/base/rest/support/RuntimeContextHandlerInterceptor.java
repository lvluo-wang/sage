package me.icymint.sage.base.rest.support;

import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.base.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.util.UUID;

import static me.icymint.sage.base.rest.util.StringLogs.append;

/**
 * Created by daniel on 16/9/3.
 */
@Component
public class RuntimeContextHandlerInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(RuntimeContextHandlerInterceptor.class);

    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    Clock clock;
    @Value("${" + Magics.PROP_BASE_URL + "}")
    String baseUrl;
    @Value("${spring.jackson.time-zone:UTC}")
    String timezone;


    private String buildCorrelationId(HttpServletRequest request) {
        String id = request.getHeader(Magics.HEADER_CORRELATION_ID);
        if (StringUtils.isEmpty(id)) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader(Magics.HEADER_X_TIMESTAMP, String.valueOf((clock.timestamp())));
        runtimeContext.setCorrelationId(buildCorrelationId(request));

        String userAddress = StringUtils.isEmpty(request.getHeader(Magics.HEADER_FORWARDED_FOR_HEADER))
                ? request.getRemoteHost()
                : request.getHeader(Magics.HEADER_FORWARDED_FOR_HEADER);
        if (StringUtils.hasText(userAddress) && userAddress.indexOf(',') > 0) {
            userAddress = userAddress.substring(0, userAddress.indexOf(','));
        }

        runtimeContext.setUserAddress(userAddress);
        String requestPath = request.getRequestURI();
        String requestQueryString = request.getQueryString();
        if (!StringUtils.isEmpty(requestQueryString)) {
            requestPath += "?" + requestQueryString;
        }

        runtimeContext.setResourcePath(requestPath);
        // parser request base url
        String proto = request.getHeader(Magics.HEADER_X_FORWARDED_PROTO);
        String host = request.getHeader(Magics.HEADER_X_FORWARDED_HOST);
        String port = request.getHeader(Magics.HEADER_X_FORWARDED_PORT);
        if (StringUtils.hasText(proto) && StringUtils.hasText(host) && StringUtils.hasText(port)) {
            String requestBaseUrl = proto + "://" + host + ":" + port;
            runtimeContext.setRequestBaseUrl(requestBaseUrl);
        } else {
            runtimeContext.setRequestBaseUrl(baseUrl);
        }

        String timeZone = request.getHeader(Magics.HEADER_X_TIMEZONE);
        ZoneId zone = null;
        try {
            zone = StringUtils.isEmpty(timeZone) ? null : ZoneId.of(timeZone);
        } catch (Exception e) {
            Exceptions.catching(e, false);
        }
        if (zone != null) {
            runtimeContext.setTimeZone(zone);
        }
        response.setHeader(Magics.HEADER_X_TIMEZONE, this.timezone);

        runtimeContext.setRefer(request.getHeader(Magics.HEADER_REFERER));
        logger.info(buildRequestLog(request));
        return true;
    }


    private String buildRequestLog(HttpServletRequest request) {
        StringBuilder accessLog = new StringBuilder();
        append(accessLog, request.getRemoteHost());
        append(accessLog, request.getHeader(Magics.HEADER_FORWARDED_FOR_HEADER));
        append(accessLog, request.getRemoteUser());
        append(accessLog, request.getMethod());
        append(accessLog, buildRequestPath(request));
        append(accessLog, request.getHeader(Magics.HEADER_REFERER));
        append(accessLog, request.getHeader(Magics.HEADER_USER_AGENT));
        return accessLog.toString().substring(1);
    }

    private String buildRequestPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        String requestQueryString = request.getQueryString();
        if (!StringUtils.isEmpty(requestQueryString)) {
            requestPath += "?" + requestQueryString;
        }

        return requestPath;
    }

    private String buildResponseLog(HttpServletResponse response) {
        StringBuilder responseLog = new StringBuilder();
        responseLog.append(Integer.toString(response.getStatus()));
        append(responseLog, response.getHeader("Content-Length"));
        append(responseLog, response.getHeader("ETag"));
        return responseLog.toString();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info(buildResponseLog(response));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        runtimeContext.clear();
    }
}
