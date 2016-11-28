package co.tide.exercise.handler.impl.story;

import co.tide.exercise.error.Problem;
import co.tide.exercise.error.ProblemType;
import co.tide.exercise.response.Body;
import co.tide.exercise.response.Response;
import co.tide.exercise.response.ResponseHeaders;
import co.tide.exercise.response.Status;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.text.MessageFormat.format;

public class ParentStoryHandler {

    Optional<Matcher> matches(String path, Pattern pattern) {
        return Stream.of(path).map(pattern::matcher).filter(Matcher::matches).findFirst();
    }

    int getStoryIdFrom(final String path) {
        Pattern pattern = Pattern.compile("(\\d+)");
        final Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return -1;
    }

    Response badRequest(String path) {
        final Problem problem = new Problem(ProblemType.INVALID_PARAM, format("Invalid {0} parameter", path), format("Invalid value for {0} path supplied", path));
        return new Response(Status.BAD_REQUEST, new ResponseHeaders(), new Body(problem));
    }

    Response sqlException(int storyId) {
        final Problem problem = new Problem(ProblemType.SQL_EXCEPTION, "Problem while updating popularity", "There was a problem to update the story: " + storyId);
        return new Response(Status.INTERNAL_SERVER_ERROR, new ResponseHeaders(), new Body(problem));
    }

    Response notFound(int storyId) {
        final Problem problem = new Problem(ProblemType.INVALID_PARAM, "Resource not found", "There is no popularity for story: " + storyId);
        return new Response(Status.NOT_FOUND, new ResponseHeaders(), new Body(problem));
    }

}
