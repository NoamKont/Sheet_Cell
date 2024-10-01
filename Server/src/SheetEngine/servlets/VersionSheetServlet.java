package SheetEngine.servlets;

import SheetEngine.utils.ServletUtils;
import SheetEngine.utils.SessionUtils;

import body.Logic;
import body.Sheets.SheetsManager;
import dto.SheetDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


import static SheetEngine.constants.Constants.GSON_INSTANCE;

@WebServlet(name = "VersionSheetServlet", urlPatterns = {"/sheetslist/sheet/version"})
public class VersionSheetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects
        System.out.println("VersionSheetServlet: doGet");
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
