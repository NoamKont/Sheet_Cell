package utils.deserializer;


import com.google.gson.*;
import dto.SheetDTO;
import dto.impl.ImplSheetDTO;


import java.lang.reflect.Type;

public class DTOSheetDeserializer implements JsonDeserializer<SheetDTO> {

    @Override
    public SheetDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        SheetDTO res = context.deserialize(json, ImplSheetDTO.class);

        return res;
    }
}
