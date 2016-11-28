package co.tide.exercise.handler.impl.story;

import co.tide.exercise.error.Problem;
import co.tide.exercise.error.ProblemType;
import co.tide.exercise.model.Story;
import co.tide.exercise.provider.StoryProvider;
import co.tide.exercise.response.*;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostStoryStoryHandlerTest {

    private static final String VALID_PATH = "/story/1";
    private static final String INVALID_PATH = "/story/-1";

    @Mock
    private HttpExchange httpExchange;
    @Mock private StoryProvider storyProvider;
    private PostStoryStoryHandler postStoryHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.postStoryHandler = new PostStoryStoryHandler(storyProvider);
    }

    @Test
    public void givenAValidPathAndAProblemWhileSavingStoryShouldReturnAProblem() throws URISyntaxException, IOException {

        final URI uri = new URI(VALID_PATH);
        final int storyId = 1;
        InputStream body = new ByteArrayInputStream("".getBytes());
        Story story = new Story.Builder().withId(storyId).build();

        final Problem problem = new Problem(ProblemType.SQL_EXCEPTION, "Problem while updating popularity", "There was a problem to update the story: " + storyId);
        final Response expectedResponse = new Response(Status.INTERNAL_SERVER_ERROR, new ResponseHeaders(), new Body(problem));

        when(httpExchange.getRequestURI()).thenReturn(uri);
        when(httpExchange.getRequestBody()).thenReturn(body);
        when(storyProvider.save(story)).thenReturn(-1);

        final Response response = postStoryHandler.execute(httpExchange);

        assertThat(expectedResponse, is(equalTo(response)));
    }

    @Test
    public void givenAValidPathAndNoJsonBodyShouldInsertStoryWithZeroPopularity() throws URISyntaxException {

        final URI uri = new URI(VALID_PATH);
        final int storyId = 1;
        InputStream inputStream = new ByteArrayInputStream("".getBytes());
        Story story = new Story.Builder().withId(storyId).build();

        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        HttpContext context = mock(HttpContext.class);

        final ResponseHeaders responseHeaders = new ResponseHeaders(new ResponseHeader("Location", "http://localhost:8080/story/" + storyId));
        final Response expectedResponse = new Response(Status.CREATED, responseHeaders, new Body(story));

        when(httpExchange.getRequestURI()).thenReturn(uri);
        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getLocalAddress()).thenReturn(address);
        when(httpExchange.getHttpContext()).thenReturn(context);
        when(storyProvider.save(story)).thenReturn(1);
        when(context.getPath()).thenReturn("/story");

        final Response response = postStoryHandler.execute(httpExchange);

        assertThat(expectedResponse, is(equalTo(response)));
    }

    @Test
    public void givenAValidPathAndJsonBodyShouldInsertStoryWithThatPopularity() throws URISyntaxException {

        final URI uri = new URI(VALID_PATH);
        final int storyId = 1;
        InputStream inputStream = new ByteArrayInputStream("{popularity: 10}".getBytes());
        Story story = new Story.Builder().withId(storyId).withPopularity(10).build();

        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        HttpContext context = mock(HttpContext.class);

        final ResponseHeaders responseHeaders = new ResponseHeaders(new ResponseHeader("Location", "http://localhost:8080/story/" + storyId));
        final Response expectedResponse = new Response(Status.CREATED, responseHeaders, new Body(story));

        when(httpExchange.getRequestURI()).thenReturn(uri);
        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getLocalAddress()).thenReturn(address);
        when(httpExchange.getHttpContext()).thenReturn(context);
        when(storyProvider.save(story)).thenReturn(1);
        when(context.getPath()).thenReturn("/story");

        final Response response = postStoryHandler.execute(httpExchange);

        assertThat(expectedResponse, is(equalTo(response)));
    }

}