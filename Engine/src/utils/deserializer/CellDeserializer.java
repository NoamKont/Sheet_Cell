package utils.deserializer;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.impl.ImplCell;
import body.impl.ImplLogic;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CellDeserializer implements JsonDeserializer<Cell> {

    @Override
    public Cell deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ImplCell res = new ImplCell(json.getAsJsonObject().get("Id").getAsString());
        res.setLastVersionUpdate(json.getAsJsonObject().get("lastVersionUpdate").getAsInt());
        String originalValue = json.getAsJsonObject().get("originalValue").getAsString();
        res.setOriginalValue(originalValue);
        //res.setExpression(context.deserialize(json.getAsJsonObject().get("expression"), ImplLogic.class));
        //res.setEffectiveValue(context.deserialize(json.getAsJsonObject().get("effectiveValue"), ImplLogic.class));
        res.setDependsOnHim(context.deserialize(json.getAsJsonObject().get("cellsDependsOnHim"), new TypeToken<List<Coordinate>>(){}.getType()));
        res.setDependsOnThem(context.deserialize(json.getAsJsonObject().get("cellsDependsOnThem"), new TypeToken<List<Coordinate>>(){}.getType()));

        // Assuming you want to deserialize into ImplLogic
        return res;
    }
}
