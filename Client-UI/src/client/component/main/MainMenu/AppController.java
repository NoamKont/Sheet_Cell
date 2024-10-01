package client.component.main.MainMenu;



import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import client.component.dashboard.DashboardController;
import client.component.main.MainMenu.Header.HeaderComponentController;
import client.component.login.LoginController;
import client.component.main.MainMenu.SideBar.Command.CommandComponentController;
import client.component.main.MainMenu.SideBar.Range.RangeComponentController;
import client.component.main.UIbody.UICell;
import client.component.main.UIbody.UIGridPart;
import client.component.main.UIbody.UISheet;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import com.google.gson.reflect.TypeToken;
import dto.SheetDTO;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static client.util.Constants.GSON_INSTANCE;
import static client.util.Constants.REFRESH_RATE;


public class AppController {

    private StringProperty username = new SimpleStringProperty();
    private Stage stage;
    private Scene scene;
    private final Logic logic = new ImplLogic();
    private UISheet uiSheet = new UISheet();
    private final UICell selectedCell = new UICell();
    private ObjectProperty<UICell> selectedCellProperty = new SimpleObjectProperty<>();
    private ObjectProperty<UIGridPart> selectedRowOrColumn = new SimpleObjectProperty<>();
    private StringProperty selectedRange = new SimpleStringProperty();
    private BooleanProperty isWriterPermission = new SimpleBooleanProperty(false);
    private ProgressBar progressBar = new ProgressBar(100);
    private Label statusLabel = new Label("Status: Idle");

    private Timer timer;
    private TimerTask listRefresher;


    @FXML
    private ScrollPane headerComponent;

    @FXML
    private HeaderComponentController headerComponentController;

    @FXML
    private GridPane rangeComponent;

    @FXML
    private RangeComponentController rangeComponentController;

    @FXML
    private VBox commandComponent;

    @FXML
    private CommandComponentController commandComponentController;

    private BorderPane loginComponent;
    private LoginController loginController;

    private Parent dashboardComponent;
    private DashboardController dashboardController;

    @FXML
    private BorderPane bodyComponent;

