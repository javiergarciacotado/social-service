package co.tide.exercise.jdbc.dao;

import co.tide.exercise.model.Story;

public interface StoryRepository {

    Story findBy(int storyId);

    int create(final Story story);

    int incrementLikes(final Story story);

    int decrementLikes(final Story story);

}
