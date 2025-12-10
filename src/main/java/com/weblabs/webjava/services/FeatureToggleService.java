package com.weblabs.webjava.services;

public interface FeatureToggleService {
    boolean isFeatureEnabled(String featureName);
}