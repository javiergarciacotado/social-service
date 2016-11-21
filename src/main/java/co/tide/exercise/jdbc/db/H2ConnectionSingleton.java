package co.tide.exercise.jdbc.db;

import co.tide.exercise.configuration.PropertiesReader;
import org.h2.jdbcx.JdbcConnectionPool;

import java.io.IOException;

public class H2ConnectionSingleton {

    private static H2ConnectionSingleton INSTANCE;

    private JdbcConnectionPool jdbcConnectionPool;

    private H2ConnectionSingleton() {
        try {
            PropertiesReader propertiesReader = new PropertiesReader("/application.properties");

            final String driver = propertiesReader.get("social.service.application.db.driver");
            final String connection = propertiesReader.get("social.service.application.db.connection");
            final String user = propertiesReader.get("social.service.application.db.user");
            final String password = propertiesReader.get("social.service.application.db.password");

            Class.forName(driver);
            jdbcConnectionPool = JdbcConnectionPool.create(connection, user, password);

        } catch (ClassNotFoundException | IOException exception) {
            exception.printStackTrace();
        }
    }

    public static JdbcConnectionPool getConnectionPool() {
        if (INSTANCE == null) {
            INSTANCE = new H2ConnectionSingleton();
        }
        return INSTANCE.jdbcConnectionPool;
    }

}
