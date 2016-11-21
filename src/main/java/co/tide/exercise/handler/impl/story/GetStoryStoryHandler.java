package co.tide.exercise.handler.impl.story;

import co.tide.exercise.handler.Handler;
import co.tide.exercise.model.Story;
import co.tide.exercise.response.Body;
import co.tide.exercise.response.Response;
import co.tide.exercise.response.ResponseHeaders;
import co.tide.exercise.response.Status;
import co.tide.exercise.provider.StoryProvider;
import com.sun.net.httpserver.HttpExchange;

import java.util.Optional;

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

        if (path.matches("/story/(\\d+)")) {
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
