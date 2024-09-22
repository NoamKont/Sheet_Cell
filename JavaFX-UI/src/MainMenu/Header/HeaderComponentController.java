package MainMenu.Header;

import MainMenu.AppController;
import MainMenu.Header.DynamicAnalysis.DynamicAnalysisController;
import MainMenu.Header.DynamicAnalysis.dataPopUpController;
import UIbody.UICell;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private ComboBox<String> versionSelectorMenu;

    @FXML
    private Button uploadXmlBtn;

    @FXML
    private Button dynamicAnalysisBtn;

    @FXML
    private ComboBox<String> modeComboBox;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modeComboBox.getItems().addAll("Default", "Classic Blue", "Deadpool");
    }

    @FXML
    void ModeChangePressed(ActionEvent event) {
        String mode = modeComboBox.getValue();
        mainController.changeMode(mode);
    }

    @FXML
    void selectedVersionToShow(ActionEvent event) {
        String version = versionSelectorMenu.getValue().substring(8);
        mainController.showVersion(Integer.parseInt(version));
    }

    @FXML
    private void clickedOnBtnUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            System.out.println("File selected: " + selectedFile.getName());
            mainController.createSheet(selectedFile.getAbsolutePath());
            pathView.visibleProperty().bind(mainController.isFileOpenProperty());
            pathView.setText(selectedFile.getAbsolutePath());

        }
    }

    @FXML
    void updateValueBtnPressed(ActionEvent event) {
        String input = actionLine.getText();
        mainController.updateCell(input);
        actionLine.clear();
    }

    public void bindModuleToUI(UICell selectedCell, BooleanProperty isFileLoaded){
        IdViewer.textProperty().bind(selectedCell.idProperty());
        originalValueViewer.textProperty().bind(selectedCell.originalValueProperty());
        StringExpression sb = Bindings.concat("Last Update Version: ", selectedCell.lastVersionUpdateProperty());
        lastCellVersionUpdateViewer.textProperty().bind(sb);
        dynamicAnalysisBtn.disableProperty().bind(isFileLoaded.not());
        versionSelectorMenu.disableProperty().bind(isFileLoaded.not());
        IdViewer.disableProperty().bind(isFileLoaded.not());
        originalValueViewer.disableProperty().bind(isFileLoaded.not());
        updateValueBtn.disableProperty().bind(isFileLoaded.not());
        actionLine.disableProperty().bind(isFileLoaded.not());

    }

    public void addVersionToMenu(Integer version){
        versionSelectorMenu.getItems().clear();
        for(Integer i = 1; i <= version; i++){
            versionSelectorMenu.getItems().add("Version " + i.toString());
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
        URL url = getClass().getResource("DynamicAnalysis/dataPopUpforanalysis.fxml");
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
        URL url = getClass().getResource("DynamicAnalysis/dynamicPopUp.fxml");
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

