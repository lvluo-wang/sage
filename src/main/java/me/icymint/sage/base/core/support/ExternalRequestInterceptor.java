package me.icymint.sage.base.core.support;

import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.base.spec.def.MagicConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by daniel on 16/9/4.
 */
@Component
public class ExternalRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ExternalRequestInterceptor.class);

    @Autowired(required = false)
    private RuntimeContext runtimeContext;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logger.info("External: {} {}", request.getMethod(), request.getURI());
        if (runtimeContext != null) {
            String correlationId = runtimeContext.getCorrelationId();
            if (correlationId != null && !request.getHeaders().containsKey(MagicConstants.CORRELATION_ID_HEADER)) {
                request.getHeaders().add(MagicConstants.CORRELATION_ID_HEADER, correlationId);
            }
        }
        ClientHttpResponse response = execution.execute(request, body);
        logger.info("External: {}", response.getRawStatusCode());
        return response;
    }
}