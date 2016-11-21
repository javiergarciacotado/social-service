package co.tide.exercise.jdbc.dao.impl;

import co.tide.exercise.jdbc.dao.StoryRepository;
import co.tide.exercise.jdbc.db.H2ConnectionSingleton;
import co.tide.exercise.model.Story;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoryRepositoryImpl implements StoryRepository {

    @Override
    public Story findBy(int storyId) {

        String query = "SELECT * FROM story WHERE id=" + storyId;
        try (Connection connection = getConnection()) {
            final PreparedStatement preparedStatement = connection.prepareStatement(query);
            final ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int popularity = rs.getInt("popularity");
                return new Story.Builder().withId(id).withPopularity(popularity).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int create(Story story) {

        String query = "INSERT INTO story VALUES( " + story.getId() + "," + story.getPopularity() + ")";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            final PreparedStatement preparedStatement = connection.prepareStatement(query);
            final int execution = preparedStatement.executeUpdate();
            connection.commit();
            return execution;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int incrementLikes(Story story) {

        int popularity = story.getPopularity() + 1;
        return updatePopularity(story, popularity);
    }

    @Override
    public int decrementLikes(Story story) {

        int popularity = story.getPopularity() - 1;
        return updatePopularity(story, popularity);
    }

    private Connection getConnection() throws SQLException {
        final JdbcConnectionPool connectionPool = H2ConnectionSingleton.getConnectionPool();
        return connectionPool.getConnection();
    }

    private int updatePopularity(Story story, int popularity) {

        String query = "UPDATE story SET popularity= " + popularity + "WHERE id= " + story.getId();
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            final PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            connection.commit();
            return popularity;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return -1;
    }
}
