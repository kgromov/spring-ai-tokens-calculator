package org.kgromov;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public interface ModelProperties<T> {

    Map<String, T> models();

    default Optional<T> getModelPrice(String model) {
        return  ofNullable(this.models().get(model))
                .or(() -> this.getClosestModel(model));
    }

    private Optional<T> getClosestModel(String model) {
        return this.models().entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith(model) || model.startsWith(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();
    }
}
