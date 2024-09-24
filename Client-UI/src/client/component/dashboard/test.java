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


            Sheet sheet = logic.getMainSheet().get(0);
            SheetDTO sheetDTO = new ImplSheetDTO(sheet);
            String jsonSheet = gson.toJson(sheetDTO);
            System.out.println(jsonSheet);
            SheetDTO sheet2 = gson.fromJson(jsonSheet, SheetDTO.class);
            System.out.println(sheet2.getSheetName());

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
