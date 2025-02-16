# Spring AI Pricing Calculator

A Spring Boot library for calculating usage costs across different AI model providers (OpenAI, Anthropic, Gemini) with built-in observability features. This library helps track and monitor API costs for text completions, embeddings, image generation, and audio processing.

## Overview

The Spring AI Pricing Calculator provides:

- Cost calculation for AI model usage
- Support for multiple AI providers and model types
- Built-in metrics collection using Micrometer
- Automatic cost tracking and logging

## Requirements

- Java 21 or higher
- Spring Boot 3.3.x or higher
- Spring AI
- Spring Boot Actuator (to include metrics)

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.kgromov</groupId>
    <artifactId>spring-ai-tokens-calculator</artifactId>
    <version>${spring-ai-tokens-calculator.version}</version>
</dependency>
```

### Enable Price Calculation

Add the `@EnableModelsPriceConfig` annotation to your configuration class

### Calculate text generative AI cost based on tokens usage

Implemented with custom `Advisor` - `ChatModelPriceAdvisor` which calculates cost based on chat request/response and delegates to next chain call.  
The `ChatModelPriceCalculator` is also available as a bean - so can be injected in other comonents.

```java
@Service
public class AICostTrackingService {
    private final ChatModelPriceCalculator priceCalculator;
    
    public double trackChatCosts(ChatResponse response) {
        double cost = priceCalculator.calculate(response);
        log.info("Chat completion cost: ${}", cost);
        return cost;
    }
}
```

## Metrics and Observability

The library automatically collects metrics through Micrometer:

- `spring.ai.request_tokens`: Total tokens used
- `spring.ai.response_time`: Response time in milliseconds
- `spring.ai.tokens_price`: Cumulative cost of API usage

Access these metrics through actuator

