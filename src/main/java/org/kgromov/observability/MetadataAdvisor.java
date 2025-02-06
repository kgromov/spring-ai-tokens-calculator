package org.kgromov.observability;

import org.kgromov.PriceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.DoubleAdder;

import static org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor.DEFAULT_REQUEST_TO_STRING;
import static org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor.DEFAULT_RESPONSE_TO_STRING;

public class MetadataAdvisor implements CallAroundAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(MetadataAdvisor.class);

    private final SpringAiMetricsService metricsService;
    private final PriceCalculator priceCalculator;

    private final Map<String, DoubleAdder> tokensPrice = new ConcurrentHashMap<>();

    MetadataAdvisor(SpringAiMetricsService metricsService, PriceCalculator priceCalculator) {
        this.metricsService = metricsService;
        this.priceCalculator = priceCalculator;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        logger.debug("request: {}", DEFAULT_REQUEST_TO_STRING.apply(advisedRequest));
        var now = Instant.now();
        var advisedResponse = chain.nextAroundCall(advisedRequest);
        var chatResponse = advisedResponse.response();
        logger.debug("response: {}", DEFAULT_RESPONSE_TO_STRING.apply(chatResponse));
        var metadata = chatResponse.getMetadata();
        double price = priceCalculator.calculate(chatResponse);
        logger.debug("Response cost = {} $", price);
        metricsService.recordResponseTime(now);
        metricsService.incrementRequestTokens(metadata.getUsage());
        metricsService.incrementTokensPrice(price);
        tokensPrice.computeIfAbsent(metadata.getModel(), _ -> new DoubleAdder()).add(price);
        return advisedResponse;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
