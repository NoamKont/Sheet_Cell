package SheetEngine.servlets;

import SheetEngine.utils.ServletUtils;
import SheetEngine.utils.SessionUtils;
import body.Logic;
import body.Sheets.SheetsManager;
import body.impl.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import expression.Range.impl.RangeImpl;
import expression.impl.Str;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.serializer.*;


import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UpdateSheetServlet", urlPatterns = {"/sheetslist/sheet/updateCell"})
public class UpdateCellServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");

            String username = SessionUtils.getUsername(request);
            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            SheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());

            String cellId = request.getParameter("cellId");
            String value = request.getParameter("value");
            String sheetName = request.getParameter("sheetName");
            synchronized (this) {
                if (sheetsManager.isSheetExists(sheetName)) {
                    Logic sheetEngine = sheetsManager.getSheet(sheetName);
                    try {
                        sheetEngine.updateCell(cellId, value);
                        getServletContext().getRequestDispatcher("/sheetslist/sheet").include(request, response);

                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.println(e.getMessage());
                        out.flush();
                    }
                }
            }
        }
    }
}
