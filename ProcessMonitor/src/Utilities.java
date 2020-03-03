import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utilities {

    public static String translateResults(VariableBinding id, List<OID> oids) {
        String result = "";
        if (id.getOid().startsWith(oids.get(4))) {
            switch (id.getVariable().toString()) {
                case "1" :
                    result="unknown";
                    break;
                case "2" :
                    result="operatingSystem";
                    break;
                case "3":
                    result="deviceDriver";
                    break;
                case "4":
                    result="aplication";
                    break;
                default:
                    break;
            }
        }
        else if (id.getOid().startsWith(oids.get(5))) {
            switch (id.getVariable().toString()) {
                case "1" :
                    result="running";
                    break;
                case "2" :
                    result="runnable";
                    break;
                case "3":
                    result="notRunnable";
                    break;
                case "4":
                    result="Invalid";
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    private static boolean isHex(String path) {
        boolean cond1 = Character.isDigit(path.charAt(0)) || Character.isDigit(path.charAt(1));
        boolean cond2 = Character.isDigit(path.charAt(0)) || Character.isLetter(path.charAt(1));
        boolean cond3 = path.charAt(2)==58;
        return cond1 && cond2 && cond3;
    }

    public static void populate_processes(int i,boolean isWalk,List<String> results,List<ProcessInfo> pi) {
        if (i==0) {
            if(isWalk) results.remove(results.size()-1);
            for (String r : results) {
                pi.add(new ProcessInfo(r));
            }
        }
        else if (i==1) {
            if(isWalk) results.remove(results.size()-1);
            for (int j=0;j<results.size();j++) {
                String path = results.get(j);
                if (isHex(path)) {
                  //  System.out.println(fromHexString(path));
                    pi.get(j).setPath(path);
                } else {
                    pi.get(j).setPath(results.get(j));
                }

            }
        }
        else if (i==2) {
            if(isWalk) results.remove(results.size()-1);
            for (int j=0;j<results.size();j++) {
                pi.get(j).setType(results.get(j));
            }
        }
        else if (i==3){
            if(isWalk) results.remove(results.size()-1);
            for (int j=0;j<results.size();j++) {
                pi.get(j).setStatus(results.get(j));
            }
        }
        else if(i==4) {
            if (isWalk) results.remove(results.size() - 1);
            for (int j = 0; j < results.size(); j++) {
                int val = Integer.parseInt(results.get(j));
                pi.get(j).setCputimeuse(String.valueOf(val / 100));
            }
        }
        else {
            if (isWalk) results.remove(results.size() - 1);
            for (int j = 0; j < results.size(); j++) {
                pi.get(j).setMemuse(results.get(j));
            }
        }
    }

    public static List<OID> create_OIDs() {
        List<OID> oids = new ArrayList<>();
        oids.add(new OID("1.3.6.1.2.1.25.1.6")); // hrSystemProcesses
        oids.add(new OID("1.3.6.1.2.1.25.2.2")); // hrMemorySize
        oids.add(new OID("1.3.6.1.2.1.25.4.2.1.2")); // hrSWRunName
        oids.add(new OID("1.3.6.1.2.1.25.4.2.1.4")); // hrSWRunPath
        oids.add(new OID("1.3.6.1.2.1.25.4.2.1.6")); // hrSWRunType
        oids.add(new OID("1.3.6.1.2.1.25.4.2.1.7")); // hrSWRunStatus
        oids.add(new OID(".1.3.6.1.2.1.25.5.1.1.1")); //hrSWRunPerfCPU
        oids.add(new OID(".1.3.6.1.2.1.25.5.1.1.2")); //hrSWRunPerfMem
        return oids;
    }

    public static List<OID> create_PerfOIDs() {
        List<OID> perfoids = new ArrayList<>();
        perfoids.add(new OID(".1.3.6.1.4.1.2021.4.5")); // memTotalReal
        perfoids.add(new OID(".1.3.6.1.4.1.2021.4.6")); // memAvailReal
        perfoids.add(new OID(".1.3.6.1.4.1.2021.4.11")); // memTotalFree (incl Virtual Memory)
        perfoids.add(new OID(".1.3.6.1.4.1.2021.4.3")); // memTotalSwap (Virtual Memory)
        perfoids.add(new OID(".1.3.6.1.4.1.2021.4.4")); // memAvailSwap (Virtual Memory)
        return perfoids;
    }

    public static ObservableList<PieChart.Data> generate_PerfData(List<String> results) {
        ObservableList<PieChart.Data> datalist = FXCollections.observableArrayList();
        PieChart.Data d1 = new PieChart.Data("Physical Memory",Double.parseDouble(results.get(0)));
        datalist.add(d1);
        PieChart.Data d2 = new PieChart.Data("Available (Physical) Memory",Double.parseDouble(results.get(1)));
        datalist.add(d2);
        PieChart.Data d4 = new PieChart.Data(" Total Available Memory",Double.parseDouble(results.get(2)));
        datalist.add(d4);
        PieChart.Data d5 = new PieChart.Data(" Virtual Memory",Double.parseDouble(results.get(3)));
        datalist.add(d5);
        PieChart.Data d6 = new PieChart.Data(" Available Virtual Memory",Double.parseDouble(results.get(4)));
        datalist.add(d6);
        PieChart.Data d3 = new PieChart.Data("Used (Physical) Memory",Double.parseDouble(results.get(5)));
        datalist.add(d3);
        return datalist;
    }


    public static void walkUpdate(Agent a) {
        List<String> results = a.getResultList();
        List<ProcessInfo> pi = new ArrayList<>();
        int i=0;
        for (OID id : a.getOids().subList(2,8)) {
            if (a.isMultithreaded()) a.asyncWalkMulti(id);
            else a.asyncWalk(id);
            Utilities.populate_processes(i,true,results,pi);
            a.setProcesses(pi);
            i++;
            a.clearResults();
        }
    }

    public static void bulkUpdate(Agent ax) {
        List<String> results = ax.getResultList();
        List<ProcessInfo> pi = new ArrayList<>();
        int i =0;
        for (OID id : ax.getOids().subList(2,8)) {
            if (ax.isMultithreaded()) ax.asyncBulkGetMulti(id);
            else ax.asyncBulkGet(id);
            Utilities.populate_processes(i,false,results,pi);
            ax.setProcesses(pi);
            i++;
            ax.clearResults();
        }
    }


    public static void export_processinfo(List<ProcessInfo> processInfoList) {
            Path ptext = Paths.get("../GR-TP2/Results/" + "results.html");
            try(BufferedWriter bw = Files.newBufferedWriter(ptext)) {
                bw.write("<html>" + "\n");
                bw.write("<body>" + "\n");
                bw.write("<table>" + "\n");
                bw.write("<th>Name</th>" + "\n");
                bw.write("<th>Path</th>" + "\n");
                bw.write("<th>Type</th>" + "\n");
                bw.write("<th>Status</th>" + "\n");
                bw.write("<th>CPU Time(s)</th>" + "\n");
                bw.write("<th>Memory Used(KB)</th>" + "\n");

                for (ProcessInfo pi : processInfoList) {
                    bw.write("<tr>" + "\n");
                    bw.write("<td>" + pi.getName() + "</td>" + "\n");
                    bw.write("<td>" + pi.getPath() + "</td>"  + "\n");
                    bw.write("<td>" + pi.getType() + "</td>"  + "\n");
                    bw.write("<td>" + pi.getStatus() + "</td>" + "\n");
                    bw.write("<td>" + pi.getCputimeuse() + "</td>" + "\n");
                    bw.write("<td>" + pi.getMemuse() + "</td>" + "\n");
                    bw.write("</tr>" + "\n");
                }
                bw.write("</table>" + "\n");
                bw.write("</body>" + "\n");
                bw.write("</html>" + "\n");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }

    public static void export_XML(List<ProcessInfo> processInfoList) {
        Path ptext = Paths.get("../GR-TP2/Results/" + "results.xml");
        try(BufferedWriter bw = Files.newBufferedWriter(ptext)) {
            bw.write("<ProcessInfo>" + "\n");
            for (ProcessInfo pi : processInfoList) {
                bw.write("<Process>" + "\n");
                bw.write("<Name>" + pi.getName() + "</Name>" + "\n");
                bw.write("<Path>" + pi.getPath() + "</Path>"  + "\n");
                bw.write("<Type>" + pi.getType() + "</Type>"  + "\n");
                bw.write("<Status>" + pi.getStatus() + "</Status>" + "\n");
                bw.write("<CPUTime>" + pi.getCputimeuse() + "</CPUTime>" + "\n");
                bw.write("<MemoryUsed>" + pi.getMemuse() + "</MemoryUsed>" + "\n");
                bw.write("</Process>" + "\n");
            }
            bw.write("</ProcessInfo>" + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void export_Binary(List<ProcessInfo> processInfoList) {
        Path ptext = Paths.get("../GR-TP2/Results/" + "results");
        try(BufferedWriter bw = Files.newBufferedWriter(ptext)) {
            bw.write("ProcessInfo" + "\n");
            for (ProcessInfo pi : processInfoList) {
                bw.write("Process" + "\n");
                bw.write("Name: " + pi.getName()  + "\n");
                bw.write("Path: " + pi.getPath()  + "\n");
                bw.write("Type: " + pi.getType()  + "\n");
                bw.write("Status: " + pi.getStatus() + "\n");
                bw.write("CPU Time Used(s): " + pi.getCputimeuse() + "\n");
                bw.write("Memory Used(KB): " + pi.getMemuse() + "\n");
            }
            bw.write("End of ProcessInfo" + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void write_bin_CPU(List<ProcessInfo> ordered,BufferedWriter bw) {
        try{
            bw.write("ProcessInfo" + "\n");
            bw.write("\n");
            bw.write("==============================" + "\n");
            bw.write("ORDERED BY MOST CPU TIME USED" + "\n");
            bw.write("==============================" + "\n");
            bw.write("\n");
            for (ProcessInfo pi : ordered) {
                bw.write("Process" + "\n");
                bw.write("Name: " + pi.getName()  + "\n");
                bw.write("Path: " + pi.getPath()  + "\n");
                bw.write("Type: " + pi.getType()  + "\n");
                bw.write("Status: " + pi.getStatus() + "\n");
                bw.write("CPU Time Used(s) : " + pi.getCputimeuse() + "\n");
                bw.write("Memory Used(KB) : " + pi.getMemuse() + "\n");
            }
            bw.write("End of ProcessInfo" + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void write_bin_Mem(List<ProcessInfo> ordered,BufferedWriter bw) {
        try{
            bw.write("ProcessInfo" + "\n");
            bw.write("\n");
            bw.write("==============================" + "\n");
            bw.write("ORDERED BY MOST MEMORY USED" + "\n");
            bw.write("==============================" + "\n");
            bw.write("\n");
            for (ProcessInfo pi : ordered) {
                bw.write("Process" + "\n");
                bw.write("Name: " + pi.getName()  + "\n");
                bw.write("Path: " + pi.getPath()  + "\n");
                bw.write("Type: " + pi.getType()  + "\n");
                bw.write("Status: " + pi.getStatus() + "\n");
                bw.write("CPU Time Used(s) : " + pi.getCputimeuse() + "\n");
                bw.write("Memory Used(KB) : " + pi.getMemuse() + "\n");
            }
            bw.write("End of ProcessInfo" + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void export_Perf_Binary(List<ProcessInfo> processInfoList) {
        File f = new File("../GR-TP2/Results/" + "perfresults");
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsolutePath(),true))) {
            List<ProcessInfo> ordered = processInfoList.stream().sorted(new CPUComparator()).collect(Collectors.toList());
            bw.write("TIMESTAMP = " + LocalDateTime.now().toString() +"\n");
            write_bin_CPU(ordered,bw);
            ordered = processInfoList.stream().sorted(new MemComparator()).collect(Collectors.toList());
            write_bin_Mem(ordered,bw);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
