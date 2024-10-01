package client.component.dashboard;


import body.Logic;
import body.permission.PermissionInfo;
import client.component.main.UIbody.SheetInfo;
import client.util.Constants;
import client.util.http.HttpClientUtil;

import com.google.gson.reflect.TypeToken;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static client.util.Constants.GSON_INSTANCE;

public class SheetsListRefresher extends TimerTask {

//    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<SheetInfo>> SheetsListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;
    private final ObservableList<SheetInfo> currentSheets;
    private final String username;


    public SheetsListRefresher(BooleanProperty shouldUpdate, Consumer<List<SheetInfo>> sheetsListConsumer, ObservableList<SheetInfo> currentSheets,String username) {
        this.shouldUpdate = shouldUpdate;
        this.SheetsListConsumer = sheetsListConsumer;
        this.currentSheets = currentSheets;
        this.username = username;
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
                    List<PermissionInfo> permissionInfo = logic.getPermissionManager().getPermissions().values().stream().toList();
                    sheetInfo.add(new SheetInfo(logic.getSheet(), permissionInfo, username));
                    //sheetInfo.add(new SheetInfo(logic.getSheet()));   //, permissionInfo));
                }
                if(!sheetInfo.equals(currentSheets)){
                    SheetsListConsumer.accept(sheetInfo);
                }
            }
        });
    }
}
