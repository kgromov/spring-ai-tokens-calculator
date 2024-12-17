package org.kgromov;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@ConfigurationProperties(prefix = "text")
record TextModelProperties(Map<String, TextModelPrice> models) {

    public Optional<TextModelPrice> getModelPrice(String model) {
        return  ofNullable(models.get(model))
                .or(() -> this.getCloserModel(model));
    }

    private Optional<TextModelPrice> getCloserModel(String model) {
        return models.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith(model) || model.startsWith(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();
    }
}
