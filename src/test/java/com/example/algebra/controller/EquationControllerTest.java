package com.example.algebra.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.algebra.exception.GlobalExceptionHandler;
import com.example.algebra.repository.EquationRepository;
import com.example.algebra.service.EquationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class EquationControllerTest {

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    EquationRepository repository = new EquationRepository();
    EquationService service = new EquationService(repository);

    EquationController controller = new EquationController(service);

    mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  void shouldStoreEquation() throws Exception {
    mockMvc
        .perform(
            post("/api/equations/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {
                                  "equation": "3x + 2y - z"
                                }
                                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value("Equation stored successfully"))
        .andExpect(jsonPath("$.equationId").exists());
  }

  @Test
  void shouldRejectEmptyEquation() throws Exception {
    mockMvc
        .perform(
            post("/api/equations/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {
                                  "equation": ""
                                }
                                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Equation cannot be empty"));
  }

  @Test
  void shouldRetrieveAllEquations() throws Exception {
    mockMvc.perform(
        post("/api/equations/store")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                        {
                          "equation": "2x + 3"
                        }
                        """));

    mockMvc
        .perform(get("/api/equations"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].equationId").exists())
        .andExpect(jsonPath("$[0].equation").exists());
  }

  @Test
  void shouldEvaluateEquation() throws Exception {
    String response =
        mockMvc
            .perform(
                post("/api/equations/store")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                                {
                                  "equation": "3x + 2y - z"
                                }
                                """))
            .andReturn()
            .getResponse()
            .getContentAsString();

    String equationId =
        com.fasterxml.jackson.databind.json.JsonMapper.builder()
            .build()
            .readTree(response)
            .get("equationId")
            .asText();

    mockMvc
        .perform(
            post("/api/equations/" + equationId + "/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {
                                  "variables": {
                                    "x": 2,
                                    "y": 3,
                                    "z": 1
                                  }
                                }
                                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value(11.0));
  }

  @Test
  void shouldReturnNotFoundForUnknownEquation() throws Exception {
    mockMvc
        .perform(
            post("/api/equations/unknown-id/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {
                                  "variables": {
                                    "x": 2
                                  }
                                }
                                """))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Equation not found: unknown-id"));
  }

  @Test
  void shouldRejectInvalidEquation() throws Exception {
    mockMvc
        .perform(
            post("/api/equations/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                                {
                                  "equation": "3 +"
                                }
                                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").exists());
  }
}
