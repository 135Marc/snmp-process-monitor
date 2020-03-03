import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.snmp4j.smi.OID;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class ProcessDisplay implements Initializable {
    @FXML
    private TableView<ProcessInfo> tableproc;
    @FXML
    private Label procn;
    @FXML
    private Label mem;

    private MainView mv;
    private MainController mc;

    public void init(MainView main) {
        this.mv=main;
    }

    public void setMainController(MainController mc) {this.mc=mc;}


    public void retrieve() {
        try {
            if (this.mc.getAgente().isWalk()) {
                Utilities.walkUpdate(this.mc.getAgente());
                tableproc.getItems().addAll(this.mc.getAgente().getProcesses());
            } else if (this.mc.getAgente().isBulk()) {
                Utilities.bulkUpdate(this.mc.getAgente());
                tableproc.getItems().addAll(this.mc.getAgente().getProcesses());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void get_scalars() {
        Agent a = this.mc.getAgente();
        int i=0;
        for(OID id : a.getOids().subList(0,2)){
            a.asyncWalk(id);
            if (i==0) procn.setText(a.getResultList().get(0));
            else mem.setText(a.getResultList().get(1));
            i++;
        }
        a.clearResults();
    }

    public void export_MeasuresHTML() {
        Utilities.export_processinfo(this.mc.getAgente().getProcesses());
    }

    public void export_MeasuresXML() {
        Utilities.export_XML(this.mc.getAgente().getProcesses());
    }

    public void export_MeasureBinary() {
        Utilities.export_Binary(this.mc.getAgente().getProcesses());
    }



    public void showPerf() {
        this.mv.showPerfDisplay();
    }



    @Override
    public void initialize (URL location, ResourceBundle resources) {
        TableColumn<ProcessInfo,String> tc3 = new TableColumn<>("Name");
        tc3.setPrefWidth(200);
    //    tc3.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue()));
        tc3.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<ProcessInfo,String> tc4 = new TableColumn<>("Path");
        tc4.setPrefWidth(200);
        tc4.setCellValueFactory(new PropertyValueFactory<>("path"));
        TableColumn<ProcessInfo,String> tc5 = new TableColumn<>("Type");
        tc5.setPrefWidth(200);
        tc5.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<ProcessInfo,String> tc6 = new TableColumn<>("Status");
        tc6.setPrefWidth(200);
        tc6.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableproc.getColumns().addAll(tc3,tc4,tc5,tc6);

    }


    public void refreshTest() {
        Timeline timeholder = new Timeline(new KeyFrame(Duration.seconds(this.mc.getAgente().getPeriod()), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                get_scalars();
                tableproc.getItems().clear();
                retrieve();
            }
        }));
        timeholder.setCycleCount(Timeline.INDEFINITE);
        timeholder.play();
    }

}
