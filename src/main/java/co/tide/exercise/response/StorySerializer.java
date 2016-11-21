package co.tide.exercise.response;

import co.tide.exercise.model.Story;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

class StorySerializer implements JsonSerializer<Story> {

    @Override
    public JsonElement serialize(Story story, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject json = new JsonObject();
        if (story != null) {
            json.addProperty("popularity", story.getPopularity());
        }
        return json;
    }
}
