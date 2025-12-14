package com.weblabs.webjava.services;

import com.weblabs.webjava.config.FeatureProperties;
import com.weblabs.webjava.services.impl.FeatureToggleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeatureToggleServiceImplTest {

    @Mock
    private FeatureProperties featureProperties;

    @InjectMocks
    private FeatureToggleServiceImpl featureToggleService;

    @Test
    void isFeatureEnabled_ShouldReturnTrue_WhenEnabled() {

        when(featureProperties.getToggles()).thenReturn(Map.of("awesome-feature", true));

        boolean result = featureToggleService.isFeatureEnabled("awesome-feature");

        assertTrue(result, "Feature should be enabled");
    }

    @Test
    void isFeatureEnabled_ShouldReturnFalse_WhenDisabled() {
        when(featureProperties.getToggles()).thenReturn(Map.of("slow-feature", false));

        boolean result = featureToggleService.isFeatureEnabled("slow-feature");

        assertFalse(result, "Feature should be disabled");
    }

    @Test
    void isFeatureEnabled_ShouldReturnFalse_WhenMissing() {
        when(featureProperties.getToggles()).thenReturn(Map.of());

        boolean result = featureToggleService.isFeatureEnabled("unknown-feature");

        assertFalse(result, "Missing feature should be disabled by default");
    }
}