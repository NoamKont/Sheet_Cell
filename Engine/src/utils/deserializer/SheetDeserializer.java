package utils.deserializer;

import body.Sheet;
import body.impl.ImplSheet;
import com.google.gson.*;

import java.lang.reflect.Type;

public class SheetDeserializer implements JsonDeserializer<Sheet> {

    @Override
    public Sheet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ImplSheet res = context.deserialize(json, ImplSheet.class);
        int sheetVersion = res.getVersion() - 1 ;

        res.getActiveCells().entrySet().forEach(entry -> {
            res.setVersion(sheetVersion);
            res.updateCellDetails(entry.getKey().toString(), entry.getValue().getOriginalValue());
        });
        res.updateCellEffectiveValue("Z1");

        res.getAllRanges().entrySet().forEach(entry -> {
            entry.getValue().setSheet(res);
            entry.getValue().setRangeCells();
        });




        // Assuming you want to deserialize into ImplLogic
        return res;
    }
}
