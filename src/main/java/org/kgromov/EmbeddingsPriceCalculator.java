package org.kgromov;

import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class EmbeddingsPriceCalculator implements PriceCalculator {
    private final EmbeddingsModelProperties properties;

    EmbeddingsPriceCalculator(EmbeddingsModelProperties properties) {
        this.properties = properties;
    }

    @Override
    public double calculate(ChatResponse response) {
        Usage tokensUsage = response.getMetadata().getUsage();
        String model = response.getMetadata().getModel();
        return properties.getModelPrice(model)
                .map(price -> BigDecimal.valueOf(tokensUsage.getTotalTokens())
                        .multiply(BigDecimal.valueOf(price.price()))
                        .divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP)
                        .doubleValue()
                )
                .orElse(Double.NaN);
    }
}
