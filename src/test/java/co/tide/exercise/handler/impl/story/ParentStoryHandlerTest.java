package co.tide.exercise.handler.impl.story;

import co.tide.exercise.error.Problem;
import co.tide.exercise.error.ProblemType;
import co.tide.exercise.response.Body;
import co.tide.exercise.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static java.text.MessageFormat.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;

public class ParentStoryHandlerTest {

    private ParentStoryHandler parentStoryHandler = new ParentStoryHandler();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.parentStoryHandler = new ParentStoryHandler();
    }

    @Test
    public void givenAnEmptyPathShouldReturnAFutureProblem() {
        final int storyIdFrom = parentStoryHandler.getStoryIdFrom("");
        assertEquals(-1, storyIdFrom);
    }

    @Test
    public void givenAnInvalidPathShouldReturnBadRequest() {
        String path = "/story/-1";
        final Problem problem = new Problem(ProblemType.INVALID_PARAM, format("Invalid {0} parameter", path), format("Invalid value for {0} path supplied", path));
        final Response response = parentStoryHandler.badRequest(path);

        assertThat(new Body(problem), is(equalTo(response.getBody())));
    }

    @Test
    public void givenASqlProblemShouldReturnAnInternalServerError() throws Exception {

        int storyId = 0;
        final Problem problem = new Problem(ProblemType.SQL_EXCEPTION, "Problem while updating popularity", "There was a problem to update the story: " + storyId);
        final Response response = parentStoryHandler.sqlException(storyId);

        assertThat(new Body(problem), is(equalTo(response.getBody())));

    }

    @Test
    public void givenNoResourceShouldReturnNotFound() throws Exception {

        int storyId = 0;
        final Problem problem = new Problem(ProblemType.INVALID_PARAM, "Resource not found", "There is no popularity for story: " + storyId);
        final Response response = parentStoryHandler.notFound(storyId);

        assertThat(new Body(problem), is(equalTo(response.getBody())));
    }

}