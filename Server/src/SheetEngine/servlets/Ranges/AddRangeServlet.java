package SheetEngine.servlets.Ranges;

import SheetEngine.utils.ServletUtils;
import SheetEngine.utils.SessionUtils;
import body.Coordinate;
import body.Sheets.SheetsManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import static SheetEngine.constants.Constants.GSON_INSTANCE;
import static SheetEngine.utils.ServletUtils.getSheetManager;

@WebServlet(name = "AddRangeServlet", urlPatterns = {"/range/add"})
public class AddRangeServlet extends HttpServlet {

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
            String topLeft = request.getParameter("topLeft");
            String bottomRight = request.getParameter("bottomRight");
            synchronized (this) {
                if (sheetsManager.isSheetExists(sheetName)) {
                    try {
                        Set<Coordinate> rangeCoordinates = sheetsManager.getSheet(sheetName).addRangeToSheet(rangeName, topLeft, bottomRight);
                        String json = GSON_INSTANCE.toJson(rangeCoordinates);
                        out.println(json);
                        out.flush();
                        //getServletContext().getRequestDispatcher("/sheetslist/sheet").include(request, response);

                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.println(e.getMessage());
                        out.flush();
                        //response.getOutputStream().print(e.getMessage());
                    }
                }
            }
        }
    }
}
