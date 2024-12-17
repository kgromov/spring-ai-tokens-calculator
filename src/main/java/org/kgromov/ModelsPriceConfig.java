package org.kgromov;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfiguration
@EnableConfigurationProperties({
        TextModelProperties.class,
        EmbeddingsModelProperties.class,
        ImageModelProperties.class,
        AudioModelProperties.class
})
@Configuration
public class ModelsPriceConfig {

    @Bean
    TextModelPriceCalculator priceCalculator(TextModelProperties properties) {
        return new TextModelPriceCalculator(properties);
    }

    @Bean
    EmbeddingsPriceCalculator embeddingsPriceCalculator(EmbeddingsModelProperties properties) {
        return new EmbeddingsPriceCalculator(properties);
    }

    @Bean
    ImagePriceCalculator imagePriceCalculator(ImageModelProperties properties) {
        return new ImagePriceCalculator(properties);
    }

    @Bean
    AudioPriceCalculator audioPriceCalculator(AudioModelProperties properties) {
        return new AudioPriceCalculator(properties);
    }
}
