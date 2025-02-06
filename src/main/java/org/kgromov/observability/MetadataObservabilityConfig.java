package org.kgromov.observability;

import io.micrometer.core.instrument.MeterRegistry;
import org.kgromov.ChatModelPriceCalculator;
import org.kgromov.ImageModelProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//@ConditionalOnEnabledMetricsExport()
//@ConditionalOnProperty(value = "spring.ai.metrics.enabled", havingValue = "true")3
@EnableAspectJAutoProxy
@Configuration
public class MetadataObservabilityConfig {

    @Bean
    SpringAiMetricsService springAiMetricsService(MeterRegistry registry) {
        return new SpringAiMetricsService(registry);
    }

    // FIXME: chat model actually
    @Bean
    MetadataAdvisor metadataAdvisor(SpringAiMetricsService metricsService, ChatModelPriceCalculator priceCalculator) {
        return new MetadataAdvisor(metricsService, priceCalculator);
    }

    @Bean
    ImageCallAdvisor imageCallAdvisor(SpringAiMetricsService metricsService, ImageModelProperties properties,) {
        return new ImageCallAdvisor(metricsService, properties);
    }
}
