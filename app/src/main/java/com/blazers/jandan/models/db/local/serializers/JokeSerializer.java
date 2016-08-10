package com.blazers.jandan.models.db.local.serializers;

import com.blazers.jandan.models.db.local.LocalFavJokes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Blazers on 2015/11/19.
 */
public class JokeSerializer implements JsonSerializer<LocalFavJokes> {

    @Override
    public JsonElement serialize(LocalFavJokes src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comment_ID", src.getComment_ID());
        jsonObject.addProperty("favTime", src.getFavTime());
        return jsonObject;
    }
}
