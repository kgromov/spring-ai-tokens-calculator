package org.kgromov.observability;

import io.micrometer.core.instrument.MeterRegistry;
import org.kgromov.ChatModelPriceCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@ConditionalOnEnabledMetricsExport()
//@ConditionalOnProperty(value = "spring.ai.metrics.enabled", havingValue = "true")
@Configuration
public class MetadataObservabilityConfig {

    @Bean
    SpringAiMetricsService springAiMetricsService(MeterRegistry registry) {
        return new SpringAiMetricsService(registry);
    }

    @Bean
    MetadataAdvisor metadataAdvisor(SpringAiMetricsService metricsService, ChatModelPriceCalculator priceCalculator) {
        return new MetadataAdvisor(metricsService, priceCalculator);
    }
}
