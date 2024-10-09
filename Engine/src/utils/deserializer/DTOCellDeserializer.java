package utils.deserializer;

import body.Cell;
import body.Coordinate;
import body.impl.ImplCell;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dto.impl.CellDTO;

import java.lang.reflect.Type;
import java.util.List;

public class DTOCellDeserializer implements JsonDeserializer<CellDTO> {

    @Override
    public CellDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        CellDTO res = context.deserialize(json, CellDTO.class);
        // Assuming you want to deserialize into ImplLogic
        return res;
    }
}
