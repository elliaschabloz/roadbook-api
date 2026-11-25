package dev.roadbook.api.trip;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    TripRepository repository;

    @InjectMocks
    TripService service;

    private final TripRequest request = new TripRequest(
            "Tour du monde", "Monde", LocalDate.of(2026, 7, 7), LocalDate.of(2027, 7, 7));

    @Test
    void findAll_returnsMappedResponses() {
        when(repository.findAll()).thenReturn(List.of(
                new Trip("A", "France", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5))));

        List<TripResponse> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("A");
    }

    @Test
    void findById_throwsWhenMissing() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(42L))
                .isInstanceOf(TripNotFoundException.class)
                .hasMessageContaining("42");
    }

    @Test
    void create_savesAndReturnsResponse() {
        when(repository.save(any(Trip.class))).thenAnswer(inv -> inv.getArgument(0));

        TripResponse response = service.create(request);

        assertThat(response.name()).isEqualTo("Tour du monde");
        verify(repository).save(any(Trip.class));
    }

    @Test
    void update_modifiesExistingTrip() {
        Trip existing = new Trip("Old", "X", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2));
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        TripResponse response = service.update(1L, request);

        assertThat(response.name()).isEqualTo("Tour du monde");
        assertThat(existing.getDestination()).isEqualTo("Monde");
    }

    @Test
    void delete_throwsWhenMissing_andDeletesNothing() {
        when(repository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(7L)).isInstanceOf(TripNotFoundException.class);
        verify(repository, never()).delete(any(Trip.class));
    }
}