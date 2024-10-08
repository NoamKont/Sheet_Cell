package utils.serializer;

import body.Cell;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dto.RangeDTO;
import expression.Range.api.Range;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class RangeSerializer implements JsonSerializer<RangeDTO> {
    @Override
    public JsonElement serialize(RangeDTO src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("rangeName", src.getRangeName());
        jsonObject.addProperty("topLeftCellId", src.getTopLeftCellId());
        jsonObject.addProperty("rightBottomCellId", src.getRightBottomCellId());
        jsonObject.add("rangeCells", context.serialize(src.getRangeCells()));

        return jsonObject;
    }
}
