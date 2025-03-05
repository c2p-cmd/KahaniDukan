package org.thakur.kahanidukan.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Document(collection = "stories")
public class Story {
    @Id
    private final String _id;
    private final String title, author, story, moral;
    private final String datetime;

    public Story(String _id, String title, String author, String story, String moral, String datetime) {
        this._id = _id;
        this.title = title;
        this.author = author;
        this.story = story;
        this.moral = moral;
        this.datetime = datetime;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getStory() {
        return story;
    }

    public String getMoral() {
        return moral;
    }

    public LocalDateTime getDuration() {
        return ZonedDateTime.parse(datetime).toLocalDateTime();
    }
}
