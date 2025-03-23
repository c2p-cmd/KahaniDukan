package org.thakur.kahanidukan.services;

import org.springframework.stereotype.Service;

import org.thakur.kahanidukan.models.*;

import org.thakur.kahanidukan.models.exceptions.AuthorNotFoundException;
import org.thakur.kahanidukan.models.exceptions.BadSortingOrderException;
import org.thakur.kahanidukan.models.exceptions.ContentNotFoundException;
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

    public List<Story> getAllStoriesSortedByDate(String sortingOrder) throws BadSortingOrderException {
        return switch (sortingOrder) {
            case "asc" -> storyRepository.findAllByOrderByDatetimeAsc();
            case "desc" -> storyRepository.findAllByOrderByDatetimeDesc();
            case "none" -> storyRepository.findAll();
            default -> throw new BadSortingOrderException("Bad sorting order. Use values 'asc', 'desc', or 'none'");
        };
    }

    public Story getRandomStory() {
        final List<Story> stories = storyRepository.findAll();
        final int randomIndex = random.nextInt(stories.size());
        return stories.get(randomIndex);
    }

    public List<Story> getStoriesByAuthor(String author) throws AuthorNotFoundException {
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

    public Story getStoryById(String id) throws ContentNotFoundException {
        return storyRepository.findById(id).orElseThrow(ContentNotFoundException::new);
    }

    public void removeStoryById(String id) throws ContentNotFoundException {
        storyRepository
            .findById(id)
            .ifPresentOrElse(
                storyRepository::delete,
                () -> {
                    throw new ContentNotFoundException();
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

    public List<Story> top10LongestStories() {
        return storyRepository
                .findAll()
                .stream()
                .sorted((s1, s2) -> s2.story().length() - s1.story().length())
                .limit(10)
                .toList();
    }
}
