# KahaniDukan - Story Management API

A Spring Boot application for managing and searching stories with MongoDB.
<br> This project demonstrates CRUD operations, text search, and RESTful API design.

## Features

- Create, read, update, and delete stories
- Search stories with MongoDB text search
- Filter stories by author
- Get random stories
- Text analysis with word frequency and summarization
- API documentation with Swagger

## Tech Stack

- Java 21
- Spring Boot 3.4.3
- MongoDB
- Maven
- Apache OpenNLP

## Getting Started

### Prerequisites

- JDK 21 or higher
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

3. Download OpenNLP models and place them in `src/main/resources/models/`:
   - opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin (sentence detector) [link](https://www.apache.org/dyn/closer.cgi/opennlp/models/ud-models-1.2/opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin)
   - opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin (tokenizer) [link](https://www.apache.org/dyn/closer.cgi/opennlp/models/ud-models-1.2/opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin)

4. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. Access the API at `http://localhost:8080`
6. Access Swagger documentation at `http://localhost:8080/docs.html`

## Database Initialization with Sample Data

You can initialize your MongoDB database with stories from a JSON file using the MongoDB command line tools:

1. Create a JSON file containing an array of story objects (e.g., `sample-stories.json`)
2. Use the MongoDB import tool to load the data:

```bash
mongoimport --uri="mongodb://localhost:27017/mydatabase" --collection=stories --file=sample-stories.json --jsonArray
```

## API Endpoints

| Method | Endpoint                                              | Description                                          |
|--------|-------------------------------------------------------|------------------------------------------------------|
| GET    | /stories?sortingOrder={order}                         | Get all stories with optional sorting by date        |
|        |                                                       | (order: 'asc', 'desc', or 'none', default is 'none') |
| GET    | /stories/random                                       | Get a random story                                   |
| GET    | /stories/authors                                      | Get list of all authors                              |
| GET    | /stories/author?author={name}                         | Get stories by author                                |
| GET    | /stories/story?id={id}                                | Get story by ID                                      |
| POST   | /stories/story                                        | Add a new story                                      |
| PUT    | /stories/story                                        | Update an existing story                             |
| DELETE | /stories/story?id={id}                                | Delete a story by ID                                 |
| DELETE | /stories/author?author={name}                         | Delete all stories by an author                      |
| GET    | /stories/search?query={text}                          | Search for stories                                   |
| GET    | /text-processor/word-frequency?storyId={id}&limit={n} | Get word frequency for a story                       |
| GET    | /text-processor/summarize?storyId={id}                | Summarize a story using TextRank                     |
| GET    | /text-processor/top-10-longest-stories                | Get the 10 longest stories                           |

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

## Text Processing Features

### Word Frequency Analysis
Returns the most frequent words in a story, excluding common stop words.

### Text Summarization
Uses the TextRank algorithm to extract the most important sentences from a story, creating an automatic summary.

## Future Improvements

- Add user authentication
- Implement pagination for large datasets
- Add more search options
- Create a frontend application
- Enhance text analysis with sentiment detection

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- Spring Boot Documentation
- MongoDB Documentation
- Apache OpenNLP