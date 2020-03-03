import java.util.Comparator;

public class CPUComparator implements Comparator<ProcessInfo> {

    @Override
    public int compare(ProcessInfo processInfo, ProcessInfo t1) {
        if (Integer.valueOf(processInfo.getCputimeuse()) < Integer.valueOf(t1.getCputimeuse())) return 1;
        if (Integer.valueOf(processInfo.getCputimeuse()) > Integer.valueOf(t1.getCputimeuse())) return -1;
        return (Integer.valueOf(t1.getCputimeuse()).compareTo(Integer.valueOf(processInfo.getCputimeuse())));
    }
}
