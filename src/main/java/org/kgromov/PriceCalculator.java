package org.kgromov;

import org.springframework.ai.chat.model.ChatResponse;

public sealed interface PriceCalculator permits AudioPriceCalculator, EmbeddingsPriceCalculator, ImagePriceCalculator, TextModelPriceCalculator {

    double calculate(ChatResponse response);
}
