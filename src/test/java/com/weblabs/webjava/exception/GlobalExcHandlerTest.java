package com.weblabs.webjava.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(GlobalExcHandlerTest.TestController.class)
@Import(GlobalExcHandler.class)
class GlobalExcHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Configuration
    @RestController
    @Validated
    static class TestController {

        // Test DTO for validation
        static class TestDto {
            @NotBlank(message = "cannot be blank")
            @Size(min = 3, max = 10, message = "must be between 3 and 10 characters")
            private String name;

            @NotBlank(message = "is required")
            private String email;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }

        @GetMapping("/test/not-found")
        public void throwNotFound() {
            throw new NoSuchElementException("Item not found");
        }

        @GetMapping("/test/generic")
        public void throwGeneric() {
            throw new RuntimeException("Generic error");
        }

        @PostMapping("/test/validate")
        public String validateDto(@Valid @RequestBody TestDto dto) {
            return "Valid";
        }
    }

    @Test
    void shouldHandleNoSuchElementException_Returns404() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Item not found"))
                .andExpect(jsonPath("$.path").exists());
    }

    @Test
    void shouldHandleGenericException_Returns500() throws Exception {
        mockMvc.perform(get("/test/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Generic error"))
                .andExpect(jsonPath("$.path").exists());
    }

    @Test
    void shouldHandleValidationException_WithBlankName_Returns400() throws Exception {
        String requestBody = """
                {
                    "name": "",
                    "email": "test@example.com"
                }
                """;

        mockMvc.perform(post("/test/validate")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Field 'name' cannot be blank")))
                .andExpect(jsonPath("$.path").exists());
    }

    @Test
    void shouldHandleValidationException_WithInvalidNameLength_Returns400() throws Exception {
        String requestBody = """
                {
                    "name": "ab",
                    "email": "test@example.com"
                }
                """;

        mockMvc.perform(post("/test/validate")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(containsString("Field 'name' must be between 3 and 10 characters")))
                .andExpect(jsonPath("$.path").exists());
    }

    @Test
    void shouldHandleValidationException_WithMultipleErrors_Returns400() throws Exception {
        String requestBody = """
                {
                    "name": "",
                    "email": ""
                }
                """;

        mockMvc.perform(post("/test/validate")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").exists());
    }

    @Test
    void shouldHandleValidationException_WithMissingEmail_Returns400() throws Exception {
        String requestBody = """
                {
                    "name": "validname",
                    "email": ""
                }
                """;

        mockMvc.perform(post("/test/validate")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(containsString("Field 'email' is required")))
                .andExpect(jsonPath("$.path").exists());
    }

    @Test
    void shouldPassValidation_WithValidDto_Returns200() throws Exception {
        String requestBody = """
                {
                    "name": "validname",
                    "email": "test@example.com"
                }
                """;

        mockMvc.perform(post("/test/validate")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());
    }
}