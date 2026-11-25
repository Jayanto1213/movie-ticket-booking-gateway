# Ticket Booking System

A Java 21 + Spring Boot REST API for a movie ticket booking platform, inspired by BookMyShow.

## Features
- Movie listing, creation, update and deletion
- Theatre management and city filtering
- Show scheduling and filtering by movie or city
- Seat booking with duplicate-seat validation
- Booking lookup by ID or customer email
- Booking cancellation
- Transactional booking service
- H2 database for zero-setup local execution
- Sample movie, theatre and show data seeded at startup

> This is a runnable portfolio MVP. It does not yet include production authentication, Redis seat locks, payment gateway integration, Kafka, QR tickets, or race-safe seat inventory. Those should be added before presenting it as production-ready.

## Requirements
- JDK 21
- Maven 3.9+

## Run
```bash
mvn clean spring-boot:run
```
API base URL: `http://localhost:8080/api`

H2 console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:ticketdb`
- Username: `sa`
- Password: leave blank

## Sample data
The app seeds:
- Movie ID: `1` — The Last Journey
- Theatre ID: `1` — PVR Sakchi, Jamshedpur
- Show ID: `1` — tomorrow at 18:30 (server local time)

## API endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/movies` | List movies |
| GET | `/api/movies/{id}` | Get movie |
| POST | `/api/movies` | Create movie |
| PUT | `/api/movies/{id}` | Update movie |
| DELETE | `/api/movies/{id}` | Delete movie |
| GET | `/api/theatres` | List theatres |
| GET | `/api/theatres?city=Jamshedpur` | Filter theatres by city |
| POST | `/api/theatres` | Create theatre |
| GET | `/api/shows` | List shows |
| GET | `/api/shows?movieId=1` | Filter shows by movie |
| GET | `/api/shows?city=Jamshedpur` | Filter shows by city |
| POST | `/api/shows` | Create show |
| POST | `/api/bookings` | Book seats |
| GET | `/api/bookings/{id}` | Get booking |
| GET | `/api/bookings?email=person@example.com` | Find customer bookings |
| PATCH | `/api/bookings/{id}/cancel` | Cancel booking |

## Example requests

### Create a movie
```json
{
  "title": "Interstellar",
  "genre": "Sci-Fi",
  "durationMinutes": 169,
  "language": "English",
  "description": "A science fiction film",
  "posterUrl": ""
}
```

### Create a show
```json
{
  "movieId": 1,
  "theatreId": 1,
  "startTime": "2026-12-01T18:30:00",
  "ticketPrice": 250,
  "totalSeats": 60
}
```

### Book seats
```json
{
  "showId": 1,
  "customerName": "Jayanto Mahato",
  "customerEmail": "jayanto@example.com",
  "seatNumbers": ["A1", "A2"]
}
```

## PostgreSQL configuration
To switch from H2 to PostgreSQL, replace the datasource properties in `application.properties` with:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ticketbooking
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
```
Create the `ticketbooking` database first.

## Next improvements
1. JWT authentication and role-based authorization
2. Dedicated seat inventory table with unique `(show_id, seat_number)` constraint
3. Redis-based temporary seat holds with expiry
4. Payment gateway integration with webhook verification
5. Kafka-based notifications
6. QR-code ticket generation
7. Integration tests and Docker Compose
