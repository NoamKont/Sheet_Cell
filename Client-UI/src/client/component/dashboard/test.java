package client.component.dashboard;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import body.impl.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.ImplSheetDTO;
import expression.Range.api.Range;
import expression.Range.impl.RangeImpl;
import jakarta.xml.bind.JAXBException;

import utils.deserializer.*;
import utils.serializer.*;

import java.io.IOException;

public class test {
    public static void main(String[] args) {
        Logic logic = new ImplLogic();
        Logic res;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CoordinateImpl.class, new CoordinateSerializer())
                .registerTypeAdapter(Coordinate.class, new CoordinateDeserializer())
                .registerTypeAdapter(ImplCell.class, new CellSerializer())
                .registerTypeAdapter(Cell.class, new CellDeserializer())
                .registerTypeAdapter(ImplSheet.class, new SheetSerializer())
                .registerTypeAdapter(Sheet.class, new SheetDeserializer())
                .registerTypeAdapter(Logic.class, new LogicDeserializer())
                .registerTypeAdapter(RangeImpl.class,new RangeSerializer())
                .registerTypeAdapter(Range.class, new RangeDeserializer())
                //.registerTypeAdapter(Graph.class, new GraphDeserializer())
                .registerTypeAdapter(Graph.class, new GraphSerializer())
                .registerTypeAdapter(CellDTO.class, new DTOCellDeserializer())
                .registerTypeAdapter(SheetDTO.class, new DTOSheetDeserializer())