    @FXML
    public void initialize() {
        loadChatRoomPage();
        bodyComponent.getLeft().getStyleClass().add("left-menu");
        bodyComponent.getTop().getStyleClass().add("top-menu");

        if (headerComponentController != null && rangeComponentController != null && commandComponentController != null) {
            headerComponentController.setMainController(this);
            rangeComponentController.setMainController(this);
            commandComponentController.setMainController(this);
        }

        //bind the selected cell to the UI Header component
        bindModuleToUI();

        selectedCell.cellsDependsOnThemProperty().addListener((observableValue, oldList, newList) -> {
            if (newList != null) {
                newList.stream().forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().add("depends-on-cell");
                });
            }
            if (oldList != null) {
                oldList.stream().forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().remove("depends-on-cell");
                });
            }
        });

        selectedCell.cellsDependsOnHimProperty().addListener((observableValue, oldList, newList) -> {
            if (newList != null) {
                newList.stream().forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().add("influence-on-cell");
                });
            }
            if (oldList != null) {
                oldList.stream().forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().remove("influence-on-cell");
                });
            }
        });

        //listener for the selected cell dependency list  and style of selected cell CSS
        selectedCellProperty.addListener((observableValue, oldCell, newCell) -> {
            if (newCell != null) {
                newCell.getCellLabel().setId("selected-cell");
                commandComponentController.getTextColorPicker().setValue((Color) newCell.getCellLabel().getTextFill());
                Background background = newCell.getCellLabel().getBackground();
                if (background != null && !background.getFills().isEmpty()) {
                    BackgroundFill fill = background.getFills().get(0);
                    commandComponentController.getBackgroundColorPicker().setValue((Color) fill.getFill());
                } else {
                    commandComponentController.getBackgroundColorPicker().setValue(Color.WHITE);
                }

            }
            if (oldCell != null) {
                oldCell.getCellLabel().setId(null);
            }
        });

        //listener for the selected range and style of selected range CSS
        selectedRange.addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                uiSheet.getCoordinatesOfRange(newValue).forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().add("selected-range");
                });
                System.out.println("Selected Range: " + newValue);
            }
            if (oldValue != null) {
                uiSheet.getCoordinatesOfRange(oldValue).forEach(coordinate -> {
                    uiSheet.getCell(coordinate).getCellLabel().getStyleClass().remove("selected-range");
                });
                System.out.println("Unselected Range: " + oldValue);
            }
        });

        //listener for the selected row or column and update the command component
        selectedRowOrColumn.addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                String title;
                if (newValue.getIsRow()) {
                    title = "Row: " + newValue.getName();
                } else {
                    title = "Column: " + newValue.getName();
                }
                commandComponentController.getChosenColumnRow().setText(title);
                commandComponentController.getWidthSpinner().getValueFactory().setValue(newValue.getWidth());
                commandComponentController.getThicknessSpinner().getValueFactory().setValue(newValue.getThickness());
                commandComponentController.getWidthSpinner().setDisable(newValue.getIsRow());
                commandComponentController.getThicknessSpinner().setDisable(!newValue.getIsRow());
            }
        });

        //listener for the width and thickness of the selected row or column and change the width or thickness
        commandComponentController.getWidthSpinner().valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (selectedRowOrColumn.get() != null) {
                changeWidth(newValue);
            }
        });
        commandComponentController.getThicknessSpinner().valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (selectedRowOrColumn.get() != null) {
                changeThickness(newValue);
            }
        });


    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;

    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getUsername() {
        return username.get();
    }

    private void rangeMapListener() {
        rangeComponentController.getRangeListView().getItems().clear();
        uiSheet.rangeMapProperty().addListener((MapChangeListener.Change<? extends String, ? extends Set<Coordinate>> change) -> {
            if (change.wasAdded()) {
                rangeComponentController.addRangeToList(change.getKey());
                System.out.println("Added: " + change.getKey() + " -> " + change.getValueAdded());
            }
            if (change.wasRemoved()) {
                rangeComponentController.deleteRangeFromList(change.getKey());
                System.out.println("Removed: " + change.getKey() + " -> " + change.getValueRemoved());
            }
        });
    }

    public void createSheet(String filePath) {
//        VBox uploadStatus = new VBox(progressBar, statusLabel);
//        uploadStatus.setAlignment(Pos.CENTER); // Center the VBox contents
//        uploadStatus.setPrefSize(400, 250);
//        bodyComponent.setCenter(uploadStatus);
        Task<Void> uploadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
//                    for(int i = 0; i <= 100; i++){
//                        Thread.sleep(15);
//                        updateProgress(i, 100);
//                        updateMessage("Uploading " + i + "%");
//                    }

                // Create the request body for new Sheet
                RequestBody body = new FormBody.Builder()
                        .add("FilePath", filePath)
                        .build();


                HttpClientUtil.runPostAsync(body, Constants.NEW_SHEET, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("Response is Failed");
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error in creating new Sheet");
                            alert.setContentText(e.getMessage());
                            alert.showAndWait();
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        System.out.println("got response");
                        String rawBody = response.body().string();
                        if (response.isSuccessful()) {
                            System.out.println("Response is successful");
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Success");
                                alert.setHeaderText("Sheet Created Successfully");
                                alert.showAndWait();
                            });

                        } else {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Error in creating new Sheet");
                                alert.setContentText(rawBody);
                                alert.showAndWait();
                            });

                        }
                    }
                });

