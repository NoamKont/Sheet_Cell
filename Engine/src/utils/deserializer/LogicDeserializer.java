package utils.deserializer;

import body.Logic;
import body.impl.ImplLogic;
import com.google.gson.*;

import java.lang.reflect.Type;

public class LogicDeserializer implements JsonDeserializer<Logic> {

    @Override
    public Logic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Logic logic = context.deserialize(json, ImplLogic.class);
        // Assuming you want to deserialize into ImplLogic
        return logic;
    }
}
