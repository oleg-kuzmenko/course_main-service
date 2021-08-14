package ru.digitalleague.backend.mainservice.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class MainServiceConfig {

    @Bean
    public MappedInterceptor metricInterceptor(MeterRegistry registry) {
        return new MappedInterceptor(new String[] {"/**"}, new MetricInterceptor(registry));
    }

}
