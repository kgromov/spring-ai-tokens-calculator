package org.kgromov;

import org.springframework.ai.model.ModelResponse;

public sealed interface PriceCalculator<T extends ModelResponse<?>> permits
        ChatModelPriceCalculator,
        EmbeddingsPriceCalculator,
        ImagePriceCalculator,
        AudioPriceCalculator {
    double calculate(T response);
}
