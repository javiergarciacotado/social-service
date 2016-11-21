package co.tide.exercise.response;

import co.tide.exercise.model.Story;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class StorySerializerTest {

    private StorySerializer storySerializer;

    @Before
    public void setUp() {
        this.storySerializer = new StorySerializer();
    }

    @Test
    public void givenANullStoryShouldReturnEmpty() {
        final String json = "{}";
        assertStorySerialization(null, json);
    }


    @Test
    public void givenAnEmptyStoryShouldSerializePopularity() {
        final Story story = new Story();
        final String json = "{popularity: 0}";
        assertStorySerialization(story, json);
    }

    @Test
    public void givenAStoryShouldOnlySerializePopularity() {
        Story story = new Story.Builder().withId(1).withPopularity(10).build();
        String json = "{popularity: 10}";
        assertStorySerialization(story, json);

    }

    private void assertStorySerialization(final Story story, final String json) {
        final JsonElement jsonSerialized = storySerializer.serialize(story, Story.class, null);
        final JsonElement expectedJson = new JsonParser().parse(json);
        assertThat(expectedJson, is(equalTo(jsonSerialized)));
    }

}