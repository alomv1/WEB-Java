package com.weblabs.webjava.services.impl;

import com.weblabs.webjava.config.FeatureProperties;
import com.weblabs.webjava.services.FeatureToggleService;
import org.springframework.stereotype.Service;

@Service
public class FeatureToggleServiceImpl implements FeatureToggleService {

    private final FeatureProperties featureProperties;

    public FeatureToggleServiceImpl(FeatureProperties featureProperties) {
        this.featureProperties = featureProperties;
    }

    @Override
    public boolean isFeatureEnabled(String featureName) {
        return featureProperties.getToggles().getOrDefault(featureName, false);
    }
}