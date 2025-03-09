package org.thakur.kahanidukan.services;

import org.springframework.stereotype.Service;
import org.thakur.kahanidukan.models.Story;
import org.thakur.kahanidukan.services.repository.StoryRepository;

import java.util.*;

@Service
public class StoryService {
    private final StoryRepository storyRepository;
    private final Random random;

    public StoryService(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
        this.random = new Random();
    }

    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }

    public Story getRandomStory() {
        final List<Story> stories = storyRepository.findAll();
        final int randomIndex = random.nextInt(stories.size());
        return stories.get(randomIndex);
    }

    public List<Story> getStoriesByAuthor(String author) {
        return storyRepository.findByAuthor(author);
    }

    public List<String> getAllAuthors() {
        return storyRepository
                .findAll()
                .stream()
                .map(Story::author)
                .toList();
    }

    public Story getStoryById(String id) {
        return storyRepository.findById(id).orElse(null);
    }

    public void removeStoryById(String id) {
        storyRepository.deleteById(id);
    }

    public void addStory(Story newStory) {
        storyRepository.insert(newStory);
    }
}
