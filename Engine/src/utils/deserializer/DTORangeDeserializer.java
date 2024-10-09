package utils.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dto.RangeDTO;
import dto.impl.ImplRangeDTO;

import java.lang.reflect.Type;

public class DTORangeDeserializer implements JsonDeserializer<RangeDTO> {
    @Override
    public RangeDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        RangeDTO res = context.deserialize(json, ImplRangeDTO.class);
        return res;
    }
}
