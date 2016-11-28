package co.tide.exercise.handler.impl.story;

import co.tide.exercise.handler.Handler;
import co.tide.exercise.model.Story;
import co.tide.exercise.provider.StoryProvider;
import co.tide.exercise.response.Body;
import co.tide.exercise.response.Response;
import co.tide.exercise.response.ResponseHeaders;
import co.tide.exercise.response.Status;
import com.sun.net.httpserver.HttpExchange;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for handling GET story requests
 */
public class GetStoryStoryHandler extends ParentStoryHandler implements Handler {

    private final StoryProvider storyProvider;

    public GetStoryStoryHandler(final StoryProvider storyProvider) {
        this.storyProvider = storyProvider;
    }

    @Override
    public Response execute(final HttpExchange httpExchange) {
        final String path = httpExchange.getRequestURI().getPath();

        final Pattern pattern = Pattern.compile("/story/(\\d+)");
        final Optional<Matcher> matcher = matches(path, pattern);

        if(matcher.isPresent()) {
            final int storyId = getStoryIdFrom(path);
            if (storyId != -1) {
                final Optional<Story> story = storyProvider.retrieve(storyId);
                if (story.isPresent()) return new Response(Status.OK, new ResponseHeaders(), new Body(story.get()));
                return notFound(storyId);
            }
        }

        return badRequest(path);

    }


}
