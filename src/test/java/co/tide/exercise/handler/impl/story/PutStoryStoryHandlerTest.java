package co.tide.exercise.handler.impl.story;

import co.tide.exercise.error.Problem;
import co.tide.exercise.error.ProblemType;
import co.tide.exercise.model.Story;
import co.tide.exercise.provider.StoryProvider;
import co.tide.exercise.response.Body;
import co.tide.exercise.response.Response;
import co.tide.exercise.response.ResponseHeaders;
import co.tide.exercise.response.Status;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

public class PutStoryStoryHandlerTest {

    private static final String VALID_LIKE_PATH = "/story/1/like";
    private static final String VALID_DISLIKE_PATH = "/story/1/dislike";

    @Mock private HttpExchange httpExchange;
    @Mock private StoryProvider storyProvider;
    private PutStoryStoryHandler putStoryStoryHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.putStoryStoryHandler = new PutStoryStoryHandler(storyProvider);
    }

    @Test
    public void givenAValidPathAndAnInvalidStoryIdShouldReturnAProblem() throws URISyntaxException {

        final URI uri = new URI(VALID_LIKE_PATH);
        final int storyId = 1;

        final Problem problem = new Problem(ProblemType.INVALID_PARAM, "Resource not found", "There is no popularity for story: " + storyId);
        final Response expectedResponse = new Response(Status.NOT_FOUND, new ResponseHeaders(), new Body(problem));

        when(httpExchange.getRequestURI()).thenReturn(uri);
        when(storyProvider.incrementPopularity(storyId)).thenReturn(Optional.empty());

        final Response response = putStoryStoryHandler.execute(httpExchange);

        assertThat(expectedResponse, is(equalTo(response)));
    }

    @Test
    public void givenAValidPathAndAProblemUpdatingStoryShouldReturnAProblem() throws URISyntaxException {

        final URI uri = new URI(VALID_LIKE_PATH);
        final int storyId = 1;

        Story story = new Story.Builder().withId(storyId).withPopularity(-1).build();

        final Problem problem = new Problem(ProblemType.SQL_EXCEPTION, "Problem while updating popularity", "There was a problem to update the story: " + storyId);
        final Response expectedResponse = new Response(Status.INTERNAL_SERVER_ERROR, new ResponseHeaders(), new Body(problem));

        when(httpExchange.getRequestURI()).thenReturn(uri);
        when(storyProvider.incrementPopularity(storyId)).thenReturn(Optional.of(story));

        final Response response = putStoryStoryHandler.execute(httpExchange);

        assertThat(expectedResponse, is(equalTo(response)));
    }

    @Test
    public void givenAValidPathShouldIncrementPopularity() throws URISyntaxException {
        final URI uri =  new URI(VALID_LIKE_PATH);
        final int storyId = 1;
        final int popularity = 10;
        final Story story = new Story.Builder().withId(storyId).withPopularity(popularity + 1).build();

        final Response expectedResponse = new Response(Status.OK, new ResponseHeaders(), new Body(story));

        when(httpExchange.getRequestURI()).thenReturn(uri);
        when(storyProvider.incrementPopularity(storyId)).thenReturn(Optional.of(story));

        final Response response = putStoryStoryHandler.execute(httpExchange);

        assertThat(expectedResponse, is(equalTo(response)));
    }

    @Test
    public void givenAValidPathShouldDecrementPopularity() throws URISyntaxException {
        final URI uri =  new URI(VALID_DISLIKE_PATH);
        final int storyId = 1;
        final int popularity = 10;
        final Story story = new Story.Builder().withId(storyId).withPopularity(popularity - 1).build();

        final Response expectedResponse = new Response(Status.OK, new ResponseHeaders(), new Body(story));

        when(httpExchange.getRequestURI()).thenReturn(uri);
        when(storyProvider.decrementPopularity(storyId)).thenReturn(Optional.of(story));

        final Response response = putStoryStoryHandler.execute(httpExchange);

        assertThat(expectedResponse, is(equalTo(response)));
    }
}