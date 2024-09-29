package SheetEngine.servlets;

import SheetEngine.utils.ServletUtils;
import SheetEngine.utils.SessionUtils;
import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheets.SheetsManager;
import body.impl.CoordinateImpl;
import body.impl.Graph;
import body.impl.ImplCell;
import body.impl.ImplSheet;
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
import utils.deserializer.*;
import utils.serializer.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import static SheetEngine.constants.Constants.GSON_INSTANCE;

@WebServlet(name = "VersionSheetServlet", urlPatterns = {"/sheetslist/sheet/version"})
public class VersionSheetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");

            SheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
            String username = SessionUtils.getUsername(request);
            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            String sheetName = request.getParameter("sheetName");
            String version = request.getParameter("version");
            synchronized (this){
                if(sheetsManager.isSheetExists(sheetName)){
                    Logic sheetEngine = sheetsManager.getSheet(sheetName);
                    try{
                        SheetDTO sheet = sheetEngine.getSheetbyVersion(Integer.parseInt(version));
                        String json = GSON_INSTANCE.toJson(sheet);
                        out.println(json);
                        out.flush();
                    }catch (Exception e){
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.println(e.getMessage());
                        out.flush();
                    }
                }
                else{
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }
}
