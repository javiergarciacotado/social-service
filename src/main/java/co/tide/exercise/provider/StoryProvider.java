package co.tide.exercise.provider;

import co.tide.exercise.jdbc.dao.StoryRepository;
import co.tide.exercise.model.Story;

import java.util.Optional;

public class StoryProvider {

    private final StoryRepository storyRepository;

    public StoryProvider(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public Optional<Story> retrieve(int storyId) {
        return Optional.ofNullable(storyRepository.findBy(storyId));
    }

    public int save(Story story) {
        if (story != null) {
            return storyRepository.create(story);
        }
        return -1;
    }

    public Optional<Story> incrementPopularity(int storyId) {
        final Optional<Story> storyRetrieved = Optional.ofNullable(storyRepository.findBy(storyId));

        if (storyRetrieved.isPresent()) {
            final Story story = storyRetrieved.get();
            final int popularity = storyRepository.incrementLikes(story);
            story.setPopularity(popularity);
            return Optional.of(story);
        }
        return Optional.empty();
    }

    public Optional<Story> decrementPopularity(int storyId) {
        final Optional<Story> storyRetrieved = Optional.ofNullable(storyRepository.findBy(storyId));

        if (storyRetrieved.isPresent()) {
            final Story story = storyRetrieved.get();
            final int popularity = storyRepository.decrementLikes(story);
            story.setPopularity(popularity);
            return Optional.of(story);
        }
        return Optional.empty();
    }
}
