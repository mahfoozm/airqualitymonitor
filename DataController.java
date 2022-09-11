// EECS 1021 Major Project W22
// Mohammad Mahfooz
// 218621045
// Original coding (mostly) by Richard Robinson and James Andrew Smith; York University

package eecs1021;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListenerWithExceptions;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.apache.commons.lang3.StringUtils;
import java.nio.charset.StandardCharsets;

public class DataController implements SerialPortMessageListenerWithExceptions  {
    private final ObservableList<XYChart.Data<Number, Number>> dataPoints;
    private static final byte[] DELIMITER = new byte[]{'\n'};

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
            return;
        }

        var data = serialPortEvent.getReceivedData();
        Number time = System.currentTimeMillis();

        // Convert the received Byte array into a String
        String dataString;
        dataString = null;
        try {
            dataString = new String(data, StandardCharsets.UTF_8);
            dataString  = StringUtils.chop(dataString);
        }
        catch(Exception e)
        {
            System.out.println("error");
        }
        assert dataString != null;

        // Convert String to Integer and then convert to plot point
        Integer CO2Value = Integer.valueOf(dataString);
        var Point = new XYChart.Data<>(time, (Number)CO2Value);
        Platform.runLater(()-> this.dataPoints.add(Point));

    }

    public DataController() {
        this.dataPoints = FXCollections.observableArrayList();
    }

    public ObservableList<XYChart.Data<Number, Number>> getDataPoints() {
        return dataPoints;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void catchException(Exception e) {
        e.printStackTrace();
    }

    @Override
    public byte[] getMessageDelimiter() {
        return DELIMITER;
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        return true;
    }
}