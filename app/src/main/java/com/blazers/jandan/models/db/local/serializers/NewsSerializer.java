package com.blazers.jandan.models.db.local.serializers;

import com.blazers.jandan.models.db.local.LocalFavNews;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Blazers on 2015/11/19.
 */
public class NewsSerializer implements JsonSerializer<LocalFavNews>{

    @Override
    public JsonElement serialize(LocalFavNews src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("favTime", src.getFavTime());
        return jsonObject;
    }
}
