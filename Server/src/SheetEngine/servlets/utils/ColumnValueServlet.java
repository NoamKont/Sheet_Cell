package SheetEngine.servlets.utils;

import SheetEngine.utils.SessionUtils;
import body.Sheets.SheetsManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static SheetEngine.constants.Constants.GSON_INSTANCE;
import static SheetEngine.utils.ServletUtils.getSheetManager;

@WebServlet(name = "ColumnValueServlet", urlPatterns = {"/utils/getValuesFromColumn"})
public class ColumnValueServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String usernameFromSession = SessionUtils.getUsername(request);
        if (usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String sheetName = request.getParameter("sheetName");
        String column = request.getParameter("columnIndex");
        String top = request.getParameter("top");
        String bottom = request.getParameter("bottom");

        SheetsManager sheetManager = getSheetManager(getServletContext());

        try {
            List<String> columnValues = sheetManager.getSheet(sheetName).getSheet().getValuesFromColumn(Integer.parseInt(column), Integer.parseInt(top), Integer.parseInt(bottom));
            String columnValuesJson = GSON_INSTANCE.toJson(columnValues);
            response.setStatus(HttpServletResponse.SC_OK);
            out.println(columnValuesJson);
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(e.getMessage());
            out.flush();
        }
    }
}
