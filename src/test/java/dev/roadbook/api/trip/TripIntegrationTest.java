package dev.roadbook.api.trip;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.roadbook.api.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class TripIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    private static final String BODY = """
            {"name":"%s","destination":"Monde",
             "startDate":"2026-07-07","endDate":"2027-07-07"}""";

    @Test
    void fullCrudLifecycle() throws Exception {
        // Create
        String location = mockMvc.perform(post("/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.formatted("Tour du monde")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getHeader("Location");

        // Read
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tour du monde"));

        // Update
        mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY.formatted("Tour du monde v2")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tour du monde v2"));

        // List
        mockMvc.perform(get("/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("Tour du monde v2")));

        // Delete, puis 404
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }

    @Test
    void invalidRequest_returns400WithFieldErrors() throws Exception {
        mockMvc.perform(post("/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"","startDate":"2026-08-01","endDate":"2026-07-01"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
    }
}