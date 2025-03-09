package org.thakur.kahanidukan.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stories")
public record StoryWithTextScore(
        String id,
        String datetime,
        String title,
        String story,
        String moral,
        String author,
        double score
) {
    public Story toStory() {
        return new Story(id, datetime, title, story, moral, author);
    }
}
