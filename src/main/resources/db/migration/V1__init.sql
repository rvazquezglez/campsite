CREATE TABLE reservations (
    ubi BIGINT GENERATED BY DEFAULT AS IDENTITY,
    arrival_date DATE,
    departure_date DATE,
    email VARCHAR(50),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    PRIMARY KEY (ubi)
);