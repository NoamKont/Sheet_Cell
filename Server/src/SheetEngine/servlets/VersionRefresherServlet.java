package SheetEngine.servlets;

import SheetEngine.utils.SessionUtils;
import body.Logic;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import SheetEngine.utils.ServletUtils;
import body.Sheets.SheetsManager;
import dto.SheetDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static SheetEngine.constants.Constants.*;

@WebServlet(name = "VersionSheetServlet", urlPatterns = {"/simultaneity"})
public class VersionRefresherServlet extends HttpServlet {

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
            String version = request.getParameter("version");
            synchronized (this){
                if(sheetsManager.isSheetExists(sheetName)){
                    Logic sheetEngine = sheetsManager.getSheet(sheetName);
                    SheetDTO sheet = sheetEngine.getSheet();
                    if(sheet.getVersion() > Integer.parseInt(version)){
                        response.setStatus(HttpServletResponse.SC_OK);
                        String json = GSON_INSTANCE.toJson(sheet);
                        out.println(json);
                        out.flush();
                    }
                    else{
                        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    }
                }
                else{
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }
}
