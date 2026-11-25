package dev.roadbook.api.trip;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.roadbook.api.common.GlobalExceptionHandler;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TripController.class)
@Import(GlobalExceptionHandler.class)
class TripControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TripService service;

    private static final String VALID_JSON = """
            {"name":"Tour du monde","destination":"Monde",
             "startDate":"2026-07-07","endDate":"2027-07-07"}""";

    private final TripResponse response = new TripResponse(
            1L, "Tour du monde", "Monde", LocalDate.of(2026, 7, 7), LocalDate.of(2027, 7, 7));

    @Test
    void findAll_returns200() throws Exception {
        when(service.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tour du monde"));
    }

    @Test
    void findById_returns404WhenMissing() throws Exception {
        when(service.findById(99L)).thenThrow(new TripNotFoundException(99L));

        mockMvc.perform(get("/trips/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.title").value("Trip not found"));
    }

    @Test
    void create_returns201WithLocation() throws Exception {
        when(service.create(any(TripRequest.class))).thenReturn(response);

        mockMvc.perform(post("/trips").contentType(MediaType.APPLICATION_JSON).content(VALID_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/trips/1")))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_returns400WithFieldErrors() throws Exception {
        String invalid = """
                {"name":"","startDate":"2026-08-01","endDate":"2026-07-01"}""";

        mockMvc.perform(post("/trips").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.endDateValid").exists());
    }

    @Test
    void update_returns200() throws Exception {
        when(service.update(eq(1L), any(TripRequest.class))).thenReturn(response);

        mockMvc.perform(put("/trips/1").contentType(MediaType.APPLICATION_JSON).content(VALID_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/trips/1")).andExpect(status().isNoContent());
    }
}