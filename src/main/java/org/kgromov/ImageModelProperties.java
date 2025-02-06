package org.kgromov;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "image")
public record ImageModelProperties(Map<String, ImageModelPrice> models)
        implements ModelProperties<ImageModelPrice> {
}
