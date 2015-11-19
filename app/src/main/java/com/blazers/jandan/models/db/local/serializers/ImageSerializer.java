package com.blazers.jandan.models.db.local.serializers;

import com.blazers.jandan.models.db.local.LocalFavImages;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Blazers on 2015/11/19.
 */
public class ImageSerializer implements JsonSerializer<LocalFavImages> {
    @Override
    public JsonElement serialize(LocalFavImages src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("url", src.getUrl());
        jsonObject.addProperty("favTime", src.getFavTime());
        return jsonObject;
    }
}
