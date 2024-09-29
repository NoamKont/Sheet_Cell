package SheetEngine.servlets.utils;

import SheetEngine.utils.SessionUtils;
import body.Sheets.SheetsManager;
import com.google.gson.reflect.TypeToken;
import dto.SheetDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static SheetEngine.constants.Constants.GSON_INSTANCE;
import static SheetEngine.utils.ServletUtils.getSheetManager;

@WebServlet(name = "FilterSheetServlet", urlPatterns = {"/utils/filter"})
public class FilterSheetServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String usernameFromSession = SessionUtils.getUsername(request);
        if (usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String sheetName = request.getParameter("sheetName");
        String topLeft = request.getParameter("topLeft");
        String bottomRight = request.getParameter("bottomRight");
        String values = request.getParameter("values");
        String columns = request.getParameter("columns");
        String[] columnsArray = GSON_INSTANCE.fromJson(columns, String[].class);
        List<List<String>> valuesList = GSON_INSTANCE.fromJson(values, new TypeToken<List<List<String>>>(){}.getType());
        SheetsManager sheetManager = getSheetManager(getServletContext());
        try {
            SheetDTO filterSheet = sheetManager.getSheet(sheetName).filterSheet(topLeft, bottomRight, valuesList, List.of(columnsArray));
            response.setStatus(HttpServletResponse.SC_OK);
            String sortedSheetJson = GSON_INSTANCE.toJson(filterSheet);
            out.println(sortedSheetJson);
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(e.getMessage());
            out.flush();
        }
    }
}
