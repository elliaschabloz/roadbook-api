package dev.roadbook.api.trip;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String destination;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    protected Trip(){}

    public Trip(String name, String destination, LocalDate startDate, LocalDate endDate){
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(String name, String destination, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDestination() { return destination; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}