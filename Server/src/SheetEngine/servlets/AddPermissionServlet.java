package SheetEngine.servlets;

import SheetEngine.utils.ServletUtils;
import SheetEngine.utils.SessionUtils;
import body.Logic;
import body.Sheets.SheetsManager;
import body.permission.PermissionInfo;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static SheetEngine.constants.Constants.GSON_INSTANCE;

@WebServlet(name = "AddPermissionServlet", urlPatterns = {"/permission/add"})
public class AddPermissionServlet extends HttpServlet {
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
            String userRequest = request.getParameter("userName");
            synchronized (this){
                if(sheetsManager.isSheetExists(sheetName)){
                    Logic sheetEngine = sheetsManager.getSheet(sheetName);
                    PermissionInfo.Permissions permission = PermissionInfo.Permissions.valueOf(request.getParameter("permission"));
                    PermissionInfo.Status status = PermissionInfo.Status.valueOf(request.getParameter("status"));
                    sheetEngine.getPermissionManager().addPermission(userRequest,permission,status);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else{
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }
}
