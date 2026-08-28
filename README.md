# KickAbout - Distributed Booking System

KickAbout is a distributed booking system for grassroots football, built as an MSc Computer Science dissertation project. It allows players to browse, host and join organised football games.

The system's primary technical focus is concurrency-safe booking, using a reservation hold pattern combined with optimistic locking to ensure two players can never book the same slot at the same time. Full design rationale and testing evidence are in the accompanying project report.

## Features

- **Concurrency-safe booking** - a reservation hold and optimistic locking ensure exactly one booking succeeds when multiple players compete for the same slot
- **Games** - browse and filter games, book available slots, and host or join organised games with eligibility rules enforced per game
- **Credits** - A credits-based system stands in for a real payment gateway. Users automatically receive 100 credits on registration to use for paid online games
- **Accounts & Profiles** - registration, login and profile management
- **Admin Management** - manage pitches, games and users

## Tech Stack

- Java 21
- Spring Boot 4.0.6
- PostgreSQL 17
- Thymeleaf
- Spring Security
- Spring Data JPA / Hibernate
- Lombok
- JUnit 5 / Mockito
- Maven

## Prerequisites

- Java 21+
- Maven
- PostgreSQL

## Setup

1. Create a PostgreSQL database e.g. `distributed_booking_system`.
2. Copy the example config to create your local config file:

   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

   Edit `application.properties` and set your database name, username, and password.

3. Run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

4. The app will be available at `http://localhost:8080`.

`spring.jpa.hibernate.ddl-auto=update` is set in the application.properties file, so the schema is created/updated automatically on startup.

### Sample data
`sample-data.sql` can be used to create sample data to explore the app without manually creating pitches, users and bookings. Also includes locations, pitches, user accounts and profiles, starting credits, and a range of games in different states (open, full, cancelled, completed). 
All sample accounts log in with the password `password`.

A few games are worth noting when browsing:

- A game with 9 of its 10 slots already booked (1 available slot for 2 users to compete for) to test the reservation hold behaviour under contention
- A women-only game to test the gender validation matrix
- A paid-online game with a 24-hour refund policy to test the credit refunds when withdrawing from a game.

## Running Tests

```bash
./mvnw test
```

## Project Structure

```
src/main/java/com/dpapie01/distributed_booking_system/
├── config/       # Security and app configuration
├── controller/   # MVC controllers
├── dto/          # Data transfer objects
├── entity/       # JPA entities (User, Booking, Game, GameSlot, Pitch, Location, Credit, Profile)
├── enums/        # Enums
├── mapper/       # Entity/DTO mappers
├── repository/   # Spring Data JPA repositories
├── scheduler/    # Scheduled jobs
└── service/      # Business logic
```

Thymeleaf templates live under `src/main/resources/templates`.

## Configuration Notes

- `application.properties` is gitignored — use `application.properties.example` as a template.
- `application-prod.properties` holds production (Railway) overrides.
