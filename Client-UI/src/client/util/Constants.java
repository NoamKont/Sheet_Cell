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
import dto.RangeDTO;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.Range.api.Range;
import expression.Range.impl.RangeImpl;
import utils.deserializer.*;
import utils.serializer.*;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static int REFRESH_RATE = 500;

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/SheetCell";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logout";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String SHEETS_LIST = FULL_SERVER_PATH + "/sheetslist";

    public final static String NEW_SHEET = FULL_SERVER_PATH + "/sheetslist/new";
    public final static String GET_SHEET = FULL_SERVER_PATH + "/sheetslist/sheet";
    public static final String GET_VERSION = FULL_SERVER_PATH + "/sheetslist/sheet/version";
    public final static String UPDATE_CELL = FULL_SERVER_PATH + "/sheetslist/sheet/updateCell";

    public final static String ADD_RANGE = FULL_SERVER_PATH + "/range/add";
    public final static String DELETE_RANGE = FULL_SERVER_PATH + "/range/delete";

    public final static String SORT_SHEET = FULL_SERVER_PATH + "/utils/sort";
    public final static String FILTER_SHEET = FULL_SERVER_PATH + "/utils/filter";
    public final static String DYNAMIC_SHEET = FULL_SERVER_PATH + "/utils/dynamic";
    public static final String GET_VALUES_FROM_COLUMN = FULL_SERVER_PATH +  "/utils/getValuesFromColumn";
    public static final String GET_PERMISSION = FULL_SERVER_PATH + "/permission";
    public static final String ADD_PERMISSION = FULL_SERVER_PATH + "/permission/add";

    public static final String SIMULTANEITY = FULL_SERVER_PATH + "/simultaneity";



    // GSON instance
    public final static Gson GSON_INSTANCE = new GsonBuilder()
                        .registerTypeAdapter(Coordinate .class, new CoordinateDeserializer())
                        .registerTypeAdapter(Cell .class, new CellDeserializer())
                        //.registerTypeAdapter(Logic .class, new LogicDeserializer())
                        .registerTypeAdapter(SheetDTO .class, new DTOSheetDeserializer())
                        .registerTypeAdapter(RangeDTO .class, new DTORangeDeserializer())
                        .create();
}
