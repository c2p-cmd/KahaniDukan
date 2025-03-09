# KahaniDukan - Story Management API

A Spring Boot application for managing and searching stories with MongoDB.
<br> This project demonstrates CRUD operations, text search, and RESTful API design.

## Features

- Create, read, update, and delete stories
- Search stories with MongoDB text search
- Filter stories by author
- Get random stories
- API documentation with Swagger

## Tech Stack

- Java 17+
- Spring Boot 3.4.3
- MongoDB
- Maven

## Getting Started

### Prerequisites

- JDK 17 or higher
- MongoDB
- Maven

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/c2p-cmd/kahanidukan.git
   cd kahanidukan
   ```

2. Configure MongoDB connection in `application.properties`:
   ```properties
   spring.data.mongodb.uri=mongodb://localhost:27017/mydatabase
   ```

3. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Access the API at `http://localhost:8080`
5. Access Swagger documentation at `http://localhost:8080/docs.html`

## API Endpoints

| Method | Endpoint                      | Description                     |
|--------|-------------------------------|---------------------------------|
| GET    | /stories                      | Get all stories                 |
| GET    | /stories/random               | Get a random story              |
| GET    | /stories/authors              | Get list of all authors         |
| GET    | /stories/author?author={name} | Get stories by author           |
| GET    | /stories/story?id={id}        | Get story by ID                 |
| POST   | /stories/story                | Add a new story                 |
| PUT    | /stories/story                | Update an existing story        |
| DELETE | /stories/story?id={id}        | Delete a story by ID            |
| DELETE | /stories/author?author={name} | Delete all stories by an author |
| GET    | /stories/search?query={text}  | Search for stories              |

## Story Model

```json
{
  "id": "67cde8efa82cd009dc14af03",
  "datetime": "2024-03-02T07:43:30.000Z",
  "title": "The Fox and the Hedgehog",
  "story": "A Fox, swimming across a river, was barely able to reach the bank...",
  "moral": "Better to bear a lesser evil than to risk a greater in removing it.",
  "author": "Jean-Pierre Dubois"
}
```

## Future Improvements

- Add user authentication
- Implement pagination for large datasets
- Add more search options
- Create a frontend application

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- Spring Boot Documentation
- MongoDB Documentation
- University instructors and classmates