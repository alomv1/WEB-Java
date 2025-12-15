package com.weblabs.webjava.security;

import com.weblabs.webjava.services.FeatureToggleService;
import com.weblabs.webjava.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasicSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private FeatureToggleService featureToggleService;

    @Test
    @DisplayName("Should return 401 Unauthorized when requesting without token")
    void shouldDenyAccess_WhenNoToken() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 200 OK when requesting with valid JWT token")
    void shouldAllowAccess_WhenJwtIsValid() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());


        mockMvc.perform(get("/api/products")
                        .with(jwt()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}