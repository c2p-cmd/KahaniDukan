package org.thakur.kahanidukan.models;

public record StoryRequest(String title, String story, String moral, String author) {
    public Story toStory() {
        return new Story(
            null,
            null,
            title,
            story,
            moral,
            author
        );
    }
}
