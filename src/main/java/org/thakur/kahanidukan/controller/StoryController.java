package org.thakur.kahanidukan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thakur.kahanidukan.models.*;
import org.thakur.kahanidukan.models.exceptions.AuthorNotFoundException;
import org.thakur.kahanidukan.models.exceptions.ContentNotFoundException;
import org.thakur.kahanidukan.models.exceptions.DateTimeInFutureException;
import org.thakur.kahanidukan.models.exceptions.EmptyStoryException;
import org.thakur.kahanidukan.services.StoryService;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/stories")
public class StoryController {
    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    private static void validateStoryElseThrow(Story story) throws ContentNotFoundException, AuthorNotFoundException, DateTimeInFutureException {
        if (story.story() == null || story.story().isBlank()) {
            throw new ContentNotFoundException("Story is required");
        }
        if (story.moral() == null || story.moral().isBlank()) {
            throw new ContentNotFoundException("Moral is required");
        }
        if (story.author() == null || story.author().isBlank()) {
            throw new AuthorNotFoundException("Author is required");
        }
        if (story.datetime() == null || story.datetime().isBlank()) {
            throw new ContentNotFoundException("Datetime is required");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime storyDatetime = ZonedDateTime.parse(story.datetime(), Story.formatter);
        if (storyDatetime.isAfter(now)) {
            throw new DateTimeInFutureException();
        }
    }

    // Get all stories
    @RequestMapping(method = RequestMethod.GET)
    public List<Story> getAllStories(@RequestParam(defaultValue = "none") String sortingOrder) {
        return storyService.getAllStoriesSortedByDate(sortingOrder);
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
    @RequestMapping(value = "/story", method = RequestMethod.POST)
    public ResponseEntity<Message> addStory(@RequestBody StoryRequest storyRequest) {
        StoryController.validateStoryElseThrow(storyRequest.toStory());

        storyService.addStory(storyRequest.toStory());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new Message("Story added successfully"));
    }

    // Update story
    @RequestMapping(value = "/story", method = RequestMethod.PUT)
    public ResponseEntity<Message> updateStory(@RequestParam String storyId, @RequestBody StoryRequest newStory) {
        if (storyId == null || storyId.isBlank()) {
            throw new ContentNotFoundException("Story ID is required");
        }

        Story storyToUpdate = storyService.getStoryById(storyId);
        String title = storyToUpdate.title();
        String author = storyToUpdate.author();
        String datetime = storyToUpdate.datetime();
        String moral = storyToUpdate.moral();

        if (newStory.title() == null || newStory.title().isBlank()) {
            title = storyToUpdate.title();
        }
        if (newStory.story() == null || newStory.story().isBlank()) {
            throw new EmptyStoryException();
        }
        if (newStory.moral() == null || newStory.moral().isBlank()) {
            moral = storyToUpdate.moral();
        }
        if (newStory.author() == null || newStory.author().isBlank()) {
            author = storyToUpdate.author();
        }

        storyToUpdate = new Story(storyId, datetime, title, newStory.story(), moral, author);

        storyService.removeStoryById(storyId);
        storyService.addStory(storyToUpdate);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new Message("Story updated successfully"));
    }

    // Delete a story
    @RequestMapping(value = "/story", method = RequestMethod.DELETE)
    public ResponseEntity<Message> deleteStory(@RequestParam String id) {
        storyService.removeStoryById(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new Message("Story deleted successfully"));
    }

    // Delete stories by author
    @RequestMapping(value = "/author", method = RequestMethod.DELETE)
    public ResponseEntity<Message> deleteStoriesByAuthor(@RequestParam String author) {
        final boolean didDelete = storyService.removeStoriesByAuthor(author);
        if (didDelete) {
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new Message("Stories deleted successfully"));
        } else {
            throw new ContentNotFoundException("No stories found for author: " + author);
        }
    }

    // Search for stories
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Story> searchStories(@RequestParam String query) {
        return storyService.searchStories(query);
    }
}
