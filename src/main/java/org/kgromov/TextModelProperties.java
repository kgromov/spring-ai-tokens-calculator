package org.kgromov;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "text")
record TextModelProperties(Map<String, TextModelPrice> models)
        implements ModelProperties<TextModelPrice> {
}
