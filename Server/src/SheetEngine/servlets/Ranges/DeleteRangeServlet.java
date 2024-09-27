package SheetEngine.servlets.Ranges;

import SheetEngine.utils.ServletUtils;
import SheetEngine.utils.SessionUtils;
import body.Sheets.SheetsManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DeleteRangeServlet", urlPatterns = {"/range/delete"})
public class DeleteRangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            String username = SessionUtils.getUsername(request);
            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            SheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());

            String sheetName = request.getParameter("sheetName");
            String rangeName = request.getParameter("rangeName");
            synchronized (this) {
                if (sheetsManager.isSheetExists(sheetName)) {
                    try {
                        sheetsManager.getSheet(sheetName).deleteRange(rangeName);
                        //getServletContext().getRequestDispatcher("/sheetslist/sheet").include(request, response);

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
