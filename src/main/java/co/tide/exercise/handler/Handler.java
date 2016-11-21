package co.tide.exercise.handler;

import co.tide.exercise.response.Response;
import com.sun.net.httpserver.HttpExchange;

public interface Handler {

    Response execute(final HttpExchange httpExchange);
}
