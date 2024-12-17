package org.kgromov;

import org.springframework.ai.chat.model.ChatResponse;

public final class ImagePriceCalculator implements PriceCalculator {
    private final ImageModelProperties properties;

    ImagePriceCalculator(ImageModelProperties properties) {
        this.properties = properties;
    }

    @Override
    public double calculate(ChatResponse response) {
       throw new UnsupportedOperationException("Required to examine image metadata");
    }
}
