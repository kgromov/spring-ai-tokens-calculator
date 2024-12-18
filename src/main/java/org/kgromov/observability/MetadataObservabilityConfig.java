package org.kgromov.observability;

import io.micrometer.core.instrument.MeterRegistry;
import org.kgromov.TextModelPriceCalculator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

//@ConditionalOnEnabledMetricsExport()
//@ConditionalOnProperty(value = "spring.ai.metrics.enabled", havingValue = "true")
@Configuration
public class MetadataObservabilityConfig {

    @Bean
    SpringAiMetricsService springAiMetricsService(MeterRegistry registry) {
        return new SpringAiMetricsService(registry);
    }

    @Bean
    MetadataAdvisor metadataAdvisor(SpringAiMetricsService metricsService, TextModelPriceCalculator priceCalculator) {
        return new MetadataAdvisor(metricsService, priceCalculator);
    }
}
