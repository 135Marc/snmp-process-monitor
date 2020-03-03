import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class InitialDisplay implements Initializable {
    @FXML
    private ChoiceBox<String> polltype;
    @FXML
    private ChoiceBox<Double> pollperiod;
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private TextField comField;
    @FXML
    private TextField nonRepeaters;
    @FXML
    private TextField maxRepeats;
    @FXML
    private TextField threadN;
    @FXML
    private CheckBox checkBox;

    private MainView main;
    private MainController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        polltype.getItems().add("Walk (GETNEXT)");
        polltype.getItems().add("Bulk (BULKGET)");
        pollperiod.getItems().add(1.0);
        pollperiod.getItems().add(2.0);
        pollperiod.getItems().add(3.0);
        pollperiod.getItems().add(4.0);
        pollperiod.getItems().add(5.0);
        pollperiod.getItems().add(10.0);

    }

    public void init(MainView mv) {
        this.main = mv;
    }

    public void setMainController(MainController mc) {
        this.mainController=mc;
    }

    public void addAgent() throws Exception {

      if(!this.checkBox.isSelected()) this.mainController.initAgent(ipField.getText(),Integer.parseInt(portField.getText()),comField.getText(),true,1);
      else this.mainController.initAgent(ipField.getText(),Integer.parseInt(portField.getText()),comField.getText(),false,Integer.parseInt(this.threadN.getText()));
        if (polltype.getSelectionModel().getSelectedItem().equals("Walk (GETNEXT)")) this.mainController.getAgente().setWalk(true);
        else if (polltype.getSelectionModel().getSelectedItem().equals("Bulk (BULKGET)")) this.mainController.getAgente().setBulk(true);
        if (pollperiod.getSelectionModel()!=null) this.mainController.getAgente().setPeriod(pollperiod.getSelectionModel().getSelectedItem());
        if (!nonRepeaters.isDisabled()) this.mainController.getAgente().setNonreps(Integer.parseInt(nonRepeaters.getText()));
        if (!maxRepeats.isDisabled()) this.mainController.getAgente().setMaxreps(Integer.parseInt(maxRepeats.getText()));
        if (this.threadN.getText()!=null && this.checkBox.isSelected()) {
            this.mainController.getAgente().setMultithreaded(true);
            this.mainController.getAgente().setThreads(Integer.parseInt(threadN.getText()));
        }
        this.main.showProcDisplay();
    }

    public void changeState() {
        if (!(this.polltype.getSelectionModel().isEmpty()) && this.polltype.getSelectionModel().getSelectedItem().equals("Bulk (BULKGET)")) {
            this.nonRepeaters.setDisable(false);
            this.maxRepeats.setDisable(false);
        }
        else {
            this.nonRepeaters.setDisable(true);
            this.maxRepeats.setDisable(true);
        }

    }

    public void clicked() {
        if(threadN.isDisabled()) threadN.setDisable(false);
        else threadN.setDisable(true);
    }

}
