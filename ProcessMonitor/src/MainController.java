import javafx.fxml.Initializable;
import org.snmp4j.smi.OID;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // SNMP Logic Related Classes
    private Agent agente;
    // Views
    private MainView mview;
    private InitialDisplay idis;
    private ProcessDisplay pdis;
    private PerfDisplay perf;

    public MainController(MainView mv) {
        this.mview = mv;
    }

    public void initAgent(String ip,int port,String com,boolean multi,int threads) throws Exception {
        agente = new Agent(ip,port,com,multi,threads);
    }


    public Agent getAgente() {
        return this.agente;
    }

    public void setInitialDisplay(InitialDisplay id) {
        this.idis=id;
    }

    public void setProcessDisplay(ProcessDisplay pdis) { this.pdis=pdis;}

    public void setPerfDisplay (PerfDisplay perfdis) { this.perf=perfdis;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
