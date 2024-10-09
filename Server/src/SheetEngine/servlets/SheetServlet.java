package SheetEngine.servlets;


import SheetEngine.constants.Constants;
import body.Logic;
import body.Sheet;
import body.impl.CoordinateImpl;
import body.impl.Graph;
import body.impl.ImplCell;
import body.impl.ImplSheet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.SheetDTO;
import expression.Range.impl.RangeImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import SheetEngine.utils.ServletUtils;
import SheetEngine.utils.SessionUtils;
import body.Sheets.SheetsManager;
import utils.serializer.*;

import java.io.IOException;
import java.io.PrintWriter;

import static SheetEngine.constants.Constants.*;


@WebServlet(name = "GetSheetServlet", urlPatterns = {"/sheetslist/sheet"})
public class SheetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            SheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
            String username = SessionUtils.getUsername(request);
            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            String sheetName = request.getParameter("sheetName");
            synchronized (this){
                if(sheetsManager.isSheetExists(sheetName)){
                    Logic sheetEngine = sheetsManager.getSheet(sheetName);
                    SheetDTO sheet = sheetEngine.getSheet();
                    String json = GSON_INSTANCE.toJson(sheet);
                    out.println(json);
                    out.flush();
                }
                else{
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }

}
