package co.tide.exercise.jdbc.dao;

import co.tide.exercise.model.Story;

import java.util.ArrayList;
import java.util.List;

public class StoryRepositoryStub implements StoryRepository {

    private List<Story> stories = new ArrayList<>();

    @Override
    public Story findBy(int storyId) {
        return stories.stream().filter(story -> story.getId() == storyId).findFirst().orElse(null);
    }

    @Override
    public int create(Story story) {
        stories.add(story);
        return 1;
    }

    @Override
    public int incrementLikes(final Story story) {
        final int popularity = story.getPopularity() + 1;
        return updatePopularity(story, popularity);
    }

    @Override
    public int decrementLikes(final Story story) {
        final int popularity = story.getPopularity() - 1;
        return updatePopularity(story, popularity);
    }

    private int updatePopularity(final Story story, int popularity) {
        final boolean storyRemoved = stories.removeIf(s -> s.equals(story));
        story.setPopularity(popularity);
        final boolean storyAdded = stories.add(story);
        return storyRemoved && storyAdded ? popularity : -1;
    }
}