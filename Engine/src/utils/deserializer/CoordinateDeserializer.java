package utils.deserializer;

import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import body.impl.ImplSheet;
import com.google.gson.*;

import java.lang.reflect.Type;

public class CoordinateDeserializer implements JsonDeserializer<Coordinate> {

    @Override
    public Coordinate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Assuming you want to deserialize into ImplLogic
        try{
            CoordinateImpl coordinate = new CoordinateImpl(json.getAsString());
            return coordinate;

        }catch (Exception e){
            return context.deserialize(json, CoordinateImpl.class);
        }
    }
}
