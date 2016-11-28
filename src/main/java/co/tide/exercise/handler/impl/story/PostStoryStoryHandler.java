package co.tide.exercise.handler.impl.story;

import co.tide.exercise.handler.Handler;
import co.tide.exercise.model.Story;
import co.tide.exercise.provider.StoryProvider;
import co.tide.exercise.response.*;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class for handling POST story requests
 */
public class PostStoryStoryHandler extends ParentStoryHandler implements Handler {

    private final StoryProvider storyProvider;

    public PostStoryStoryHandler(final StoryProvider storyProvider) {
        this.storyProvider = storyProvider;
    }

    /**
     * Method to process the request
     * @param httpExchange contains info of request/response
     * @return Response to the client
     */
    @Override
    public Response execute(HttpExchange httpExchange) {
        final String path = httpExchange.getRequestURI().getPath();

        final Pattern pattern = Pattern.compile("/story/(\\d+)");
        final Optional<Matcher> matcher = matches(path, pattern);

        if(matcher.isPresent()) {
            int storyId = getStoryIdFrom(path);
            if (storyId != -1) {
                final Story story = retrievePopularityFromRequestBody(httpExchange, storyId);
                story.setId(storyId);
                final int storySaved = storyProvider.save(story);

                if (storySaved != -1) {

                    ResponseHeaders responseHeaders = getResponseHeaders(httpExchange, storyId);
                    return new Response(Status.CREATED, responseHeaders, new Body(story));
                }
                return sqlException(storyId);

            }
        }

        return badRequest(path);
    }

    /**
     * Set response headers for the response
     * @param httpExchange contains info of request/response
     * @param storyId story's id
     * @return Custom response headers set
     */
    private ResponseHeaders getResponseHeaders(HttpExchange httpExchange, int storyId) {

        final String hostName = httpExchange.getLocalAddress().getHostName();
        final int port = httpExchange.getLocalAddress().getPort();
        final String contextPath = httpExchange.getHttpContext().getPath();

        return new ResponseHeaders(locationHeader(hostName, port, contextPath, storyId));
    }

    /**
     * Sets the 'Location' header value
     * @param hostName Host
     * @param port Port
     * @param contextPath Context of the app
     * @param storyId story's number
     * @return a new response header to be added
     */
    private ResponseHeader locationHeader(String hostName, int port, String contextPath, int storyId) {

        StringBuilder sb = new StringBuilder();

        sb.append("http://");
        sb.append(hostName);
        sb.append(":");
        sb.append(port);
        sb.append(contextPath);
        sb.append("/");
        sb.append(storyId);

        return new ResponseHeader("Location", sb.toString());
    }

    /**
     * Retrieves popularity from request JSON body
     * @param httpExchange contains info of request/response
     * @param storyId story's number
     * @return Story from request
     */
    private Story retrievePopularityFromRequestBody(HttpExchange httpExchange, int storyId) {
        final InputStream inputStream = httpExchange.getRequestBody();
        Gson gson = new Gson();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final Optional<Story> story = Optional.ofNullable(gson.fromJson(reader, Story.class));
        return story.orElse(new Story.Builder().withId(storyId).build());
    }

}
