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

import static java.text.MessageFormat.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

public class GetStoryHandlerTest {

    private static final String VALID_PATH = "/story/1";
    private static final String INVALID_PATH = "/story/-1";

    @Mock private HttpExchange httpExchange;
    @Mock private StoryProvider storyProvider;
    private GetStoryStoryHandler getStoryHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.getStoryHandler = new GetStoryStoryHandler(storyProvider);
    }

    @Test
    public void givenAnInvalidPathShouldReturnBadRequest() throws URISyntaxException {
        final URI uri =  new URI(INVALID_PATH);
        final Problem problem = new Problem(ProblemType.INVALID_PARAM, format("Invalid {0} parameter", uri.getPath()), format("Invalid value for {0} path supplied", uri.getPath()));
        final Response expectedResponse = new Response(Status.BAD_REQUEST, new ResponseHeaders(), new Body(problem));

        when(httpExchange.getRequestURI()).thenReturn(uri);

        final Response response = getStoryHandler.execute(httpExchange);

        assertThat(expectedResponse, is(equalTo(response)));
    }

    @Test
    public void givenAValidPathAndANonStoredProductShouldReturnNotFoundResponse() throws URISyntaxException {
        final URI uri = new URI(VALID_PATH);
        final int storyId = 1;
        final Problem problem = new Problem(ProblemType.INVALID_PARAM, "Resource not found", "There is no popularity for story: " + storyId);
        final Response expectedResponse = new Response(Status.NOT_FOUND, new ResponseHeaders(), new Body(problem));

        when(httpExchange.getRequestURI()).thenReturn(uri);
        when(storyProvider.retrieve(storyId)).thenReturn(Optional.empty());

        final Response response = getStoryHandler.execute(httpExchange);

        assertThat(expectedResponse, is(equalTo(response)));
    }

    @Test
    public void givenAValidPathAndStoredStoryShouldReturnResponse() throws URISyntaxException {

        final URI uri = new URI(VALID_PATH);
        final int storyId = 1;
        final int popularity = 10;

        final Story story = new Story.Builder().withId(storyId).withPopularity(popularity).build();
        final Response expectedResponse = new Response(Status.OK, new ResponseHeaders(), new Body(story));

        when(httpExchange.getRequestURI()).thenReturn(uri);
        when(storyProvider.retrieve(storyId)).thenReturn(Optional.of(story));

        final Response response = getStoryHandler.execute(httpExchange);

        assertThat(expectedResponse, is(equalTo(response)));
    }
}