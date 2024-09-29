package utils.deserializer;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import body.impl.ImplSheet;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import expression.Range.api.Range;

import java.lang.reflect.Type;
import java.util.Map;

public class SheetDeserializer implements JsonDeserializer<Sheet> {

    @Override
    public Sheet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ImplSheet res = context.deserialize(json, ImplSheet.class);

        int sheetVersion = res.getVersion() - 1 ;
        res.getActiveCells().entrySet().forEach(entry -> {
            res.setVersion(sheetVersion);
            res.updateCellDitels(entry.getKey().toString(), entry.getValue().getOriginalValue());
            res.updateListsOfDependencies(entry.getKey());
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
