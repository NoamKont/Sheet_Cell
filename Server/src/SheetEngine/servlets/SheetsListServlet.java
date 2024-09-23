package SheetEngine.servlets;


import SheetEngine.utils.ServletUtils;
import body.Logic;
import body.Sheet;
import body.Sheets.SheetsManager;
import body.users.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "SheetsListServlet", urlPatterns = {"/sheetslist"})
public class SheetsListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            SheetsManager sheetManager = ServletUtils.getSheetManager(getServletContext());
            Set<Logic> sheetList = sheetManager.getSheets();
            String json = gson.toJson(sheetList);
            out.println(json);
            out.flush();
        }
    }

}
