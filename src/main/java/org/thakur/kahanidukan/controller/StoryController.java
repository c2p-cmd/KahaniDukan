package org.thakur.kahanidukan.controller;

import org.springframework.web.bind.annotation.*;
import org.thakur.kahanidukan.models.Message;
import org.thakur.kahanidukan.models.Story;
import org.thakur.kahanidukan.services.StoryService;

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
    public List<Story> getStoriesByAuthor(@RequestParam String author) {
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
        storyService.addStory(story);
        return new Message("Story added successfully");
    }

    // Update story
    @RequestMapping(value = "story", method = RequestMethod.PUT)
    public Message updateStory(@RequestBody Story story) {
        storyService.removeStoryById(story.id());
        storyService.addStory(story);
        return new Message("Story updated successfully");
    }
}
