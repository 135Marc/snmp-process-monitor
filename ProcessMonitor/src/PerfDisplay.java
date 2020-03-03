import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.util.Duration;
import org.snmp4j.smi.OID;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PerfDisplay implements Initializable {

    CategoryAxis xAxis  = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    NumberAxis eixoX = new NumberAxis();
    NumberAxis eixoY = new NumberAxis();

    @FXML
    private ScatterChart bcn = new ScatterChart(xAxis,yAxis);
    @FXML
    private PieChart pieChart1;
    @FXML
    private BarChart barChart =  new BarChart(xAxis,yAxis);
    @FXML
    private LineChart lineChart = new LineChart(eixoX,eixoY);

    private MainView mv;
    private MainController mc;

    public void init(MainView main) {
        this.mv=main;
    }

    public void setMainController(MainController mc) {this.mc=mc;}

    public void fillChart() {
        this.bcn.setAnimated(false);
        XYChart.Series dataSeries0 = new XYChart.Series();
        dataSeries0.setName("CPU Time Used (s)");
        List<ProcessInfo> results = this.mc.getAgente().getProcesses();
        for (ProcessInfo res : results) {
            dataSeries0.getData().add(new XYChart.Data(res.getName(),Integer.parseInt(res.getCputimeuse())));
        }
        this.bcn.getData().add(dataSeries0);
        if(this.bcn.getData().size()>3) {
            this.bcn.getData().clear();
        }
        this.mc.getAgente().clearResults();
    }

    public void fillChartv2() {
        List<OID> perfois = Utilities.create_PerfOIDs();
        if(this.mc.getAgente().isMultithreaded()) perfois.forEach(id -> this.mc.getAgente().asyncWalkMulti(id));
        else perfois.forEach(id -> this.mc.getAgente().asyncWalk(id));
        List<String> results = this.mc.getAgente().getResultList();
        int usedReal = Integer.parseInt(results.get(0)) - Integer.parseInt(results.get(1));
        results.add(String.valueOf(usedReal));
        ObservableList<PieChart.Data> datalist = Utilities.generate_PerfData(results);
        pieChart1.setData(datalist);
        this.mc.getAgente().clearResults();
    }

    public void fillBar() {
        if (this.mc.getAgente().isBulk()) {
            if (this.mc.getAgente().isMultithreaded()) this.mc.getAgente().asyncBulkGetMulti(new OID(".1.3.6.1.4.1.2021.10.1.5"));
                else this.mc.getAgente().asyncBulkGet(new OID(".1.3.6.1.4.1.2021.10.1.5")); //laLoadInt
        }
        else {
            if(this.mc.getAgente().isMultithreaded()) this.mc.getAgente().asyncWalkMulti((new OID(".1.3.6.1.4.1.2021.10.1.5")));
            else this.mc.getAgente().asyncWalk((new OID(".1.3.6.1.4.1.2021.10.1.5")));
            this.mc.getAgente().getResultList().remove(this.mc.getAgente().getResultList().size()-1);
        }
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Load Average (1,5,15) Minutes*");
        int i = 1;
        for (String res : this.mc.getAgente().getResultList()) {
            dataSeries1.getData().add(new XYChart.Data(String.valueOf(i),Integer.parseInt(res)));
            i= (i==1) ? (i+=4)  : (i+=10) ;
        }
        barChart.getData().add(dataSeries1);
        if(this.barChart.getData().size()>3) this.barChart.getData().clear();
        this.mc.getAgente().clearResults();
    }

    public void fillLine() {
        this.lineChart.setAnimated(false);
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("KB used");
        List<ProcessInfo> results = this.mc.getAgente().getProcesses();
        for (ProcessInfo res : results) {
            dataSeries1.getData().add(new XYChart.Data(res.getName(),Integer.parseInt(res.getMemuse())));
        }
        this.lineChart.getData().add(dataSeries1);
        if (this.lineChart.getData().size()>3) {
            Utilities.export_Perf_Binary(results);
        this.lineChart.getData().clear();
        }
        this.mc.getAgente().clearResults();
    }

    public void updateCharts() {
        if (this.mc.getAgente().isBulk()) Utilities.bulkUpdate(this.mc.getAgente());
        else Utilities.walkUpdate(this.mc.getAgente());
    }

    public void refreshTest() { // FUNCA!
        Timeline timeholder = new Timeline(new KeyFrame(Duration.seconds(this.mc.getAgente().getPeriod()), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fillChartv2();
                fillBar();
                fillChart();
                fillLine();
                updateCharts();
            }
        }));
        timeholder.setCycleCount(Timeline.INDEFINITE);
        timeholder.play();
    }

    public void goBack() {
        this.mv.showProcDisplay();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
