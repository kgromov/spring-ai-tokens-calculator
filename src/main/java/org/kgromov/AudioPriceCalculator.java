package org.kgromov;

import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Optional.ofNullable;

public final class AudioPriceCalculator implements PriceCalculator {
    private final AudioModelProperties properties;

    AudioPriceCalculator(AudioModelProperties properties) {
        this.properties = properties;
    }

    @Override
    public double calculate(ChatResponse response) {
        Usage tokensUsage = response.getMetadata().getUsage();
        String model = response.getMetadata().getModel();
        return properties.getModelPrice(model)
                .map(price -> this.calculate(price, new TokenUsage(
                        tokensUsage.getPromptTokens(),
                        tokensUsage.getGenerationTokens())
                ))
                .orElse(Double.NaN);
    }

    private double calculate(AudioModelPrice modelPrice, TokenUsage usage) {
        return ofNullable(modelPrice.price())
                .map(price -> BigDecimal.valueOf(usage.total()).multiply(BigDecimal.valueOf(price)))
                .orElseGet(() -> BigDecimal.valueOf(usage.prompt()).multiply(BigDecimal.valueOf(modelPrice.input()))
                        .add(BigDecimal.valueOf(usage.generated()).multiply(BigDecimal.valueOf(modelPrice.output())))
                )
                .divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
