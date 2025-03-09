package org.thakur.kahanidukan.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Document(collection = "stories")
public record Story(String id, String datetime, String title, String story, String moral, String author) {
    public Story(String id, String datetime, String title, String story, String moral, String author) {
        if (id == null) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
        if (title.isBlank() || title.equalsIgnoreCase("null")) {
            throw new ContentNotFoundException("Title is required");
        }
        if (story.isBlank() || story.equalsIgnoreCase("null")) {
            throw new ContentNotFoundException("Story is required");
        }
        if (moral.isBlank() || moral.equalsIgnoreCase("null")) {
            throw new ContentNotFoundException("Moral is required");
        }
        if (author.isBlank() || author.equalsIgnoreCase("null")) {
            throw new AuthorNotFoundException("Author is required");
        }
        if (datetime.isBlank() || datetime.equalsIgnoreCase("null")) {
            throw new ContentNotFoundException("Datetime is required");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime dateTime = ZonedDateTime.parse(datetime, Story.formatter);
        if (dateTime.isAfter(now)) {
            throw new ContentNotFoundException("Datetime cannot be in the future");
        }
        this.datetime = datetime;
        this.title = title;
        this.story = story;
        this.moral = moral;
        this.author = author;
    }

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
}
