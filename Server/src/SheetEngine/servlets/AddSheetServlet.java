package SheetEngine.servlets;

import SheetEngine.utils.ServletUtils;
import SheetEngine.utils.SessionUtils;
import body.Logic;
import body.Sheet;
import body.Sheets.SheetsManager;
import body.users.UserManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static SheetEngine.utils.ServletUtils.getEngine;
import static SheetEngine.utils.ServletUtils.getSheetManager;

@WebServlet(name = "NewSheetServlet", urlPatterns = {"/sheetslist/new"})
public class AddSheetServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        if(usernameFromSession == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            String sheetPath = request.getParameter("FilePath");
            response.setStatus(HttpServletResponse.SC_OK);
            SheetsManager sheetManager = getSheetManager(getServletContext());
            try{
                sheetManager.addSheet(sheetPath, usernameFromSession);
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getOutputStream().println(e.getMessage());
            }
        }


    }
}
