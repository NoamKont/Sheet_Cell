package client.util;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import body.impl.CoordinateImpl;
import body.impl.Graph;
import body.impl.ImplCell;
import body.impl.ImplSheet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.Range.api.Range;
import expression.Range.impl.RangeImpl;
import utils.deserializer.*;
import utils.serializer.*;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    //TODO: Change the fxml locations to the correct ones
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/client/component/app/main/chat-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/client/component/login/login.fxml";
    public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/client/component/chatroom/chat-room-main.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/SheetCell";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String SHEETS_LIST = FULL_SERVER_PATH + "/sheetslist";

    public final static String NEW_SHEET = FULL_SERVER_PATH + "/sheetslist/new";


    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";

    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    // GSON instance
    public final static Gson GSON_INSTANCE = new GsonBuilder()
                        .registerTypeAdapter(CoordinateImpl .class, new CoordinateSerializer())
                        .registerTypeAdapter(Coordinate .class, new CoordinateDeserializer())
                        .registerTypeAdapter(ImplCell .class, new CellSerializer())
                        .registerTypeAdapter(Cell .class, new CellDeserializer())
                        .registerTypeAdapter(ImplSheet .class, new SheetSerializer())
                        .registerTypeAdapter(Sheet .class, new SheetDeserializer())
                        .registerTypeAdapter(Logic .class, new LogicDeserializer())
                        .registerTypeAdapter(RangeImpl .class,new RangeSerializer())
                        .registerTypeAdapter(Range .class, new RangeDeserializer())
                        .registerTypeAdapter(Graph .class, new GraphSerializer())
                        .registerTypeAdapter(CellDTO .class, new DTOCellDeserializer())
                        .registerTypeAdapter(SheetDTO .class, new DTOSheetDeserializer())
                        .create();
}
