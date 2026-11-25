package dev.roadbook.api.trip;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TripService {

    private final TripRepository repository;

    public TripService(TripRepository repository) {
        this.repository = repository;
    }

    public List<TripResponse> findAll() {
        return repository.findAll().stream().map(TripResponse::from).toList();
    }

    public TripResponse findById(Long id) {
        return TripResponse.from(getOrThrow(id));
    }

    @Transactional
    public TripResponse create(TripRequest request) {
        Trip trip = new Trip(request.name(), request.destination(),
                request.startDate(), request.endDate());
        return TripResponse.from(repository.save(trip));
    }

    @Transactional
    public TripResponse update(Long id, TripRequest request) {
        Trip trip = getOrThrow(id);
        trip.update(request.name(), request.destination(),
                request.startDate(), request.endDate());
        return TripResponse.from(trip);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Trip getOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new TripNotFoundException(id));
    }
}