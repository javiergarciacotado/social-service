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

public class PutStoryStoryHandler extends ParentStoryHandler implements Handler {

    private final StoryProvider storyProvider;

    public PutStoryStoryHandler(final StoryProvider storyProvider) {
        this.storyProvider = storyProvider;
    }

    @Override
    public Response execute(HttpExchange httpExchange) {

        final String path = httpExchange.getRequestURI().getPath();
        final Pattern likePattern = Pattern.compile("/story/(\\d+)/like");
        final Optional<Matcher> likeMatcher = matches(path, likePattern);
        final Pattern dislikePattern = Pattern.compile("/story/(\\d+)/dislike");
        final Optional<Matcher> dislikeMatcher = matches(path, dislikePattern);

        int storyId = getStoryIdFrom(path);
        Optional<Story> story;

        if (likeMatcher.isPresent()) {
            story = storyProvider.incrementPopularity(storyId);
            return createResponse(storyId, story);
        }

        if (dislikeMatcher.isPresent()) {
            story = storyProvider.decrementPopularity(storyId);
            return createResponse(storyId, story);
        }

        return badRequest(path);
    }

    private Response createResponse(int storyId, Optional<Story> s) {
        if (s.isPresent()) {
            final Story story = s.get();
            if (story.getPopularity() != -1) {
                return new Response(Status.OK, new ResponseHeaders(), new Body(story));
            }
            return sqlException(storyId);
        }
        return notFound(storyId);
    }


}
