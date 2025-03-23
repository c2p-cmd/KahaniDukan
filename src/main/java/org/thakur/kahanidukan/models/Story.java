package org.thakur.kahanidukan.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.thakur.kahanidukan.models.exceptions.AuthorNotFoundException;
import org.thakur.kahanidukan.models.exceptions.ContentNotFoundException;
import org.thakur.kahanidukan.models.exceptions.DateTimeInFutureException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Document(collection = "stories")
public record Story(
    @Id String id,
    @CreatedDate
    String datetime,
    String title,
    @TextIndexed String story,
    String moral,
    String author
) {
    static public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public Story(String id, String datetime, String title, String story, String moral, String author) {
        if (id == null) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
        if (title.isBlank() || title.equalsIgnoreCase("null")) {
            throw new ContentNotFoundException();
        }
        if (story.isBlank() || story.equalsIgnoreCase("null")) {
            throw new ContentNotFoundException();
        }
        if (moral.isBlank() || moral.equalsIgnoreCase("null")) {
            throw new ContentNotFoundException();
        }
        if (author.isBlank() || author.equalsIgnoreCase("null")) {
            throw new AuthorNotFoundException("Author is required");
        }
        if (datetime == null || datetime.isBlank() || datetime.equalsIgnoreCase("null")) {
            this.datetime = ZonedDateTime.now().format(formatter);
        } else {
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime dateTime = ZonedDateTime.parse(datetime, Story.formatter);
            if (dateTime.isAfter(now)) {
                throw new DateTimeInFutureException();
            }
            this.datetime = datetime;
        }
        this.title = title;
        this.story = story;
        this.moral = moral;
        this.author = author;
    }
}
