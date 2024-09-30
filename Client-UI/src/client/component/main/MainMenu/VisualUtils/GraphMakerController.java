package client.component.main.MainMenu.VisualUtils;


import client.component.main.MainMenu.AppController;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import static client.util.Constants.GSON_INSTANCE;

public class GraphMakerController implements Initializable {

    private List<String> xAxis;
    private List<String> yAxis;
    private String xTitle;
    private String yTitle;

    private AppController mainController;

    @FXML
    private Button generateBtn;

    @FXML
    private ChoiceBox<String> graphTypeChoiceBox;

    @FXML
    private TextField xAxisBounds;

    @FXML
    private ChoiceBox<String> xAxisColumn;

    @FXML
    private TextField yAxisBounds;

    @FXML
    private ChoiceBox<String> yAxisColumn;

    private Stage popupStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graphTypeChoiceBox.getItems().addAll("Line", "Bar");
        generateBtn.disableProperty().bind(xAxisColumn.valueProperty().isNull()
                .or(yAxisColumn.valueProperty().isNull())
                .or(xAxisBounds.textProperty().isEmpty())
                .or(yAxisBounds.textProperty().isEmpty())
                .or(graphTypeChoiceBox.valueProperty().isNull()));


    }

    @FXML
    void generateBtnPress(ActionEvent event) {
        String xAxisCol = xAxisColumn.getValue();
        String yAxisCol = yAxisColumn.getValue();
        String xAxisBoundsStr = xAxisBounds.getText();
        String yAxisBoundsStr = yAxisBounds.getText();
        String graphType = graphTypeChoiceBox.getValue();
        try{
            analyzeData(xAxisCol, yAxisCol, xAxisBoundsStr, yAxisBoundsStr, graphType);
            if(graphType.equals("Line")){
                createLineGraph();
            }
            else{
                createBarGraph();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in Creating Graph");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void createBarGraph() {
        try{

        List<Double> y = this.yAxis.stream().mapToDouble(Double::parseDouble).boxed().toList();
        // Define the x and y axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xTitle);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yTitle);


        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(xTitle + " X " + yTitle);

        for (int i = 0; i < this.xAxis.size(); i++) {
            series.getData().add(new XYChart.Data<>(this.xAxis.get(i), y.get(i)));
        }

        barChart.getData().add(series);

        Scene popupGraph = new Scene(barChart, 900, 500);
        popupStage.setScene(popupGraph);
        }catch (Exception e){
            throw new IllegalArgumentException("Y axis must be numeric in a bar chart.");
        }
    }

    private void createLineGraph() {
        // Create the line chart
        // Define the x and y axes
        try {
            List<Double> x = this.xAxis.stream().mapToDouble(Double::parseDouble).boxed().toList();
            List<Double> y = this.yAxis.stream().mapToDouble(Double::parseDouble).boxed().toList();


            NumberAxis xAxis = new NumberAxis(Collections.min(x) * 0.5, Collections.max(x) * 1.5, 100);
            xAxis.setLabel(xTitle);

            NumberAxis yAxis = new NumberAxis(Collections.min(y) * 0.5, Collections.max(y) * 1.5, 100);
            yAxis.setLabel(yTitle);

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(xTitle + " X " + yTitle);

            for (int i = 0; i < this.xAxis.size(); i++) {
                series.getData().add(new XYChart.Data<>(x.get(i), y.get(i)));
            }

            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.getData().add(series);
            Scene popupGraph = new Scene(lineChart, 900, 500);
            popupStage.setScene(popupGraph);
        }catch (Exception e){
            throw new IllegalArgumentException("X and Y axis must be numeric in Line Graph");
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
        int columnsNumber = mainController.getColumnsNumber();
        for(int col = 0; col < columnsNumber; col++){
            String colName = "Column " + Character.toString('A' + col);
            xAxisColumn.getItems().add(colName);
            yAxisColumn.getItems().add(colName);
        }
    }

    private void analyzeData(String xAxisCol, String yAxisCol, String xAxisBounds, String yAxisBounds, String graphType) {
        Integer xColIndex = xAxisCol.substring(7).charAt(0) - 'A' + 1;
        Integer yColIndex = yAxisCol.substring(7).charAt(0) - 'A' + 1;

        Integer xTopBound = Integer.parseInt(xAxisBounds.split(":")[0]);
        Integer xBottomBound = Integer.parseInt(xAxisBounds.split(":")[1]);

        Integer yTopBound = Integer.parseInt(yAxisBounds.split(":")[0]);
        Integer yBottomBound = Integer.parseInt(yAxisBounds.split(":")[1]);

        xAxis = mainController.getValuesFromColumnsAsList(xColIndex, xTopBound, xBottomBound);
        yAxis = mainController.getValuesFromColumnsAsList(yColIndex, yTopBound, yBottomBound);


        if(xAxis.size() != yAxis.size()){
            throw new IllegalArgumentException("X and Y axis must have the same number of values");
        }
        xTitle = xAxisCol;
        yTitle = yAxisCol;

    }


    public void setStage(Stage popupStage) {
        this.popupStage = popupStage;
    }
}
