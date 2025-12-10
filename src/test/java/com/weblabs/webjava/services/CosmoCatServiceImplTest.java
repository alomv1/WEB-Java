package com.weblabs.webjava.services;

import com.weblabs.webjava.exception.FeatureNotAvailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CosmoCatServiceTest {

    @Autowired
    private CosmoCatService cosmoCatService;

    @Test
    @DisplayName("Should return list of cats when 'cosmoCats' feature is ENABLED")
    void testGetCosmoCats_WhenEnabled() {

        List<String> result = cosmoCatService.getCosmoCats();

        assertNotNull(result, "List should not be null");
        assertFalse(result.isEmpty(), "List should not be empty");
        assertTrue(result.contains("Murzik The Astronaut"));

        System.out.println("Test 1 Passed: Cosmo Cats received!");
    }

    @Test
    @DisplayName("Should throw exception when 'kittyProducts' feature is DISABLED")
    void testGetKittyProducts_WhenDisabled() {
        Exception exception = assertThrows(FeatureNotAvailableException.class, () -> {
            cosmoCatService.getKittyProducts();
        });

        String expectedMessage = "kittyProducts";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Exception message should contain feature name");

        System.out.println("Test 2 Passed: Access correctly denied!");
    }
}