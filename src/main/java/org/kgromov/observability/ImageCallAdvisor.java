package org.kgromov.observability;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.kgromov.ImageModelProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Aspect
public class ImageCallAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(ImageCallAdvisor.class);
    private final SpringAiMetricsService metricsService;
    private final ImageModelProperties properties;

    ImageCallAdvisor(SpringAiMetricsService metricsService, ImageModelProperties properties) {
        this.metricsService = metricsService;
        this.properties = properties;
    }

    @Pointcut("execution(* org.springframework.ai.image.ImageModel.call(..))")
    public void imageCall() {
    }

    @Around("imageCall()")
    public Object aroundImageCall(ProceedingJoinPoint joinPoint) throws Throwable {
        var now = Instant.now();
        ImagePrompt imagePrompt = (ImagePrompt) joinPoint.getArgs()[0];
        logger.debug("request: {}", imagePrompt);
        try {
            ImageResponse response = (ImageResponse) joinPoint.proceed();
            metricsService.recordResponseTime(now);
            properties.getModelPrice(imagePrompt.getOptions().getModel())
                    .ifPresent(modelPrice -> {
                        double price = BigDecimal.valueOf(modelPrice.standard())
                                .multiply(BigDecimal.valueOf(response.getResults().size()))
                                .divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP)
                                .doubleValue();
                        metricsService.incrementTokensPrice(price);
                    });
            return response;
        } catch (Exception e) {
            logger.error("Cannot get image response", e);
            throw e;
        }
    }
}
