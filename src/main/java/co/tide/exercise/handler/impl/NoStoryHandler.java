package co.tide.exercise.handler.impl;

import co.tide.exercise.error.Problem;
import co.tide.exercise.error.ProblemType;
import co.tide.exercise.handler.Handler;
import co.tide.exercise.handler.impl.story.ParentStoryHandler;
import co.tide.exercise.response.Body;
import co.tide.exercise.response.Response;
import co.tide.exercise.response.Status;
import com.sun.net.httpserver.HttpExchange;

/**
 * Client making request methods (DELETE,PATCH) that currently are not implemented
 */
public class NoStoryHandler extends ParentStoryHandler implements Handler {

    @Override
    public Response execute(HttpExchange httpExchange) {
        Problem problem = new Problem(ProblemType.BAD_REQUEST, "HTTP Method", "The requested method is not implemented");
        return new Response(Status.BAD_REQUEST, null, new Body(problem));
    }
}
