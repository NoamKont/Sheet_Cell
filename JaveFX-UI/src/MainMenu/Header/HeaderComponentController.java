package MainMenu.Header;

import MainMenu.AppController;
import UIbody.UICell;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HeaderComponentController implements Initializable {

    private AppController mainController;

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
    private SplitMenuButton versionSelectorMenu;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actionLine.textProperty().addListener((observable, oldValue, newValue) -> {
            if(actionLine.getText().isEmpty()){
                updateValueBtn.setDisable(true);
                return;
            }
            updateValueBtn.setDisable(false);
        });
    }

    @FXML
    private void clickedOnBtnUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        //TODO change before submit
        fileChooser.setInitialDirectory(new File("C:\\Users\\Noam\\Downloads\\Ex2 example"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            System.out.println("File selected: " + selectedFile.getName());
            mainController.createSheet(selectedFile.getAbsolutePath());
            pathView.setText(selectedFile.getAbsolutePath());
            pathView.setVisible(true);
        }
    }

    @FXML
    void updateValueBtnPressed(ActionEvent event) {
        String input = actionLine.getText();
        mainController.updateCell(input);
        actionLine.clear();
    }

    public void bindModuleToUI(UICell selectedCell) {
        IdViewer.textProperty().bind(selectedCell.idProperty());
        originalValueViewer.textProperty().bind(selectedCell.originalValueProperty());

        StringExpression sb = Bindings.concat("Last Update Version: ", selectedCell.lastVersionUpdateProperty());
        lastCellVersionUpdateViewer.textProperty().bind(sb);
    }

    public void addVersionToMenu(Integer version){
        versionSelectorMenu.getItems().add(new MenuItem("Version " + version.toString()));
    }

//    public void setIdViewer(String CellID){
//        IdViewer.setText(CellID);
//    }
//
//    public void setOriginalValueViewer(String originalValue) {
//        originalValueViewer.setText(originalValue);
//    }
//
//    public void setLastCellVersionUpdateViewer(Integer lastCellVersionUpdate) {
//        lastCellVersionUpdateViewer.setText(lastCellVersionUpdate.toString());
//    }

}

