package SheetEngine.constants;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import body.impl.CoordinateImpl;
import body.impl.Graph;
import body.impl.ImplCell;
import body.impl.ImplSheet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.RangeDTO;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.ImplRangeDTO;
import expression.Range.api.Range;
import expression.Range.impl.RangeImpl;
import utils.deserializer.*;
import utils.serializer.*;

public class Constants {
    public static final String USERNAME = "username";

    
    public static final int INT_PARAMETER_ERROR = Integer.MIN_VALUE;

    // GSON instance
    public final static Gson GSON_INSTANCE = new GsonBuilder()
            .registerTypeAdapter(CoordinateImpl.class, new CoordinateSerializer())
            .registerTypeAdapter(Coordinate.class, new CoordinateDeserializer())
            .registerTypeAdapter(ImplCell.class, new CellSerializer())
            .registerTypeAdapter(Cell.class, new CellDeserializer())
            .registerTypeAdapter(ImplSheet.class, new SheetSerializer())
            .registerTypeAdapter(Sheet.class, new SheetDeserializer())
            .registerTypeAdapter(Logic.class, new LogicDeserializer())
            .registerTypeAdapter(Range.class, new RangeDeserializer())
            .registerTypeAdapter(Graph.class, new GraphSerializer())
            .registerTypeAdapter(CellDTO.class, new DTOCellDeserializer())
            .registerTypeAdapter(SheetDTO.class, new DTOSheetDeserializer())
            .registerTypeAdapter(RangeDTO.class, new DTORangeDeserializer())
            .registerTypeAdapter(ImplRangeDTO.class,new RangeSerializer())
            .create();
}