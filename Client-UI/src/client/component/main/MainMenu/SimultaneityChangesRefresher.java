package client.component.main.MainMenu;

import client.util.Constants;
import client.util.http.HttpClientUtil;
import dto.SheetDTO;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.util.Constants.GSON_INSTANCE;

public class SimultaneityChangesRefresher extends TimerTask {


    private final Consumer<SheetDTO> updateSheet;
    private final String sheetName;
    private final IntegerProperty sheetVersion;

    public SimultaneityChangesRefresher(String sheetName, IntegerProperty sheetVersion, Consumer<SheetDTO> updateSheet) {
        this.sheetName = sheetName;
        this.sheetVersion = sheetVersion;
        this.updateSheet = updateSheet;
    }

    @Override
    public void run() {
       //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.SIMULTANEITY)
                .newBuilder()
                .addQueryParameter("sheetName",sheetName)
                .addQueryParameter("version", sheetVersion.getValue().toString())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 304){
                    System.out.println("no changes");
                    return;
                }
                else if(response.code() == 200){
                    System.out.println("got changes new version available");
                    String jsonArrayOfUsersNames = response.body().string();
                    SheetDTO currSheet = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, SheetDTO.class);
                    Platform.runLater(() -> {
                        updateSheet.accept(currSheet);
                    });
                }
            }
        });

    }
}
