package co.tide.exercise.jdbc.dao.impl;

import co.tide.exercise.jdbc.dao.StoryRepository;
import co.tide.exercise.jdbc.db.H2ConnectionSingleton;
import co.tide.exercise.model.Story;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class StoryRepositoryIT {

    private StoryRepository storyRepository;

    @Before
    public void setUp() throws SQLException {
        storyRepository = new StoryRepositoryImpl();
        dropDatabase();
        createTables();
    }

    @After
    public void tearDown() throws Exception {
        final JdbcConnectionPool connectionPool = H2ConnectionSingleton.getConnectionPool();
        final Connection connection = connectionPool.getConnection();
        connection.close();
    }

    @Test
    public void givenANonStoredShouldNoRetrieveIt() {
        final int storyId = 1;
        assertNull(storyRepository.findBy(storyId));
    }

    @Test
    public void givenAStoredStoryShouldReturnAStory() throws Exception {

        final Story story = getInitStory();
        final int storyCreated = storyRepository.create(story);

        final Story storyRetrieved = storyRepository.findBy(story.getId());
        assertNotNull(storyCreated);
        assertEquals(story, storyRetrieved);
    }

    @Test
    public void givenAStoryShouldSaveIt() throws Exception {

        final Story story = getInitStory();
        final int storyCreated = storyRepository.create(story);

        assertTrue(storyCreated >= 0);
    }

    @Test
    public void givenAStoryShouldIncrementLikes() throws Exception {
        final Story story = createInitStory();

        final int popularityRetrieved = storyRepository.incrementLikes(story);
        assertEquals(11, popularityRetrieved);
    }

    @Test
    public void testDecrementLikes() throws Exception {
        final Story story = createInitStory();

        final int popularityRetrieved = storyRepository.decrementLikes(story);
        assertEquals(9, popularityRetrieved);
    }

    private Story createInitStory() {
        final Story story = getInitStory();
        storyRepository.create(story);
        return story;
    }

    private Story getInitStory() {
        final int storyId = 1;
        final int popularity = 10;

        return new Story.Builder().withId(storyId).withPopularity(popularity).build();
    }

    private void dropDatabase() throws SQLException {
        try (Connection connection = H2ConnectionSingleton.getConnectionPool().getConnection()) {
            final PreparedStatement createTable = connection.prepareStatement("DROP ALL OBJECTS");
            createTable.executeUpdate();
        }
    }

    private void createTables() throws SQLException {
        try (Connection connection = H2ConnectionSingleton.getConnectionPool().getConnection()) {
            final PreparedStatement createTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS story(id INT PRIMARY KEY, popularity INT)");
            createTable.executeUpdate();
        }
    }


}