//                    Platform.runLater(() -> {
//                        selectedCell.clearCell();
//                        if (logic.getSheet() == null) {
//                            bodyComponent.setCenter(null);
//                            return;
//                        }
//                        uiSheet = new UISheet(logic.getSheet()); //set the module
//                        rangeMapListener();
//                        versionSelectorMenuListener();
//                        uiSheet.updateSheet(logic.getSheet());
//                        createViewSheet();
//                        isFileOpen.set(true);
//                        System.out.println("Sheet Created");
//                    });
                return null;
            }
        };
        progressBar.progressProperty().bind(uploadTask.progressProperty());
        statusLabel.textProperty().bind(uploadTask.messageProperty());
        new Thread(uploadTask).start();
    }

    public void updateCell(String input) {
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.UPDATE_CELL)
                .newBuilder()
                .addQueryParameter("sheetName", uiSheet.getSheetName())
                .addQueryParameter("cellId", selectedCell.idProperty().getValue())
                .addQueryParameter("value", input)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseBody = response.body().string();
                    if(response.isSuccessful()) {
                        System.out.println("UpdateSheet is successful now bringing the update Sheet");
                        SheetDTO updateSheet = GSON_INSTANCE.fromJson(responseBody, SheetDTO.class);
                        Platform.runLater(() -> {
                            uiSheet.updateSheet(updateSheet);
                            selectedCellProperty.set(uiSheet.getCell(new CoordinateImpl(selectedCell.idProperty().getValue())));
                            selectedCell.updateUICell(selectedCellProperty.get());
                        });
                    }
                    else {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error in updating cell");
                            alert.setContentText(responseBody);
                            alert.showAndWait();
                        });
                    }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Response is Failed");
            }
        });

    }

    private void versionSelectorMenuListener() {
        uiSheet.sheetVersionProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                headerComponentController.addVersionToMenu((Integer) newValue);
            }
        });
    }

    private void bindModuleToUI() {
        // Bind the UI to the module
        headerComponentController.bindModuleToUI(selectedCell, isWriterPermission);
    }

    public ScrollPane creatSheetComponent(UISheet Sheet, boolean isActive) {
        GridPane dynamicGrid = new GridPane();
        //add '1' for the header

        int numRows = uiSheet.getRowsNumber() + 1;
        int numCols = uiSheet.getColumnsNumber() + 1;

        // Add RowConstraints and ColumnConstraints
        for (int i = 0; i < numRows; i++) {
            RowConstraints row = new RowConstraints();

            row.setPrefHeight(uiSheet.thicknessProperty().get());
            row.setMinHeight(Region.USE_PREF_SIZE);
            row.setMaxHeight(Region.USE_PREF_SIZE);
            row.setVgrow(Priority.ALWAYS);
            dynamicGrid.getRowConstraints().add(row);
        }

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(uiSheet.widthProperty().get());
            col.setMinWidth(Region.USE_PREF_SIZE);
            col.setMaxWidth(Region.USE_PREF_SIZE);
            col.setHgrow(Priority.ALWAYS);
            dynamicGrid.getColumnConstraints().add(col);
        }

        // Populate the GridPane with Labels and Headers
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                AnchorPane anchorPane = new AnchorPane();
                if (row == 0 && col > 0) {
                    // Top row (column headers)
                    Label label = new Label(Character.toString((char) ('A' + col - 1))); // "A", "B", "C", ...
                    dynamicGrid.getColumnConstraints().get(col).prefWidthProperty().bind(Sheet.getColumn(label.getText()).widthProperty());
                    // Set click event handler
                    label.setOnMouseClicked(event -> {
                        System.out.println("Label " + label.getText() + " clicked: " + label.getText());
                        selectedRowOrColumn.set(Sheet.getColumn(label.getText()));
                    });
                    setHeaderLabel(anchorPane, label);
                } else if (col == 0 && row > 0) {
                    // First column (row headers)
                    Label label = new Label(Integer.toString(row)); // "1", "2", "3", ..
                    dynamicGrid.getRowConstraints().get(row).prefHeightProperty().bind(Sheet.getRow(label.getText()).thicknessProperty());
                    // Set click event handler
                    label.setOnMouseClicked(event -> {
                        System.out.println("Label " + label.getText() + " clicked: " + label.getText());
                        selectedRowOrColumn.set(Sheet.getRow(label.getText()));
                    });
                    setHeaderLabel(anchorPane, label);
                } else if (row > 0 && col > 0) {
                    String cellID = fromDotToCellID(row, col);
                    Label label = Sheet.getCell(new CoordinateImpl(cellID)).getCellLabel();

                    label.getStyleClass().add("single-cell");
                    Coordinate coordinate = new CoordinateImpl(cellID);
                    label.textProperty().bind(Sheet.getCell(coordinate).effectiveValueProperty());
                    Sheet.setCellLabel(coordinate, label);
                    AnchorPane.setTopAnchor(label, 0.0);
                    AnchorPane.setBottomAnchor(label, 0.0);
                    AnchorPane.setLeftAnchor(label, 0.0);
                    AnchorPane.setRightAnchor(label, 0.0);
                    anchorPane.getChildren().add(label);
                    if (isActive) {
                        // Set click event handler
                        label.setOnMouseClicked(event -> {
                            System.out.println("Label clicked: " + label.getText());
                            selectedCellProperty.set(Sheet.getCell(new CoordinateImpl(cellID)));
                            selectedCell.updateUICell(selectedCellProperty.get());
                        });
                    }
                }
                dynamicGrid.add(anchorPane, col, row);
            }
        }

        dynamicGrid.setAlignment(Pos.CENTER);
        dynamicGrid.setGridLinesVisible(true);

        dynamicGrid.setMinHeight(Region.USE_PREF_SIZE);
        dynamicGrid.setMinWidth(Region.USE_PREF_SIZE);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.fitToHeightProperty().set(true);
        scrollPane.fitToWidthProperty().set(true);
        scrollPane.setContent(dynamicGrid);
        return scrollPane;
    }

    private void createViewSheet() {
        ScrollPane scrollPane = creatSheetComponent(uiSheet, true);
        scrollPane.getStyleClass().add("center-menu");
        bodyComponent.setCenter(scrollPane);
        bodyComponent.setMinWidth(0);
        bodyComponent.setMinHeight(0);
    }

    private void setHeaderLabel(AnchorPane anchorPane, Label label) {
        label.getStyleClass().add("headers-cell");
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(label, 0.0);
        AnchorPane.setBottomAnchor(label, 0.0);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        anchorPane.getChildren().add(label);
    }

    private String fromDotToCellID(int row, int col) {
        return String.valueOf((char) ('A' + col - 1)) + (row);
    }

    public UISheet getUiSheet() {
        return uiSheet;
    }

    public void addRangeToSheet(String rangeName, String topLeft, String bottomRight) {
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.ADD_RANGE)
                .newBuilder()
                .addQueryParameter("sheetName", uiSheet.getSheetName())
                .addQueryParameter("rangeName", rangeName)
                .addQueryParameter("topLeft", topLeft)
                .addQueryParameter("bottomRight", bottomRight)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if(response.isSuccessful()) {
                    System.out.println("UpdateSheet is successful now bringing the update Sheet");
                    Set<Coordinate> rangeCoordinates = GSON_INSTANCE.fromJson(responseBody, new TypeToken<Set<Coordinate>>(){}.getType());
                    Platform.runLater(() -> {
                        uiSheet.addRange(rangeName, rangeCoordinates);
                    });
                }
                else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error in adding range");
                        alert.setContentText(responseBody);
                        alert.showAndWait();
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error in adding range");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }
        });
    }

    public void setSelectedRange(String newValue) {
        selectedRange.set(newValue);
    }

    public void alignCells(Pos pos) {
        selectedRowOrColumn.get().alignCells(pos);
    }

    public void changeWidth(Integer newValue) {
        selectedRowOrColumn.get().setWidth(newValue);
    }

    public void changeThickness(Integer newValue) {
        selectedRowOrColumn.get().setThickness(newValue);
    }

    public void deleteRangeFromSheet(String selectedItem) {
        selectedRange.set(null);
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.DELETE_RANGE)
                .newBuilder()
                .addQueryParameter("sheetName", uiSheet.getSheetName())
                .addQueryParameter("rangeName", selectedItem)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if(response.isSuccessful()) {
                    System.out.println("delete range is successful");
                    Platform.runLater(() -> {
                        uiSheet.deleteRange(selectedItem);
                    });
                }
                else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error in deleting range");
                        alert.setContentText(responseBody);
                        alert.showAndWait();
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error in deleting range");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }
        });
    }

    public int getColumnsNumber() {
        //return logic.getColumnsNumber();
        return uiSheet.getColumnsNumber();
    }

    public void showVersion(Integer i) {
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.GET_VERSION)
                .newBuilder()
                .addQueryParameter("sheetName", uiSheet.getSheetName())
                .addQueryParameter("version", i.toString())
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if(response.isSuccessful()) {
                    System.out.println("Response is successful");
                    Platform.runLater(() -> {
                        Stage popupStage = new Stage();
                        SheetDTO versionSheet = GSON_INSTANCE.fromJson(responseBody, SheetDTO.class);
                        UISheet sheet = new UISheet(versionSheet);
                        ScrollPane popupLayout = creatSheetComponent(sheet, false);
                        Scene popupSortedSheet = new Scene(popupLayout, 600, 400);
                        popupStage.setScene(popupSortedSheet);
                        popupStage.showAndWait();
                    });
                }
                else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error in showing version");
                        alert.setContentText(responseBody);
                        alert.showAndWait();
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error in showing version");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }
        });
    }

