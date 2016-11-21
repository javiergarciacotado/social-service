package co.tide.exercise.handler.impl.story;

import co.tide.exercise.handler.factory.HandlerFactory;
import co.tide.exercise.handler.impl.NoStoryHandler;
import co.tide.exercise.response.Body;
import co.tide.exercise.response.Response;
import co.tide.exercise.response.ResponseHeaders;
import co.tide.exercise.response.Status;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Mockito.*;

public class StoryHandlerTest {

    private StoryHandler storyHandler;

    @Mock private HandlerFactory handlerFactory;
    @Mock private HttpExchange httpExchange;
    @Mock private OutputStream outputStream;
    @Mock private Response response;
    @Mock private Headers headers;
    @Mock private ResponseHeaders responseHeaders;
    @Mock private Body body;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.storyHandler = new StoryHandler(handlerFactory);
    }

    @Test
    public void givenAnInvalidRequestShouldHandleAResponse() throws Exception {

        NoStoryHandler noHandler = mock(NoStoryHandler.class);

        when(httpExchange.getRequestMethod()).thenReturn("DELETE");
        when(handlerFactory.get("DELETE")).thenReturn(noHandler);
        when(httpExchange.getResponseHeaders()).thenReturn(headers);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);
        when(noHandler.execute(httpExchange)).thenReturn(response);
        when(response.getStatus()).thenReturn(Status.OK);
        when(response.getResponseHeaders()).thenReturn(responseHeaders);
        when(response.getBody()).thenReturn(new Body("The requested method is not implemented"));

        storyHandler.handle(httpExchange);

        verify(httpExchange, times(1)).getRequestMethod();
        verify(httpExchange, times(1)).getResponseHeaders();
        verify(httpExchange, times(1)).getResponseBody();
        verify(handlerFactory.get("DELETE"), times(1)).execute(httpExchange);
        verify(response, times(1)).getStatus();
        verify(response, times(1)).getResponseHeaders();
        verify(response, times(1)).getBody();
    }

    @Test
    public void givenAValidRequestMethodShouldHandleAResponse() throws IOException {

        GetStoryStoryHandler getStoryHandler = mock(GetStoryStoryHandler.class);

        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(handlerFactory.get("GET")).thenReturn(getStoryHandler);
        when(httpExchange.getResponseHeaders()).thenReturn(headers);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);
        when(getStoryHandler.execute(httpExchange)).thenReturn(response);
        when(response.getStatus()).thenReturn(Status.OK);
        when(response.getResponseHeaders()).thenReturn(responseHeaders);
        when(response.getBody()).thenReturn(new Body("The requested method is not implemented"));

        storyHandler.handle(httpExchange);

        verify(httpExchange, times(1)).getRequestMethod();
        verify(httpExchange, times(1)).getResponseHeaders();
        verify(httpExchange, times(1)).getResponseBody();
        verify(handlerFactory.get("GET"), times(1)).execute(httpExchange);
        verify(response, times(1)).getStatus();
        verify(response, times(1)).getResponseHeaders();
        verify(response, times(1)).getBody();
    }

}