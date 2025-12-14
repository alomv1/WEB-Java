package com.weblabs.webjava.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GlobalExceptionHandlerTest.TestController.class)
@Import(GlobalExcHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Configuration
    @RestController
    static class TestController {

        @GetMapping("/test/not-found")
        public void throwNotFound() {
            throw new NoSuchElementException("Resource not found");
        }

        @GetMapping("/test/persistence")
        public void throwPersistence() {
            throw new PersistenceException("Database error", new RuntimeException("SQL Error occurred"));
        }

        @GetMapping("/test/feature-disabled")
        public void throwFeature() {
            throw new FeatureNotAvailableException("Feature is off");
        }

        @GetMapping("/test/generic")
        public void throwGeneric() {
            throw new RuntimeException("Unexpected error");
        }
    }

    @Test
    void shouldHandleNoSuchElementException_404() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldHandlePersistenceException_500() throws Exception {
        mockMvc.perform(get("/test/persistence"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldHandleFeatureNotAvailable_503_or_400() throws Exception {
        mockMvc.perform(get("/test/feature-disabled"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldHandleGenericException_500() throws Exception {
        mockMvc.perform(get("/test/generic"))
                .andExpect(status().isInternalServerError());
    }
}