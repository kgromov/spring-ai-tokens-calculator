package org.kgromov;

import org.springframework.ai.chat.model.ChatResponse;

/*
 * TODO: implementation:
 * it seems to be missed advisor for image request
 * so as an attempt to implement - try out aspect around call to ImageModel#call(ImagePrompt request);
 * from ImagePrompt it' possible to fetch resolution (alternatively can be done from Image response - ImageResponse#getResult().getOutput())
 * result images can be just fetched from ImageResponse#getResults().size()
 */
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
