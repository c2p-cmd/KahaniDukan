package org.thakur.kahanidukan.services.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.thakur.kahanidukan.models.Story;

import java.util.List;

public interface StoryRepository extends MongoRepository<Story, String> {
    List<Story> findByAuthor(String author);
}
