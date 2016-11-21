package co.tide.exercise.handler.factory;

import co.tide.exercise.handler.Handler;
import co.tide.exercise.handler.impl.NoStoryHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Class to register/get Handlers
 */
public class HandlerFactory {

    private final Map<String, Supplier<Handler>> map = new HashMap<>();

    public void register(final String name, Supplier<Handler> supplier) {
        if (supplier != null) map.put(name, supplier);
    }

    public Handler get(final String name) {
        return map.getOrDefault(name, NoStoryHandler::new).get();
    }

    public int size() {
        return map.size();
    }


}
