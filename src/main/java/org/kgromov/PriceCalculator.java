package org.kgromov;

import org.springframework.ai.chat.model.ChatResponse;

public sealed interface PriceCalculator permits AudioPriceCalculator, EmbeddingsPriceCalculator, ImagePriceCalculator, TextModelPriceCalculator {

    // TODO: change to ModelResponse or generify ModelResponse as interface generic type
    double calculate(ChatResponse response);
}