                .setPrettyPrinting()
                .create();
        try{
            logic.creatNewSheet("C:\\Users\\Noam\\Downloads\\Ex2 example\\insurance.xml");

//            List<Coordinate> coordinateList = new ArrayList<>();
//            coordinateList.add(new CoordinateImpl(1, 2));
//            coordinateList.add(new CoordinateImpl(3, 4));
//
//            String jsonList = gson.toJson(coordinateList);  // Serialize the list
//            System.out.println(jsonList);
//
//
//            Coordinate c = new CoordinateImpl("A1");
//            String jsonC = gson.toJson(c);
//            System.out.println("TO "+jsonC);
//            Coordinate c2 = gson.fromJson(jsonC, Coordinate.class);
//            System.out.println("FROM " + c2);

//            Cell cell = logic.getMainSheet().get(0).getCell("E4");
//            CellDTO cellDTO = new CellDTO(cell);
//
//            String jsonCell = gson.toJson(cellDTO);
//            System.out.println("TO " + jsonCell);
//            CellDTO cell2 = gson.fromJson(jsonCell, CellDTO.class);
//            System.out.println("FROM " + cell2);

            String input = "{\"currSheet\":{\"sheetVersion\":1,\"sheetName\":\"Car Insurance\",\"thickness\":30,\"width\":100,\"row\":7,\"col\":7,\"activeCells\":{\"B5\":{\"coor\":{\"row\":5,\"column\":2},\"Id\":\"B5\",\"lastVersionUpdate\":1,\"originalValue\":\"WOBI\",\"expression\":{\"string\":\"WOBI\",\"isUndifined\":false},\"effectiveValue\":{\"string\":\"WOBI\",\"isUndifined\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[]},\"B6\":{\"coor\":{\"row\":6,\"column\":2},\"Id\":\"B6\",\"lastVersionUpdate\":1,\"originalValue\":\"SHIRBIT\",\"expression\":{\"string\":\"SHIRBIT\",\"isUndifined\":false},\"effectiveValue\":{\"string\":\"SHIRBIT\",\"isUndifined\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[]},\"C5\":{\"coor\":{\"row\":5,\"column\":3},\"Id\":\"C5\",\"lastVersionUpdate\":1,\"originalValue\":\"900\",\"expression\":{\"num\":900.0,\"isNan\":false},\"effectiveValue\":{\"num\":900.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":5,\"column\":5}]},\"C6\":{\"coor\":{\"row\":6,\"column\":3},\"Id\":\"C6\",\"lastVersionUpdate\":1,\"originalValue\":\"5555\",\"expression\":{\"num\":5555.0,\"isNan\":false},\"effectiveValue\":{\"num\":5555.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":6,\"column\":5}]},\"D5\":{\"coor\":{\"row\":5,\"column\":4},\"Id\":\"D5\",\"lastVersionUpdate\":1,\"originalValue\":\"1100\",\"expression\":{\"num\":1100.0,\"isNan\":false},\"effectiveValue\":{\"num\":1100.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":5,\"column\":5}]},\"D6\":{\"coor\":{\"row\":6,\"column\":4},\"Id\":\"D6\",\"lastVersionUpdate\":1,\"originalValue\":\"4444\",\"expression\":{\"num\":4444.0,\"isNan\":false},\"effectiveValue\":{\"num\":4444.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":6,\"column\":5}]},\"E5\":{\"coor\":{\"row\":5,\"column\":5},\"Id\":\"E5\",\"lastVersionUpdate\":1,\"originalValue\":\"{PLUS, {REF,C5}, {REF,D5}}\",\"expression\":{\"expression1\":{\"cell\":{\"coor\":{\"row\":5,\"column\":3},\"Id\":\"C5\",\"lastVersionUpdate\":1,\"originalValue\":\"900\",\"expression\":{\"num\":900.0,\"isNan\":false},\"effectiveValue\":{\"num\":900.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":5,\"column\":5}]},\"expression1\":{\"string\":\"C5\",\"isUndifined\":false}},\"expression2\":{\"cell\":{\"coor\":{\"row\":5,\"column\":4},\"Id\":\"D5\",\"lastVersionUpdate\":1,\"originalValue\":\"1100\",\"expression\":{\"num\":1100.0,\"isNan\":false},\"effectiveValue\":{\"num\":1100.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":5,\"column\":5}]},\"expression1\":{\"string\":\"D5\",\"isUndifined\":false}}},\"effectiveValue\":{\"num\":2000.0,\"isNan\":false},\"cellsDependsOnThem\":[{\"row\":5,\"column\":3},{\"row\":5,\"column\":4}],\"cellsDependsOnHim\":[]},\"E6\":{\"coor\":{\"row\":6,\"column\":5},\"Id\":\"E6\",\"lastVersionUpdate\":1,\"originalValue\":\"{PLUS, {REF,C6}, {REF,D6}}\",\"expression\":{\"expression1\":{\"cell\":{\"coor\":{\"row\":6,\"column\":3},\"Id\":\"C6\",\"lastVersionUpdate\":1,\"originalValue\":\"5555\",\"expression\":{\"num\":5555.0,\"isNan\":false},\"effectiveValue\":{\"num\":5555.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":6,\"column\":5}]},\"expression1\":{\"string\":\"C6\",\"isUndifined\":false}},\"expression2\":{\"cell\":{\"coor\":{\"row\":6,\"column\":4},\"Id\":\"D6\",\"lastVersionUpdate\":1,\"originalValue\":\"4444\",\"expression\":{\"num\":4444.0,\"isNan\":false},\"effectiveValue\":{\"num\":4444.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":6,\"column\":5}]},\"expression1\":{\"string\":\"D6\",\"isUndifined\":false}}},\"effectiveValue\":{\"num\":9999.0,\"isNan\":false},\"cellsDependsOnThem\":[{\"row\":6,\"column\":3},{\"row\":6,\"column\":4}],\"cellsDependsOnHim\":[]},\"D3\":{\"coor\":{\"row\":3,\"column\":4},\"Id\":\"D3\",\"lastVersionUpdate\":1,\"originalValue\":\"3rd party\",\"expression\":{\"string\":\"3rd party\",\"isUndifined\":false},\"effectiveValue\":{\"string\":\"3rd party\",\"isUndifined\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[]},\"B4\":{\"coor\":{\"row\":4,\"column\":2},\"Id\":\"B4\",\"lastVersionUpdate\":1,\"originalValue\":\"AIG\",\"expression\":{\"string\":\"AIG\",\"isUndifined\":false},\"effectiveValue\":{\"string\":\"AIG\",\"isUndifined\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[]},\"C4\":{\"coor\":{\"row\":4,\"column\":3},\"Id\":\"C4\",\"lastVersionUpdate\":1,\"originalValue\":\"1000\",\"expression\":{\"num\":1000.0,\"isNan\":false},\"effectiveValue\":{\"num\":1000.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":4,\"column\":5}]},\"D4\":{\"coor\":{\"row\":4,\"column\":4},\"Id\":\"D4\",\"lastVersionUpdate\":1,\"originalValue\":\"800\",\"expression\":{\"num\":800.0,\"isNan\":false},\"effectiveValue\":{\"num\":800.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":4,\"column\":5}]},\"E4\":{\"coor\":{\"row\":4,\"column\":5},\"Id\":\"E4\",\"lastVersionUpdate\":1,\"originalValue\":\"{PLUS, {REF,C4}, {REF,D4}}\",\"expression\":{\"expression1\":{\"cell\":{\"coor\":{\"row\":4,\"column\":3},\"Id\":\"C4\",\"lastVersionUpdate\":1,\"originalValue\":\"1000\",\"expression\":{\"num\":1000.0,\"isNan\":false},\"effectiveValue\":{\"num\":1000.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":4,\"column\":5}]},\"expression1\":{\"string\":\"C4\",\"isUndifined\":false}},\"expression2\":{\"cell\":{\"coor\":{\"row\":4,\"column\":4},\"Id\":\"D4\",\"lastVersionUpdate\":1,\"originalValue\":\"800\",\"expression\":{\"num\":800.0,\"isNan\":false},\"effectiveValue\":{\"num\":800.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":4,\"column\":5}]},\"expression1\":{\"string\":\"D4\",\"isUndifined\":false}}},\"effectiveValue\":{\"num\":1800.0,\"isNan\":false},\"cellsDependsOnThem\":[{\"row\":4,\"column\":3},{\"row\":4,\"column\":4}],\"cellsDependsOnHim\":[]},\"C3\":{\"coor\":{\"row\":3,\"column\":3},\"Id\":\"C3\",\"lastVersionUpdate\":1,\"originalValue\":\"mandatory\",\"expression\":{\"string\":\"mandatory\",\"isUndifined\":false},\"effectiveValue\":{\"string\":\"mandatory\",\"isUndifined\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[]},\"E3\":{\"coor\":{\"row\":3,\"column\":5},\"Id\":\"E3\",\"lastVersionUpdate\":1,\"originalValue\":\"Total\",\"expression\":{\"string\":\"Total\",\"isUndifined\":false},\"effectiveValue\":{\"string\":\"Total\",\"isUndifined\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[]}},\"graph\":{\"graph\":{\"B5\":[],\"B6\":[],\"C5\":[{\"row\":5,\"column\":5}],\"C6\":[{\"row\":6,\"column\":5}],\"D5\":[{\"row\":5,\"column\":5}],\"D6\":[{\"row\":6,\"column\":5}],\"E5\":[],\"E6\":[],\"D3\":[],\"B4\":[],\"C4\":[{\"row\":4,\"column\":5}],\"D4\":[{\"row\":4,\"column\":5}],\"E4\":[],\"C3\":[],\"E3\":[]},\"graph_T\":{\"B5\":[],\"B6\":[],\"C5\":[],\"C6\":[],\"D5\":[],\"D6\":[],\"E5\":[{\"row\":5,\"column\":3},{\"row\":5,\"column\":4}],\"E6\":[{\"row\":6,\"column\":3},{\"row\":6,\"column\":4}],\"D3\":[],\"B4\":[],\"C4\":[],\"D4\":[],\"E4\":[{\"row\":4,\"column\":3},{\"row\":4,\"column\":4}],\"C3\":[],\"E3\":[]}},\"countUpdateCell\":15,\"rangeMap\":{\"grades\":{\"rangeName\":\"grades\",\"topLeftCellId\":\"B4\",\"rightBottomCellId\":\"E6\",\"rangeCells\":[{\"coor\":{\"row\":6,\"column\":4},\"Id\":\"D6\",\"lastVersionUpdate\":1,\"originalValue\":\"4444\",\"expression\":{\"num\":4444.0,\"isNan\":false},\"effectiveValue\":{\"num\":4444.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":6,\"column\":5}]},{\"coor\":{\"row\":6,\"column\":3},\"Id\":\"C6\",\"lastVersionUpdate\":1,\"originalValue\":\"5555\",\"expression\":{\"num\":5555.0,\"isNan\":false},\"effectiveValue\":{\"num\":5555.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":6,\"column\":5}]},{\"coor\":{\"row\":4,\"column\":4},\"Id\":\"D4\",\"lastVersionUpdate\":1,\"originalValue\":\"800\",\"expression\":{\"num\":800.0,\"isNan\":false},\"effectiveValue\":{\"num\":800.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":4,\"column\":5}]},{\"coor\":{\"row\":6,\"column\":5},\"Id\":\"E6\",\"lastVersionUpdate\":1,\"originalValue\":\"{PLUS, {REF,C6}, {REF,D6}}\",\"expression\":{\"expression1\":{\"cell\":{\"coor\":{\"row\":6,\"column\":3},\"Id\":\"C6\",\"lastVersionUpdate\":1,\"originalValue\":\"5555\",\"expression\":{\"num\":5555.0,\"isNan\":false},\"effectiveValue\":{\"num\":5555.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":6,\"column\":5}]},\"expression1\":{\"string\":\"C6\",\"isUndifined\":false}},\"expression2\":{\"cell\":{\"coor\":{\"row\":6,\"column\":4},\"Id\":\"D6\",\"lastVersionUpdate\":1,\"originalValue\":\"4444\",\"expression\":{\"num\":4444.0,\"isNan\":false},\"effectiveValue\":{\"num\":4444.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":6,\"column\":5}]},\"expression1\":{\"string\":\"D6\",\"isUndifined\":false}}},\"effectiveValue\":{\"num\":9999.0,\"isNan\":false},\"cellsDependsOnThem\":[{\"row\":6,\"column\":3},{\"row\":6,\"column\":4}],\"cellsDependsOnHim\":[]},{\"coor\":{\"row\":5,\"column\":2},\"Id\":\"B5\",\"lastVersionUpdate\":1,\"originalValue\":\"WOBI\",\"expression\":{\"string\":\"WOBI\",\"isUndifined\":false},\"effectiveValue\":{\"string\":\"WOBI\",\"isUndifined\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[]},{\"coor\":{\"row\":6,\"column\":2},\"Id\":\"B6\",\"lastVersionUpdate\":1,\"originalValue\":\"SHIRBIT\",\"expression\":{\"string\":\"SHIRBIT\",\"isUndifined\":false},\"effectiveValue\":{\"string\":\"SHIRBIT\",\"isUndifined\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[]},{\"coor\":{\"row\":5,\"column\":3},\"Id\":\"C5\",\"lastVersionUpdate\":1,\"originalValue\":\"900\",\"expression\":{\"num\":900.0,\"isNan\":false},\"effectiveValue\":{\"num\":900.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":5,\"column\":5}]},{\"coor\":{\"row\":4,\"column\":5},\"Id\":\"E4\",\"lastVersionUpdate\":1,\"originalValue\":\"{PLUS, {REF,C4}, {REF,D4}}\",\"expression\":{\"expression1\":{\"cell\":{\"coor\":{\"row\":4,\"column\":3},\"Id\":\"C4\",\"lastVersionUpdate\":1,\"originalValue\":\"1000\",\"expression\":{\"num\":1000.0,\"isNan\":false},\"effectiveValue\":{\"num\":1000.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":4,\"column\":5}]},\"expression1\":{\"string\":\"C4\",\"isUndifined\":false}},\"expression2\":{\"cell\":{\"coor\":{\"row\":4,\"column\":4},\"Id\":\"D4\",\"lastVersionUpdate\":1,\"originalValue\":\"800\",\"expression\":{\"num\":800.0,\"isNan\":false},\"effectiveValue\":{\"num\":800.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":4,\"column\":5}]},\"expression1\":{\"string\":\"D4\",\"isUndifined\":false}}},\"effectiveValue\":{\"num\":1800.0,\"isNan\":false},\"cellsDependsOnThem\":[{\"row\":4,\"column\":3},{\"row\":4,\"column\":4}],\"cellsDependsOnHim\":[]},{\"coor\":{\"row\":5,\"column\":5},\"Id\":\"E5\",\"lastVersionUpdate\":1,\"originalValue\":\"{PLUS, {REF,C5}, {REF,D5}}\",\"expression\":{\"expression1\":{\"cell\":{\"coor\":{\"row\":5,\"column\":3},\"Id\":\"C5\",\"lastVersionUpdate\":1,\"originalValue\":\"900\",\"expression\":{\"num\":900.0,\"isNan\":false},\"effectiveValue\":{\"num\":900.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":5,\"column\":5}]},\"expression1\":{\"string\":\"C5\",\"isUndifined\":false}},\"expression2\":{\"cell\":{\"coor\":{\"row\":5,\"column\":4},\"Id\":\"D5\",\"lastVersionUpdate\":1,\"originalValue\":\"1100\",\"expression\":{\"num\":1100.0,\"isNan\":false},\"effectiveValue\":{\"num\":1100.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":5,\"column\":5}]},\"expression1\":{\"string\":\"D5\",\"isUndifined\":false}}},\"effectiveValue\":{\"num\":2000.0,\"isNan\":false},\"cellsDependsOnThem\":[{\"row\":5,\"column\":3},{\"row\":5,\"column\":4}],\"cellsDependsOnHim\":[]},{\"coor\":{\"row\":4,\"column\":3},\"Id\":\"C4\",\"lastVersionUpdate\":1,\"originalValue\":\"1000\",\"expression\":{\"num\":1000.0,\"isNan\":false},\"effectiveValue\":{\"num\":1000.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":4,\"column\":5}]},{\"coor\":{\"row\":5,\"column\":4},\"Id\":\"D5\",\"lastVersionUpdate\":1,\"originalValue\":\"1100\",\"expression\":{\"num\":1100.0,\"isNan\":false},\"effectiveValue\":{\"num\":1100.0,\"isNan\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[{\"row\":5,\"column\":5}]},{\"coor\":{\"row\":4,\"column\":2},\"Id\":\"B4\",\"lastVersionUpdate\":1,\"originalValue\":\"AIG\",\"expression\":{\"string\":\"AIG\",\"isUndifined\":false},\"effectiveValue\":{\"string\":\"AIG\",\"isUndifined\":false},\"cellsDependsOnThem\":[],\"cellsDependsOnHim\":[]}]}},\"username\":\"Moshe\"}}\n";
            SheetDTO testsheetDTO = gson.fromJson(input, SheetDTO.class);

//            Sheet sheet = logic.getMainSheet().get(0);
//            String jsonSheet = gson.toJson(sheet);
//            Sheet sheet1 = gson.fromJson(jsonSheet, Sheet.class);
//
//            //SheetDTO sheetDTO = new ImplSheetDTO(sheet);
//            System.out.println(jsonSheet);
//            System.out.println(sheet2.getSheetName());

//            String json = gson.toJson(logic);
//            System.out.println(json);
//            res = gson.fromJson(json, Logic.class);
//            System.out.println(res.getSheet().getSheetName());
//            System.out.println(res.getMainSheet().size());


        }catch (JAXBException | IOException e){
            e.printStackTrace();
        }

    }
}
