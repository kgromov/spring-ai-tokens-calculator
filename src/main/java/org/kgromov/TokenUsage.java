package org.kgromov;

public record TokenUsage(long prompt, long generated) {
    public long total() {
        return prompt + generated;
    }
}
