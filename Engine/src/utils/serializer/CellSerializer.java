package utils.serializer;

import body.Cell;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CellSerializer implements JsonSerializer<Cell> {

    @Override
    public JsonElement serialize(Cell src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("coor", context.serialize(src.getCoordinate()));  // Ensure coordinate is serialized correctly
        jsonObject.addProperty("Id", src.getId());
        jsonObject.addProperty("lastVersionUpdate", src.getLastVersionUpdate());
        jsonObject.addProperty("originalValue", src.getOriginalValue());
        jsonObject.add("expression", context.serialize(src.getExpression()));  // Handle expression serialization
        jsonObject.add("effectiveValue", context.serialize(src.getEffectiveValue()));  // Handle effectiveValue
        jsonObject.add("cellsDependsOnThem", context.serialize(src.getCellsDependsOnThem()));  // Serialize the list
        jsonObject.add("cellsDependsOnHim", context.serialize(src.getCellsDependsOnHim()));  // Serialize the list
        jsonObject.addProperty("updateBy", src.getUpdateBy());
        return jsonObject;
    }
}
