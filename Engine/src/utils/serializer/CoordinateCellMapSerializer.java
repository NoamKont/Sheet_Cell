package utils.serializer;

import body.Cell;
import body.Coordinate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

public class CoordinateCellMapSerializer implements JsonSerializer<Map<Coordinate, Cell>> {

    @Override
    public JsonElement serialize(Map<Coordinate, Cell> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        // Iterate through the map and manually add each entry
        for (Map.Entry<Coordinate, Cell> entry : src.entrySet()) {
            // Serialize the Coordinate as a JSON object
            JsonElement coordJson = context.serialize(entry.getKey());

            // Serialize the Cell
            JsonElement cellJson = context.serialize(entry.getValue());

            // Add the serialized Coordinate and Cell to the result JSON
            jsonObject.add(coordJson.toString(), cellJson); // Use the serialized Coordinate as key
        }

        return jsonObject;
    }
}
