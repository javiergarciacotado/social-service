package co.tide.exercise.provider;

import co.tide.exercise.jdbc.dao.StoryRepository;
import co.tide.exercise.model.Story;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class StoryProviderTest {

    private StoryProvider storyProvider;
    @Mock private StoryRepository storyRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.storyProvider = new StoryProvider(storyRepository);
    }

    @Test
    public void givenANonStoredStoryShouldNotRetrieveIt() throws Exception {

        final int storyId = 1;

        when(storyRepository.findBy(storyId)).thenReturn(null);

        final Optional<Story> storyRetrieved = storyProvider.retrieve(storyId);

        assertEquals(Optional.empty(), storyRetrieved);

    }

    @Test
    public void givenAStoredStoryShouldRetrieveIt() throws Exception {

        final int storyId = 1;
        final int popularity = 10;
        final Story story = new Story.Builder().withId(storyId).withPopularity(popularity).build();
        when(storyRepository.findBy(storyId)).thenReturn(story);

        final Optional<Story> storyRetrieved = storyProvider.retrieve(storyId);

        assertEquals(story, storyRetrieved.get());

    }

    @Test
    public void givenAnInvalidStoryShouldNotSaveIt() throws Exception {

        final int storyId = 1;
        final int popularity = 10;
        final int creationStatus = -1;

        final Story story = new Story.Builder().withId(storyId).withPopularity(popularity).build();
        when(storyRepository.create(story)).thenReturn(creationStatus);

        assertEquals(-1, storyProvider.save(story));
    }

    @Test
    public void givenAValidStoryShouldSaveIt() throws Exception {

        final int storyId = 1;
        final int popularity = 10;
        final int creationStatus = 1;

        final Story story = new Story.Builder().withId(storyId).withPopularity(popularity).build();
        when(storyRepository.create(story)).thenReturn(creationStatus);

        assertEquals(creationStatus, storyProvider.save(story));
    }

    @Test
    public void givenANonStoredStoryShouldNotIncrementPopularity() throws Exception {

        final int storyId = 1;
        final int popularity = 10;

        final Story story = new Story.Builder().withId(storyId).withPopularity(popularity).build();
        when(storyRepository.incrementLikes(story)).thenReturn(popularity);

        assertEquals(Optional.empty(), storyProvider.incrementPopularity(storyId));
    }

    @Test
    public void givenAStoredStoryShouldIncrementPopularity() throws Exception {

        final int storyId = 1;
        final int popularity = 10;

        final Story story = new Story.Builder().withId(storyId).withPopularity(popularity).build();
        when(storyRepository.findBy(storyId)).thenReturn(story);
        when(storyRepository.incrementLikes(story)).thenReturn(popularity + 1);

        final Optional<Story> storyRetrieved = storyProvider.incrementPopularity(storyId);
        assertEquals(popularity + 1, storyRetrieved.get().getPopularity());
    }

    @Test
    public void givenANonStoredStoryShouldNotDecrementPopularity() throws Exception {

        final int storyId = 1;
        final int popularity = 10;
        final int creationStatus = 1;

        final Story story = new Story.Builder().withId(storyId).withPopularity(popularity).build();
        when(storyRepository.decrementLikes(story)).thenReturn(creationStatus);

        assertEquals(Optional.empty(), storyProvider.incrementPopularity(1));
    }

    @Test
    public void givenAStoredStoryShouldDecrementPopularity() throws Exception {

        final int storyId = 1;
        final int popularity = 10;

        final Story story = new Story.Builder().withId(storyId).withPopularity(popularity).build();
        when(storyRepository.findBy(storyId)).thenReturn(story);
        when(storyRepository.incrementLikes(story)).thenReturn(popularity - 1);

        final Optional<Story> storyRetrieved = storyProvider.incrementPopularity(storyId);
        assertEquals(popularity - 1, storyRetrieved.get().getPopularity());
    }


}