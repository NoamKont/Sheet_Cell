package utils.serializer;

import body.Coordinate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CoordinateSerializer implements JsonSerializer<Coordinate> {

    @Override
    public JsonElement serialize(Coordinate src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("row", src.getRow());
        jsonObject.addProperty("column", src.getColumn());
        return jsonObject;
    }
}
