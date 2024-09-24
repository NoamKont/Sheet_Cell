package utils.deserializer;

import body.Logic;
import body.impl.ImplLogic;
import com.google.gson.*;

import java.lang.reflect.Type;

public class LogicDeserializer implements JsonDeserializer<Logic> {

    @Override
    public Logic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        System.out.println("Enter LogicDeserializer");
        Logic logic = context.deserialize(json, ImplLogic.class);
        System.out.println("Print Sheet name "+logic.getMainSheet().get(0).getSheetName());
        // Assuming you want to deserialize into ImplLogic
        return logic;
    }
}
