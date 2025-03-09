package org.thakur.kahanidukan.services;

import org.springframework.stereotype.Service;

import org.thakur.kahanidukan.models.*;

import org.thakur.kahanidukan.services.repository.*;

import java.util.*;

@Service
public class StoryService {
    private final StoryRepository storyRepository;
    private final StorySearchRepository storySearchRepository;
    private final Random random;

    public StoryService(
        StoryRepository storyRepository,
        StorySearchRepository storySearchRepository
    ) {
        this.storyRepository = storyRepository;
        this.storySearchRepository = storySearchRepository;
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
        final List<Story> storiesByAuthor = storyRepository.findByAuthor(author);
        if (storiesByAuthor == null) {
            throw new AuthorNotFoundException("Author not found");
        }
        if (storiesByAuthor.isEmpty()) {
            throw new AuthorNotFoundException("Author not found");
        }
        return storiesByAuthor;
    }

    public List<String> getAllAuthors() {
        return getAllStories()
                .stream()
                .map(Story::author)
                .distinct()
                .toList();
    }

    public Story getStoryById(String id) {
        return storyRepository.findById(id).orElseThrow(() -> new ContentNotFoundException("Story not found for id: " + id));
    }

    public void removeStoryById(String id) {
        storyRepository
            .findById(id)
            .ifPresentOrElse(
                storyRepository::delete,
                () -> {
                    throw new ContentNotFoundException("Story not found for id: " + id);
                }
            );
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

    public List<Story> searchStories(String query) {
        return storySearchRepository
                .findByTextSortedByRelevance(query)
                .stream()
                .sorted(Comparator.comparingDouble(StoryWithTextScore::score))
                .map(StoryWithTextScore::toStory)
                .toList();
    }
}
