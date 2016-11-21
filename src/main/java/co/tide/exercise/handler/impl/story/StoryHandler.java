package co.tide.exercise.handler.impl.story;


import co.tide.exercise.handler.Handler;
import co.tide.exercise.handler.factory.HandlerFactory;
import co.tide.exercise.response.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class StoryHandler implements HttpHandler {

    private final HandlerFactory handlerFactory;

    public StoryHandler(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    @Override
    public void handle(final HttpExchange httpExchange) throws IOException {
        final Handler handler = handlerFactory.get(httpExchange.getRequestMethod());
        final Response response = handler.execute(httpExchange);
        write(httpExchange, response);
    }

    private void write(final HttpExchange httpExchange, final Response response) throws IOException {
        final Status status = response.getStatus();
        final Body body = response.getBody();
        final ResponseHeaders responseHeaders = response.getResponseHeaders();

        final String jsonMessage = createJsonBodyResponse(body);

        setResponseHeaders(httpExchange, responseHeaders);
        httpExchange.sendResponseHeaders(status.getCode(), jsonMessage.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(jsonMessage.getBytes());
        }
    }

    private void setResponseHeaders(final HttpExchange httpExchange, ResponseHeaders responseHeaders) {
        final Headers headers = httpExchange.getResponseHeaders();
        commonHeaders(headers);
        customHeaders(headers, responseHeaders);
    }

    private void commonHeaders(Headers headers) {
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "POST, GET, PUT");
    }

    private void customHeaders(Headers headers, ResponseHeaders responseHeaders) {
        for (ResponseHeader responseHeader : responseHeaders.getHeaders()) {
            headers.add(responseHeader.getName(), responseHeader.getValue());
        }
    }

    private String createJsonBodyResponse(Body body) {
        Gson json = new GsonBuilder().create();
        return json.toJson(body);
    }

}
