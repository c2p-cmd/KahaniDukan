package org.thakur.kahanidukan.controller;

import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public List<Story> getAllStories() {
        return storyService.getAllStories();
    }

    @GetMapping("/random")
    public Story getRandomStory() {
        return storyService.getRandomStory();
    }

    @GetMapping("/author/{author}")
    public List<Story> getStoriesByAuthor(@PathVariable String author) {
        return storyService.getStoriesByAuthor(author);
    }

    @GetMapping("/story")
    public Story getStoryById(@RequestParam String id) {
        return storyService.getStoryById(id);
    }
}
