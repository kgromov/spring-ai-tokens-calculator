package org.kgromov.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.ai.chat.metadata.Usage;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

class SpringAiMetricsService {

    private final Counter requestTokens;
    private final Counter tokensPrice;
    private final Timer responseTimer;

    SpringAiMetricsService(MeterRegistry registry) {
        requestTokens = Counter.builder("spring.ai.request_tokens")
                .description("Number of request tokens used")
                .tag("spring.ai", "request_tokens")
                .register(registry);
        responseTimer = Timer.builder("spring.ai.response_time")
                .tag("spring.ai", "response_time")
                .description("Cumulative response time in milliseconds")
                .register(registry);
        tokensPrice = Counter.builder("spring.ai.tokens_price")
                .description("Number of request tokens used")
                .tag("spring.ai", "request_tokens")
                .register(registry);
    }

    void incrementRequestTokens(Usage usage) {
        requestTokens.increment(usage.getTotalTokens());
    }

    void incrementTokensPrice(double price) {
        tokensPrice.increment(price);
    }

    void recordResponseTime(Instant startTime) {
        responseTimer.record(Duration.between(startTime, Instant.now()).toMillis(), TimeUnit.MILLISECONDS);
    }
}
