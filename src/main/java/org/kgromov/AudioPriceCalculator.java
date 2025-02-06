package org.kgromov;

import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;

public final class AudioPriceCalculator implements PriceCalculator<AudioTranscriptionResponse> {
    private final AudioModelProperties properties;

    AudioPriceCalculator(AudioModelProperties properties) {
        this.properties = properties;
    }

    @Override
    public double calculate(AudioTranscriptionResponse response) {
        throw new UnsupportedOperationException("Required to examine audio metadata");
    }

    private double calculate(AudioModelPrice modelPrice, TokenUsage usage) {
        throw new UnsupportedOperationException("Required to examine audio metadata");
    }
}