//TODO: if graph not needed may delete this method
    public List<String> getValuesFromColumnsAsList(Integer columnIndex, int top, int bottom) {
        try {
            return logic.getSheet().getValuesFromColumn(columnIndex, top, bottom);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in getting values from column");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void changeTextColorForSelectedCell(Color textColor) {
        selectedCell.getCellLabel().setTextFill(textColor);
    }

    public void changeBackgroundColorForSelectedCell(Color backgroundColor) {
        selectedCell.getCellLabel().setBackground(new Background(new BackgroundFill(
                backgroundColor,
                CornerRadii.EMPTY,
                javafx.geometry.Insets.EMPTY)));
    }

    public void resetColorForSelectedCell() {
        selectedCell.getCellLabel().setTextFill(Color.BLACK);
        selectedCell.getCellLabel().setBackground(Background.EMPTY);
    }

    public UICell getSelectedCell() {
        return selectedCell;
    }


    public BooleanProperty isWriterPermissionProperty() {
        return isWriterPermission;
    }

    public void changeMode(String mode) {
        scene.getStylesheets().clear();
        switch (mode) {
            case "Classic Blue":
                scene.getStylesheets().add(getClass().getResource("resources/theme1/classicBlue.css").toExternalForm());
                break;
            case "Deadpool":
                scene.getStylesheets().add(getClass().getResource("resources/theme2/theme2.css").toExternalForm());
                break;
            case "Default":
                scene.getStylesheets().add(getClass().getResource("resources/defaultTheme/default.css").toExternalForm());
                break;
            default:
                System.out.println("No such theme");
                break;
        }
    }

    public void loadLoginPage() {
        URL loginPageUrl = getClass().getResource("/client/component/login/login.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setAppMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadChatRoomPage() {
        URL loginPageUrl = getClass().getResource("/client/component/dashboard/Dash.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            dashboardComponent = fxmlLoader.load();
            dashboardController = fxmlLoader.getController();
            dashboardController.setAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMainPanelTo(Parent Component) {
        scene.setRoot(Component);
    }

    public void switchToDashboard() {
        setMainPanelTo(dashboardComponent);
        dashboardController.startListRefresher();
        cancelTimerTask();
    }

    public void switchToSheetView(String sheetName) {
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.GET_SHEET)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .build()
                .toString();

        System.out.println("New request is launched for: " + finalUrl);
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Response is Failed");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("got response");
                String jsonSheetDTO = response.body().string();
                SheetDTO sheet = GSON_INSTANCE.fromJson(jsonSheetDTO, SheetDTO.class);
                if (sheet == null) {
                    Platform.runLater(() -> {
                        bodyComponent.setCenter(null);
                    });
                    return;
                }

                Platform.runLater(() -> {
                    uiSheet = new UISheet(sheet); //set the module
                    selectedCell.clearCell();
                    rangeMapListener();
                    versionSelectorMenuListener();
                    uiSheet.updateSheet(sheet);

                    //isFileOpen.set(true);
                    System.out.println("Sheet Created");
                    setMainPanelTo(bodyComponent);
                    createViewSheet();
                    startSimultaneityChangesRefresher();
                });
            }
        });
    }

    private void startSimultaneityChangesRefresher() {
        listRefresher = new SimultaneityChangesRefresher(
                uiSheet.getSheetName(),
                uiSheet.sheetVersionProperty(),
                headerComponentController::newVersionExist
        );
        timer = new Timer();
        timer.schedule(listRefresher, 0, REFRESH_RATE);
    }

    public void cancelTimerTask() {
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

   public void updateSheet(SheetDTO sheet) {
        uiSheet.updateSheet(sheet);
    }

    public String getSheetName() {
        return uiSheet.getSheetName();
    }

    public Object usernameProperty() {
        return username;
    }
}
