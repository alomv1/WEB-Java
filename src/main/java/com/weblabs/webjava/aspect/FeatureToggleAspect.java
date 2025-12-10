package com.weblabs.webjava.aspect;

import com.weblabs.webjava.annotation.CheckFeature;
import com.weblabs.webjava.exception.FeatureNotAvailableException;
import com.weblabs.webjava.services.FeatureToggleService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    public FeatureToggleAspect(FeatureToggleService featureToggleService) {
        this.featureToggleService = featureToggleService;
    }

    @Around("@annotation(checkFeature)")
    public Object checkFeatureToggle(ProceedingJoinPoint joinPoint, CheckFeature checkFeature) throws Throwable {
        String featureName = checkFeature.value();

        if (featureToggleService.isFeatureEnabled(featureName)) {
            return joinPoint.proceed();
        } else {
            throw new FeatureNotAvailableException("Feature '" + featureName + "' is currently disabled.");
        }
    }
}