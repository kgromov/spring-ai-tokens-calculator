package org.kgromov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/*
 * TODO: implementation:
 * it seems to be missed advisor for image request
 * so as an attempt to implement - try out aspect around call to ImageModel#call(ImagePrompt request);
 * from ImagePrompt it' possible to fetch resolution (alternatively can be done from Image response - ImageResponse#getResult().getOutput())
 * result images can be just fetched from ImageResponse#getResults().size()
 */
public final class ImagePriceCalculator implements PriceCalculator<ImageResponse> {
    private static final Logger logger = LoggerFactory.getLogger(ImagePriceCalculator.class);
    private final ImageModelProperties properties;

    ImagePriceCalculator(ImageModelProperties properties) {
        this.properties = properties;
    }

    @Override
    public double calculate(ImageResponse response) {
        List<ImageGeneration> results = response.getResults();
        var images = results.stream()
                .map(ImageGeneration::getOutput)
                .toList();
        var resolutions = images.stream()
                .flatMap(image -> this.base64ToImage(image.getB64Json()).stream())
                .toList();
        throw new UnsupportedOperationException("Examine how to retrieve model");
    }

    private Optional<Resolution> base64ToImage(String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
            return Optional.of(new Resolution(img.getWidth(), img.getHeight()));
        } catch (IOException e) {
            logger.error("Cannot get image resolution", e);
            return Optional.empty();
        }
    }

    record Resolution(int width, int height) {
        boolean isStandard() {
            return (width >= 512 && height >= 512) && (width <= 1024 && height <= 1024);
        }

        boolean isHigh() {
            return width > 1024 && height > 1024;
        }

        boolean isLow() {
            return width < 512 && height < 512;
        }
    }
}
