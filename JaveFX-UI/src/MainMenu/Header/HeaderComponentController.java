package MainMenu.Header;

import MainMenu.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HeaderComponentController{

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

    public void setIdViewer(String CellID){
        IdViewer.setText(CellID);
    }

    public void setOriginalValueViewer(String originalValue) {
        originalValueViewer.setText(originalValue);
    }

}

