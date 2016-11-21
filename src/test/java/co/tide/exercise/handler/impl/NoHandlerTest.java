package co.tide.exercise.handler.impl;

import co.tide.exercise.response.Response;
import co.tide.exercise.response.Status;
import org.junit.Test;

import static org.junit.Assert.*;

public class NoHandlerTest {

    @Test
    public void givenAnInvalidRequestShouldReturnBadRequest() {
        NoStoryHandler noHandler = new NoStoryHandler();
        final Response response = noHandler.execute(null);
        assertEquals(Status.BAD_REQUEST, response.getStatus());
    }
}