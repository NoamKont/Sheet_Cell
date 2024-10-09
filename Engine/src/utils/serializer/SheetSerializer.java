package utils.serializer;

import body.Sheet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class SheetSerializer implements JsonSerializer<Sheet> {
    @Override
    public JsonElement serialize(Sheet src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sheetVersion", src.getVersion());
        jsonObject.addProperty("sheetName", src.getSheetName());
        jsonObject.addProperty("thickness", src.getThickness());
        jsonObject.addProperty("width", src.getWidth());
        jsonObject.addProperty("row", src.getRowCount());
        jsonObject.addProperty("col", src.getColumnCount());
        jsonObject.add("activeCells", context.serialize(src.getActiveCells()));
        jsonObject.add("graph", context.serialize(src.getGraph()));
        jsonObject.addProperty("countUpdateCell", src.getCountUpdateCell());
        jsonObject.add("rangeMap", context.serialize(src.getAllRanges()));
        jsonObject.addProperty("username", src.getUsername());

        return jsonObject;
    }
}
