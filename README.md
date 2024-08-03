# Blog Application Backend

This is a backend application for a blog, built with Spring Boot and PostgreSQL. It provides APIs to manage users, posts, and comments.



## Features

- User management
- Post creation and management
- Comment creation and management
- JWT-based authentication and authorization

## Requirements

- Java 17 or later
- Maven
- PostgreSQL

## Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/kevin-mbon/QTchallenge-be.git
    cd QTchallenge-be
    cd blog
    ```

2. Install dependencies:

    ```bash
    mvn clean install
    ```

## Database Setup

1. Create a new PostgreSQL database:
2. create tables use script querry provided

4. Update `src/main/resources/application.properties` with your PostgreSQL database connection details:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/blogdb
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

## Running the Application

1. Start the Spring Boot application:

    ```bash
    mvn spring-boot:run
    ```

2. The application will be running at `http://localhost:8082/api/`.
3. Test APIs with postman documentation provided

