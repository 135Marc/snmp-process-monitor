import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainView extends Application {

    private Stage window;
    private MainController mc;

    public static void main(String[] args) {
        launch(args);
    }

    public  MainView() {
        this.window = new Stage();
    }


    @Override
    public void start(Stage primaryStage) {
        mc = new MainController(this);
        showInitial();
    }

    public void showInitial() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("initialdisplay.fxml"));
            Pane view = loader.load();
            Scene nova = new Scene(view,589,443);
            window.setScene(nova);
            InitialDisplay idis = loader.getController();
            idis.init(this);
            idis.setMainController(mc);
            mc.setInitialDisplay(idis);
            window.setTitle("Initialize Agent");
            window.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void showProcDisplay() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("processdisplay.fxml"));
            Pane view = loader.load();
            Scene nova = new Scene(view,951,675);
            window.setScene(nova);
            ProcessDisplay pdis = loader.getController();
            pdis.init(this);
            pdis.setMainController(mc);
            mc.setProcessDisplay(pdis);
            window.setTitle("Process Monitor");
            window.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void showPerfDisplay() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("perfdisplay.fxml"));
            Pane view = loader.load();
            Scene nova = new Scene(view,1252,824);
            window.setScene(nova);
            PerfDisplay perfdis = loader.getController();
            perfdis.init(this);
            perfdis.setMainController(mc);
            mc.setPerfDisplay(perfdis);
            window.setTitle("Performance Analysis");
            window.show();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
