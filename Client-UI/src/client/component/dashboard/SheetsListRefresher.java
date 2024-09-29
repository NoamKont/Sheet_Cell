package client.component.dashboard;


import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import body.impl.CoordinateImpl;
import body.impl.Graph;
import body.impl.ImplCell;
import body.impl.ImplSheet;
import client.component.main.UIbody.SheetInfo;
import client.util.Constants;
import client.util.http.HttpClientUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.Range.api.Range;
import expression.Range.impl.RangeImpl;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.serializer.*;
import utils.deserializer.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.util.Constants.GSON_INSTANCE;

public class SheetsListRefresher extends TimerTask {

//    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<SheetInfo>> SheetsListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;
    private final ObservableList<SheetInfo> currentSheets;


    public SheetsListRefresher(BooleanProperty shouldUpdate, Consumer<List<SheetInfo>> sheetsListConsumer, ObservableList<SheetInfo> currentSheets) {
        this.shouldUpdate = shouldUpdate;
        this.SheetsListConsumer = sheetsListConsumer;
        this.currentSheets = currentSheets;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;

//        //for debugging
//        if(finalRequestNumber == 2){
//            shouldUpdate.setValue(false);
//        }


        HttpClientUtil.runAsync(Constants.SHEETS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("got response");
                String jsonArrayOfUsersNames = response.body().string();
                Set<Logic> sheetsList = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, new TypeToken<Set<Logic>>(){}.getType());
                //for debugging
                System.out.println("Number of Sheets in the System: " + sheetsList.size());
                List<SheetInfo> sheetInfo = new ArrayList<>();
                for (Logic logic : sheetsList) {
                    sheetInfo.add(new SheetInfo(logic.getSheet()));
                }

                if(!sheetInfo.equals(currentSheets)){
                    SheetsListConsumer.accept(sheetInfo);
                }
            }
        });
    }
}
