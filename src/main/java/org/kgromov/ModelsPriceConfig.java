package org.kgromov;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfiguration
@EnableConfigurationProperties({
        ChatModelProperties.class,
        EmbeddingsModelProperties.class,
        ImageModelProperties.class,
        AudioModelProperties.class
})
@Configuration
public class ModelsPriceConfig {

    @Bean
    ChatModelPriceCalculator priceCalculator(ChatModelProperties properties) {
        return new ChatModelPriceCalculator(properties);
    }

    @Bean
    EmbeddingsPriceCalculator embeddingsPriceCalculator(EmbeddingsModelProperties properties) {
        return new EmbeddingsPriceCalculator(properties);
    }

    @Bean
    AudioPriceCalculator audioPriceCalculator(AudioModelProperties properties) {
        return new AudioPriceCalculator(properties);
    }
}
