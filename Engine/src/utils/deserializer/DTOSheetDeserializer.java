package utils.deserializer;

import body.Sheet;
import body.impl.CoordinateImpl;
import body.impl.ImplSheet;
import com.google.gson.*;
import dto.SheetDTO;
import dto.impl.ImplSheetDTO;
import expression.impl.Str;

import java.lang.reflect.Type;

public class DTOSheetDeserializer implements JsonDeserializer<SheetDTO> {

    @Override
    public SheetDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject sheet = json.getAsJsonObject().get("currSheet").getAsJsonObject();
        Sheet res = context.deserialize(sheet, Sheet.class);
        SheetDTO sheetDTO = new ImplSheetDTO(res);

//        SheetDTO sheetDTO = context.deserialize(json, ImplSheetDTO.class);
//        res.getActiveCells().entrySet().forEach(entry -> {
//            res.updateCellDitels(entry.getValue().getId(), entry.getValue().getOriginalValue());
//            res.updateListsOfDependencies(new CoordinateImpl(entry.getKey()));
//        });
//        res.updateCellEffectiveValue("A1");
//
//        res.getAllRanges().entrySet().forEach(entry -> {
//            entry.getValue().setSheet(res);
//            entry.getValue().setRangeCells();
//        });

        return sheetDTO;
    }
}
