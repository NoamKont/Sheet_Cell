package SheetEngine.servlets.utils;

import SheetEngine.utils.SessionUtils;

import body.Sheets.SheetsManager;
import dto.SheetDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static SheetEngine.constants.Constants.GSON_INSTANCE;
import static SheetEngine.utils.ServletUtils.getSheetManager;

@WebServlet(name = "SortedSheetServlet", urlPatterns = {"/utils/sort"})
public class SortSheetServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            String usernameFromSession = SessionUtils.getUsername(request);
            if (usernameFromSession == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String sheetName = request.getParameter("sheetName");
            String topLeft = request.getParameter("topLeft");
            String bottomRight = request.getParameter("bottomRight");
            String columns = request.getParameter("columns");
            SheetsManager sheetManager = getSheetManager(getServletContext());
            try {
                SheetDTO sortedSheet = sheetManager.getSheet(sheetName).sortSheet(topLeft, bottomRight, columns);
                response.setStatus(HttpServletResponse.SC_OK);
                String sortedSheetJson = GSON_INSTANCE.toJson(sortedSheet);
                out.println(sortedSheetJson);
                out.flush();
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(e.getMessage());
                out.flush();
            }

        }
    }
}
