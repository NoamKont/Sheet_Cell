package MainMenu.SideBar.Command;


import MainMenu.AppController;
import UIbody.UISheet;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class CommandComponentController implements Initializable {

    @FXML
    private Text chosenColumnRow;

    @FXML
    private Spinner<Integer> thicknessSpinner;

    @FXML
    private Spinner<Integer> widthSpinner;

    @FXML
    private ComboBox<String> alignmentBox;

    private AppController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // alignment box
        ObservableList<String> options =
                FXCollections.observableArrayList( "Left", "Center", "Right" );
        alignmentBox.setItems(options);


        SpinnerValueFactory<Integer> widthValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200);
        widthSpinner.setValueFactory(widthValueFactory);

        SpinnerValueFactory<Integer> thicknessValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 200);
        thicknessSpinner.setValueFactory(thicknessValueFactory);

    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void alignmentSelectionListener(ActionEvent event) {
        int selectedIndex = alignmentBox.getSelectionModel().getSelectedIndex();
        switch (selectedIndex) {
            case 0:
                mainController.alignCells(javafx.geometry.Pos.CENTER_LEFT);
                break;
            case 1:
                mainController.alignCells(Pos.CENTER);
                break;
            case 2:
                mainController.alignCells(Pos.CENTER_RIGHT);
                break;
        }
    }
    @FXML
    void sortSheetBtnClicked(ActionEvent event) throws IOException {
        Stage popupStage = new Stage();

        // Set the pop-up window to be modal (blocks interaction with other windows)
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Sorted Sheet");
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("popupViewForSort.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        sortPopUPController controller = fxmlLoader.getController();

        Scene popupScene = new Scene(root, 600, 400);

        controller.setCommandComponentController(this);
        controller.setPopupStage(popupStage);
        controller.setColumnsNumber(mainController.getColumnsNumber());

        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    public void sortSheet(String topLeft, String bottomRight, String[] columns,Stage popupStage) {
        UISheet sortedSheet = mainController.sortSheet(topLeft, bottomRight, columns);
        ScrollPane popupLayout = mainController.creatSheetComponent(sortedSheet);
        Scene popupSortedSheet = new Scene(popupLayout, 600, 400);
        popupStage.setScene(popupSortedSheet);

    }

    public Text getChosenColumnRow() {
        return chosenColumnRow;
    }
    public Spinner<Integer> getThicknessSpinner() {
        return thicknessSpinner;
    }
    public Spinner<Integer> getWidthSpinner() {
        return widthSpinner;
    }
}

