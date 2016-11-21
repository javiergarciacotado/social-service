package co.tide.exercise;

import co.tide.exercise.configuration.PropertiesReader;
import co.tide.exercise.handler.factory.HandlerFactory;
import co.tide.exercise.handler.impl.story.GetStoryStoryHandler;
import co.tide.exercise.handler.impl.story.PostStoryStoryHandler;
import co.tide.exercise.handler.impl.story.PutStoryStoryHandler;
import co.tide.exercise.handler.impl.story.StoryHandler;
import co.tide.exercise.jdbc.dao.StoryRepository;
import co.tide.exercise.jdbc.dao.impl.StoryRepositoryImpl;
import co.tide.exercise.jdbc.db.H2ConnectionSingleton;
import co.tide.exercise.provider.StoryProvider;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Entry point for the Social Service exercise
 */
class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

        createAppTables();

        PropertiesReader propertiesReader = new PropertiesReader("/application.properties");
        int port = Integer.parseInt(propertiesReader.get("social.service.application.port"));
        String context = propertiesReader.get("social.service.application.context");

        startServer(context, port);
    }

    /**
     * Starts the server at a given port
     * @param port Listening port for the app
     * @throws IOException Produced by failed or interrupted I/O operations.
     */
    private static void startServer(String context, int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext(context, new StoryHandler(registerHandlers()));
        server.start();
        System.out.println("****Server started: Listening on port: " + port);
    }

    /**
     * Responsible for the creation of the application's tables
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    private static void createAppTables() throws SQLException {
        try (Connection connection = H2ConnectionSingleton.getConnectionPool().getConnection()) {
            final PreparedStatement createTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS story(id INT PRIMARY KEY, popularity INT)");
            createTable.executeUpdate();
        }
    }

    /**
     * Register request methods available for this application
     * @return factory to manipulate in the app
     */
    private static HandlerFactory registerHandlers() {
        HandlerFactory handlerFactory = new HandlerFactory();
        final StoryRepository storyRepository = new StoryRepositoryImpl();
        final StoryProvider storyProvider = new StoryProvider(storyRepository);

        handlerFactory.register("GET", () -> new GetStoryStoryHandler(storyProvider));
        handlerFactory.register("POST", () -> new PostStoryStoryHandler(storyProvider));
        handlerFactory.register("PUT", () -> new PutStoryStoryHandler(storyProvider));

        return handlerFactory;
    }




}
