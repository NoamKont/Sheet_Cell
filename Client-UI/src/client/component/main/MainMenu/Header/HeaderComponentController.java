package client.component.main.MainMenu.Header;



import client.component.main.MainMenu.AppController;
import client.component.main.MainMenu.Header.DynamicAnalysis.DynamicAnalysisController;
import client.component.main.MainMenu.Header.DynamicAnalysis.dataPopUpController;
import client.component.main.UIbody.UICell;
import dto.SheetDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class HeaderComponentController implements Initializable {

    private AppController mainController;

    @FXML
    private VBox headerVbox;

    @FXML
    private Label IdViewer;

    @FXML
    private TextField actionLine;

    @FXML
    private Label lastCellVersionUpdateViewer;

    @FXML
    private Label originalValueViewer;

    @FXML
    private Label pathView;

    @FXML
    private Button updateValueBtn;

    @FXML
    private Button updateVersionBtn;

    @FXML
    private Button dynamicAnalysisBtn;

    @FXML
    private ComboBox<String> modeComboBox;

    @FXML
    private Label usernameLabel;

    @FXML
    private MenuButton versionSelectorMenu;

    @FXML
    private StackPane notification;

    @FXML
    private Button back2DashBtn;

    @FXML
    private Label userUpdateCellLabel;

    private BooleanProperty newVersionAvilable = new SimpleBooleanProperty(false);
    private SheetDTO newestSheet;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void newVersionExist(SheetDTO s){
        newestSheet = s;
        updateVersionBtn.setDisable(false);
        notification.setVisible(true);

        newVersionAvilable.set(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modeComboBox.getItems().addAll("Default", "Classic Blue", "Deadpool");

        updateVersionBtn.setOnAction(e -> {
            updateVersionBtn.setDisable(true);
            notification.setVisible(false);
            newVersionAvilable.set(false);
            mainController.updateSheet(newestSheet);
        });

        back2DashBtn.setOnAction(e -> {
            mainController.switchToDashboard();
        });
    }

    @FXML
    void ModeChangePressed(ActionEvent event) {
        String mode = modeComboBox.getValue();
        mainController.changeMode(mode);
    }

    @FXML
    void updateValueBtnPressed(ActionEvent event) {
        String input = actionLine.getText();
        mainController.updateCell(input);
        actionLine.clear();
    }

    public void bindModuleToUI(UICell selectedCell, BooleanProperty isWriterPermission){
        IdViewer.textProperty().bind(selectedCell.idProperty());
        originalValueViewer.textProperty().bind(selectedCell.originalValueProperty());
        StringExpression sb = Bindings.concat("Last Update Version: ", selectedCell.lastVersionUpdateProperty());
        lastCellVersionUpdateViewer.textProperty().bind(sb);
        StringExpression usernameHelloText = Bindings.concat("Hello ", mainController.usernameProperty(), "!");
        usernameLabel.textProperty().bind(usernameHelloText);
        userUpdateCellLabel.textProperty().bind(Bindings.concat("Cell Update By: ", selectedCell.updateByProperty()));

        //dynamicAnalysisBtn.disableProperty().bind(isWriterPermission.not());
        //versionSelectorMenu.disableProperty().bind(isWriterPermission.not());
        //IdViewer.disableProperty().bind(isWriterPermission.not());
        //originalValueViewer.disableProperty().bind(isWriterPermission.not());
        updateValueBtn.disableProperty().bind(isWriterPermission.not().or(newVersionAvilable));
        actionLine.disableProperty().bind(isWriterPermission.not().or(newVersionAvilable));

    }

    public void addVersionToMenu(Integer version){
        versionSelectorMenu.getItems().clear();
        for(Integer i = 1; i <= version; i++){
            MenuItem menuItem = new MenuItem("Version " + i.toString());
            menuItem.setOnAction(e -> {
                String versionNumber = menuItem.getText().substring(8);
                mainController.showVersion(Integer.parseInt(versionNumber));
            });
            versionSelectorMenu.getItems().add(menuItem);
        }
    }

    @FXML
    void dynamicAnalysisPress(ActionEvent event) throws IOException {
        Stage popupStage = new Stage();
        UICell selectedCell = mainController.getSelectedCell();

        try{
            Double.parseDouble(selectedCell.originalValueProperty().get());
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cell value is not a number");
            alert.setContentText("Please select a cell that his original value is number");
            alert.showAndWait();
            return;
        }

        // Set the pop-up window to be modal (blocks interaction with other windows)
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Dynamic Analysis");
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("DynamicAnalysis/clientDataPopupForAnalysis.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        dataPopUpController controller = fxmlLoader.getController();

        Scene popupScene = new Scene(root, 600, 400);

        controller.setHeaderComponentController(this);
        controller.setPopupStage(popupStage);

        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    public void DynamicAnalysis(int min, int max, int stepSize, Stage popupStage) throws Exception {
        // Set the pop-up window to be modal (blocks interaction with other windows)
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("DynamicAnalysis/clientDynamicPopUp.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        DynamicAnalysisController controller = fxmlLoader.getController();

        Scene popupScene = new Scene(root, 900, 500);

        controller.setHeaderComponentController(this);
        controller.setMainController(mainController);

        controller.setPopupStage(popupStage);
        controller.setMin(min);
        controller.setMax(max);
        controller.setStepSize(stepSize);

        popupStage.setScene(popupScene);

    }

}

