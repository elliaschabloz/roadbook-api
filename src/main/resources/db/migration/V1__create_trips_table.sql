CREATE TABLE trips (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    destination VARCHAR(255),
    start_date  DATE         NOT NULL,
    end_date    DATE         NOT NULL,
    CONSTRAINT chk_trips_dates CHECK (end_date >= start_date)
);