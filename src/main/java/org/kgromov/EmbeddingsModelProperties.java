package org.kgromov;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "embedding")
record EmbeddingsModelProperties(Map<String, EmbeddingsModelPrice> models)
        implements ModelProperties<EmbeddingsModelPrice> {
}
