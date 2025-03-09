package org.thakur.kahanidukan.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stories")
public record Story(String id, String datetime, String title, String content, String moral, String author) {
}
