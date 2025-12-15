package com.weblabs.webjava.security;

import com.weblabs.webjava.services.FeatureToggleService;
import com.weblabs.webjava.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiKeySecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${app.security.api-key}")
    private String validApiKey;

    @MockitoBean private JwtDecoder jwtDecoder;
    @MockitoBean private ProductService productService;
    @MockitoBean private FeatureToggleService featureToggleService;

    @Test
    @DisplayName("Should allow access with valid API Key header")
    void shouldAllow_WhenApiKeyIsValid() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products")
                        .header("X-API-KEY", validApiKey))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should deny access with invalid API Key")
    void shouldDeny_WhenApiKeyIsInvalid() throws Exception {
        mockMvc.perform(get("/api/products")
                        .header("X-API-KEY", "wrong-key-meow"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should deny access with missing API Key (and no JWT)")
    void shouldDeny_WhenNoAuth() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized());
    }
}