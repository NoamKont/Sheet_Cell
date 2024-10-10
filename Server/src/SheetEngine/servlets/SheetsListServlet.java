package SheetEngine.servlets;


import SheetEngine.utils.ServletUtils;
import SheetEngine.utils.SessionUtils;
import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import body.Sheets.SheetInfo;
import body.Sheets.SheetsManager;
import body.impl.CoordinateImpl;
import body.impl.Graph;
import body.impl.ImplCell;
import body.permission.PermissionInfo;
import body.users.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.Range.api.Range;
import expression.Range.impl.RangeImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.serializer.*;
import utils.deserializer.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static SheetEngine.constants.Constants.GSON_INSTANCE;

@WebServlet(name = "SheetsListServlet", urlPatterns = {"/sheetslist"})
public class SheetsListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            SheetsManager sheetManager = ServletUtils.getSheetManager(getServletContext());
            Set<Logic> sheetList = sheetManager.getSheets();

            List<SheetInfo> sheetInfo = new ArrayList<>();
            for (Logic logic : sheetList) {
                List<PermissionInfo> permissionInfo = logic.getPermissionManager().getPermissions();//.values().stream().toList();
                List<PermissionInfo> acceptedPermissionInfo = logic.getPermissionManager().getAcceptedPermissions().values().stream().toList();
                sheetInfo.add(new SheetInfo(logic.getSheet(), permissionInfo, acceptedPermissionInfo,username));
            }

            //String json = GSON_INSTANCE.toJson(sheetList);
            String json = GSON_INSTANCE.toJson(sheetInfo);
            out.println(json);
            out.flush();
        }
    }

}
