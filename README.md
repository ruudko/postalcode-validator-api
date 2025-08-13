# Postal Code Validator API

A Spring Boot application that manages country-specific postal code validation.

## Description

This service provides an API for validating postal codes for different countries. It is built using Java 21 and Spring Boot 3.

## Getting Started

To get this project set up and running locally, follow these steps.

### Prerequisites

Make sure you have the following software installed:

-   Java Development Kit (JDK) 21 or higher
-   Apache Maven

### Running the Application

1.  Clone the repository to your local machine.
2.  Navigate to the project's root directory.
3.  Compile and start the application using Maven:

    ```bash
    mvn spring-boot:run
    ```

The application will start on `http://localhost:8080` by default.

## API Documentation

Once the application is running, the API documentation is available via Swagger UI at the following address:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Technologies Used

-   **Java 21**
-   **Spring Boot 3.5.4**
    -   Spring Web
    -   Spring Data JPA
    -   Spring WebFlux
    -   Spring Validation
-   **SpringDoc OpenAPI** - For API documentation
-   **H2 Database** - In-memory database for development and testing
-   **Lombok** - To reduce boilerplate code
-   **Maven** - For dependency management and build automation
