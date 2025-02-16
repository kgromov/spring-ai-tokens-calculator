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
            var options = imagePrompt.getOptions();
            properties.getModelPrice(options.getModel())
                    .ifPresent(modelPrice -> {
                        var resolution = new Resolution(options.getWidth(), options.getHeight());
                        double price = BigDecimal.valueOf(resolution.isHigh() ? modelPrice.hd() : modelPrice.standard())
                                .multiply(BigDecimal.valueOf(response.getResults().size()))
                                .doubleValue();
                        logger.debug("Response cost = {} $", price);
                        metricsService.incrementTokensPrice(price);
                    });
            return response;
        } catch (Exception e) {
            logger.error("Cannot get image response", e);
            throw e;
        }
    }

    record Resolution(int width, int height) {

        boolean isStandard() {
            return (width >= 512 && height >= 512) && (width <= 1024 && height <= 1024);
        }

        boolean isHigh() {
            return width > 1024 && height > 1024;
        }

        boolean isLow() {
            return width < 512 && height < 512;
        }
    }
}
