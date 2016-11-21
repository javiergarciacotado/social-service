package co.tide.exercise.handler.factory;

import co.tide.exercise.handler.Handler;
import co.tide.exercise.response.Response;
import co.tide.exercise.response.Status;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HandlerFactoryTest {

    private HandlerFactory handlerFactory;

    @Before
    public void setUp() {
        this.handlerFactory = new HandlerFactory();
    }

    @Test
    public void givenANonHandlerShouldNotRegisterIt() {
        handlerFactory.register("test", null);
        final Handler handler = handlerFactory.get("test");
        assertEquals(0, handlerFactory.size());
    }

    @Test
    public void givenAHandlerShouldRegisterIt() {
        handlerFactory.register("test", HandlerTest::new);
        final Handler handler = handlerFactory.get("test");
        assertEquals(Status.OK, handler.execute(null).getStatus());

    }
}

class HandlerTest implements Handler {

    @Override
    public Response execute(HttpExchange httpExchange) {
        return new Response(Status.OK, null, null);
    }
}

