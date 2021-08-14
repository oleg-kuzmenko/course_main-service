package ru.digitalleague.backend.mainservice.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MetricInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(MetricInterceptor.class);

    private MeterRegistry registry;

    public MetricInterceptor(MeterRegistry registry) {
        this.registry = registry;
    }

    private String URI, pathKey, METHOD;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        URI = request.getRequestURI();
        METHOD = request.getMethod();

        if (!URI.startsWith("/actuator/prometheus")) {
            logger.info(" >> PATH: {}", URI);
            logger.info(" >> METHOD: {}", METHOD);
            pathKey = "api_"
                    .concat(METHOD.toLowerCase())
                    .concat(URI.replaceAll("/", "_").toLowerCase());
            this.registry.counter(pathKey).increment();
        }

    }
}
