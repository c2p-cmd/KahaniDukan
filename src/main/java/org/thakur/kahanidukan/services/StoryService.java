package org.thakur.kahanidukan.services;

import org.springframework.stereotype.Service;
import org.thakur.kahanidukan.models.AuthorNotFoundException;
import org.thakur.kahanidukan.models.ContentNotFoundException;
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
        if (author == null || author.isBlank() || author.equalsIgnoreCase("null")) {
            throw new AuthorNotFoundException("Author not found");
        }
        if (getAllAuthors().contains(author)) {
            return storyRepository.findByAuthor(author);
        }
        throw new AuthorNotFoundException("Author not found");
    }

    public List<String> getAllAuthors() {
        return storyRepository
                .findAll()
                .stream()
                .map(Story::author)
                .toList();
    }

    public Story getStoryById(String id) throws ContentNotFoundException {
        return storyRepository
                .findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Story not found for id: " + id));
    }

    public void removeStoryById(String id) {
        storyRepository.deleteById(id);
    }

    public boolean removeStoriesByAuthor(String author) {
        final List<Story> stories = storyRepository.findByAuthor(author);
        if (stories.isEmpty()) {
            return false;
        }
        storyRepository.deleteAll(stories);
        return true;
    }

    public void addStory(Story newStory) {
        storyRepository.insert(newStory);
    }
}
