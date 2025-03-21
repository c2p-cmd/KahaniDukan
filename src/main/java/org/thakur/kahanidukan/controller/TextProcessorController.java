package org.thakur.kahanidukan.controller;

import org.springframework.web.bind.annotation.*;

import org.thakur.kahanidukan.models.*;

import org.thakur.kahanidukan.models.exceptions.ContentNotFoundException;
import org.thakur.kahanidukan.services.*;

import java.util.List;

@RestController
@RequestMapping("/text-processor")
public class TextProcessorController {
    private final TextProcessorService textProcessorService;
    private final StoryService storyService;

    public TextProcessorController(TextProcessorService textProcessorService, StoryService storyService) {
        this.textProcessorService = textProcessorService;
        this.storyService = storyService;
    }

    @RequestMapping(path = "/word-frequency", method = RequestMethod.GET)
    public List<WordFrequency> getWordFrequencyOf(@RequestParam String storyId, @RequestParam(defaultValue = "10") int limit) {
        final Story story = storyService.getStoryById(storyId);
        if (story == null) {
            throw new ContentNotFoundException("Story not found for id: " + storyId);
        }
        final String text = story.story();
        return textProcessorService.getWordFrequency(text, limit);
    }

    @RequestMapping(path = "/summarize", method = RequestMethod.GET)
    public Message summarize(@RequestParam String storyId) {
        final Story story = storyService.getStoryById(storyId);
        if (story == null) {
            throw new ContentNotFoundException("Story not found for id: " + storyId);
        }
        final String text = story.story();
        return new Message(textProcessorService.summarizeText(text, 3));
    }

    @RequestMapping(path = "/top-10-longest-stories", method = RequestMethod.GET)
    public List<Story> top10LongestStories() {
        return storyService.top10LongestStories();
    }
}
