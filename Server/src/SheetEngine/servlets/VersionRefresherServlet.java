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

@WebServlet(name = "VersionRefresherSheetServlet", urlPatterns = {"/simultaneity"})
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
                        //TODO solve the problem below.

                        /* problem that if I send to server that my version is 1 and
                        after millisecond I send update cell.
                        the server will go to update cell, will do its logic make the version 2
                        send back to the client the new version 2,
                        after if the server came back to the version refresher servlet
                        the version will be 1 and the client will be 2 but still not updated
                        and the client that update the cell will get message that the version
                         he has is not updated
                        */

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
