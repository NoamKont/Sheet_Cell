package utils.deserializer;

import body.Sheet;
import body.impl.CoordinateImpl;
import body.impl.ImplSheet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dto.SheetDTO;
import dto.impl.ImplSheetDTO;

import java.lang.reflect.Type;

public class DTOSheetDeserializer implements JsonDeserializer<SheetDTO> {

    @Override
    public SheetDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SheetDTO sheetDTO = context.deserialize(json, ImplSheetDTO.class);
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


        System.out.println("SheetDTO: " + sheetDTO);
        // Assuming you want to deserialize into ImplLogic
        return sheetDTO;
    }
}
