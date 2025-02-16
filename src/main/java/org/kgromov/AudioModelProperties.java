package org.kgromov;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "audio")
public record AudioModelProperties(Map<String, AudioModelPrice> models)
        implements ModelProperties<AudioModelPrice> {

}
