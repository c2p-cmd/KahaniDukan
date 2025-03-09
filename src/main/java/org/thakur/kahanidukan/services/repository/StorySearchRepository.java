package org.thakur.kahanidukan.services.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.thakur.kahanidukan.models.StoryWithTextScore;

import java.util.List;

public interface StorySearchRepository extends MongoRepository<StoryWithTextScore, String> {
    @Query(value = "{ $text: { $search: ?0 } }", fields = "{ score: { $meta: 'textScore' } }")
    List<StoryWithTextScore> findByTextSortedByRelevance(String query);
}
