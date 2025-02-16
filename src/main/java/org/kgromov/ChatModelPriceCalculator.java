package org.kgromov;

import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ChatModelPriceCalculator implements PriceCalculator<ChatResponse> {
    private final ChatModelProperties properties;

    ChatModelPriceCalculator(ChatModelProperties properties) {
        this.properties = properties;
    }

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

    private double calculate(TextModelPrice price, TokenUsage usage) {
        return BigDecimal.valueOf(usage.prompt()).multiply(BigDecimal.valueOf(price.input()))
                .add(BigDecimal.valueOf(usage.generated()).multiply(BigDecimal.valueOf(price.output())))
                .divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
