package utils.deserializer;

import com.google.gson.*;
import expression.Range.api.Range;
import expression.Range.impl.RangeImpl;

import java.lang.reflect.Type;

public class RangeDeserializer implements JsonDeserializer<Range> {

    @Override
    public Range deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Range range = context.deserialize(json, RangeImpl.class);
//        String rangeName = json.getAsJsonObject().get("rangeName").getAsString();
//        String topLeftCellId = json.getAsJsonObject().get("topLeftCellId").getAsString();
//        String rightBottomCellId = json.getAsJsonObject().get("rightBottomCellId").getAsString();
//        Range range = new RangeImpl(rangeName, topLeftCellId, rightBottomCellId);
        return range;
    }
}
