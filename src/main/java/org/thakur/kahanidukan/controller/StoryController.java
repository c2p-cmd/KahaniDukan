package org.thakur.kahanidukan.controller;

import org.springframework.web.bind.annotation.*;
import org.thakur.kahanidukan.models.AuthorNotFoundException;
import org.thakur.kahanidukan.models.ContentNotFoundException;
import org.thakur.kahanidukan.models.Message;
import org.thakur.kahanidukan.models.Story;
import org.thakur.kahanidukan.services.StoryService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stories")

public class StoryController {
    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    // Get all stories
    @RequestMapping(method = RequestMethod.GET)
    public List<Story> getAllStories() {
        return storyService.getAllStories();
    }

    // Get random story
    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public Story getRandomStory() {
        return storyService.getRandomStory();
    }

    // Get all authors
    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public List<String> getAllAuthors() {
        return storyService.getAllAuthors();
    }

    // Get stories by author
    @RequestMapping(value = "/author", method = RequestMethod.GET)
    public List<Story> getStoriesByAuthor(@RequestParam String author) throws AuthorNotFoundException {
        return storyService.getStoriesByAuthor(author);
    }

    // Get story by id
    @RequestMapping(value = "/story", method = RequestMethod.GET)
    public Story getStoryById(@RequestParam String id) {
        return storyService.getStoryById(id);
    }

    // Post new story
    @RequestMapping(value = "story", method = RequestMethod.POST)
    public Message addStory(@RequestBody Story story) {
        if (story.author() == null || story.author().isBlank() || story.author().equalsIgnoreCase("null")) {
            throw new AuthorNotFoundException("Author is required");
        }
        storyService.addStory(story);
        return new Message("Story added successfully");
    }

    // Update story
    @RequestMapping(value = "story", method = RequestMethod.PUT)
    public Message updateStory(@RequestBody Story story) {
        if (story.author() == null || story.author().isBlank() || story.author().equalsIgnoreCase("null")) {
            throw new IllegalArgumentException("Author is required");
        }
        if (story.story() == null || story.story().isBlank() || story.story().equalsIgnoreCase("null")) {
            throw new IllegalArgumentException("Story is required");
        }
        if (story.moral() == null || story.moral().isBlank() || story.moral().equalsIgnoreCase("null")) {
            throw new IllegalArgumentException("Moral is required");
        }
        if (story.title() == null || story.title().isBlank() || story.title().equalsIgnoreCase("null")) {
            throw new IllegalArgumentException("Title is required");
        }
        if (story.datetime() == null || story.datetime().isBlank() || story.datetime().equalsIgnoreCase("null")) {
            throw new IllegalArgumentException("Datetime is required");
        }
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime storyDatetime = LocalDateTime.parse(story.datetime());
        if (storyDatetime.isAfter(now)) {
            throw new IllegalArgumentException("Datetime cannot be in the future");
        }
        storyService.removeStoryById(story.id());
        storyService.addStory(story);
        return new Message("Story updated successfully");
    }

    // Delete a story
    @RequestMapping(value = "story", method = RequestMethod.DELETE)
    public Message deleteStory(@RequestParam String id) {
        storyService.removeStoryById(id);
        return new Message("Story deleted successfully");
    }

    // Delete stories by author
    @RequestMapping(value = "author", method = RequestMethod.DELETE)
    public Message deleteStoriesByAuthor(@RequestParam String author) {
        final boolean didDelete = storyService.removeStoriesByAuthor(author);
        if (didDelete) {
            return new Message("Stories deleted successfully");
        } else {
            throw new ContentNotFoundException("No stories found for author: " + author);
        }
    }
}
