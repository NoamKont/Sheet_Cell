package SheetEngine.servlets;


import SheetEngine.utils.ServletUtils;
import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import body.Sheets.SheetsManager;
import body.impl.CoordinateImpl;
import body.impl.Graph;
import body.impl.ImplCell;
import body.users.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.Range.api.Range;
import expression.Range.impl.RangeImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.serializer.*;
import utils.deserializer.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "SheetsListServlet", urlPatterns = {"/sheetslist"})
public class SheetsListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            //Gson gson = new Gson();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(CoordinateImpl.class, new CoordinateSerializer())
                    .registerTypeAdapter(Coordinate.class, new CoordinateDeserializer())
                    .registerTypeAdapter(ImplCell.class, new CellSerializer())
                    .registerTypeAdapter(Cell.class, new CellDeserializer())
                    .registerTypeAdapter(Logic.class, new LogicDeserializer())
                    .registerTypeAdapter(RangeImpl.class,new RangeSerializer())
                    .registerTypeAdapter(Range.class, new RangeDeserializer())
                    .registerTypeAdapter(Graph.class, new GraphSerializer())
                    .registerTypeAdapter(CellDTO.class, new DTOCellDeserializer())
                    .registerTypeAdapter(SheetDTO.class, new DTOSheetDeserializer())
                    .create();
            SheetsManager sheetManager = ServletUtils.getSheetManager(getServletContext());
            Set<Logic> sheetList = sheetManager.getSheets();
            String json = gson.toJson(sheetList);
            out.println(json);
            out.flush();
        }
    }

}
