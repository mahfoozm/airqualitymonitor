// EECS 1021 Major Project W22
// Mohammad Mahfooz
// 218621045

package eecs1021;

// a lot of imports for this one...
import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FormatStringConverter;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

public class MajorProjectMain extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }

    // Set up Table (which will subsequently be used in the chart as well)
    private static TableView <XYChart.Data<Number, Number>> getTableView()
    {
        var Table = new TableView <XYChart.Data<Number, Number>>();
        var Time = new TableColumn <XYChart.Data<Number, Number>, Number>("Time");
        Time.setCellValueFactory(row -> row.getValue().XValueProperty());

        var dateFormat = DateFormat.getTimeInstance();
        var convert = new FormatStringConverter<Number>(dateFormat);
        Time.setCellFactory(column -> new TextFieldTableCell<>(convert));

        var CO2 = new TableColumn<XYChart.Data<Number, Number>, Number>("CO2");
        CO2.setCellValueFactory(row -> row.getValue().YValueProperty());
        Table.getColumns().setAll(List.of(Time, CO2));

        return Table;

    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Set up Serial Port
        var CO2 = new DataController();
        var Now = System.currentTimeMillis();
        var sp = SerialPortService.getSerialPort("COM4");
        var outputStream = sp.getOutputStream();
        sp.addDataListener(CO2);

        // Set up Table (GUI)
        var Table = getTableView();
        Table.setItems(CO2.getDataPoints());
        var VBox = new VBox(Table);

        // Set Window Title
        primaryStage.setTitle("Real Time CO2 Monitor");
        var Pane = new BorderPane();

        // Create x/y axis
        var xAxis = new NumberAxis("Time (real-time)", Now, Now + 100000, 10000);
        var yAxis = new NumberAxis("CO2 Levels (parts per million)", 350, 2350, 1);

        // Create series
        var Series = new XYChart.Series<>(CO2.getDataPoints());
        var lineChart = new LineChart<>(xAxis, yAxis, FXCollections.singletonObservableList(Series));
        lineChart.setTitle("CO2 Values Vs Time");

        // Create buttons
        var Button = new Button("Turn on Fan");
        var Button2 = new Button("Turn off Fan");

        // Initialize and Add elements to GUI (chart, buttons, table)
        Pane.setLeft(VBox);
        Pane.setCenter(lineChart);
        Pane.setTop(Button);
        Pane.setRight(Button2);
        Pane.setPadding(new Insets(35, 30, 0, 25));
        var scene = new Scene(Pane, 1000, 500);

        // Launch GUI
        primaryStage.setScene(scene);
        primaryStage.show();

        // Button event (send value over serial when button is pushed)
        Button.setOnMousePressed(value ->
        {
            try {
                outputStream.write(138);
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
        Button2.setOnMousePressed(value ->
        {
            try {
                outputStream.write(139);
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }
}