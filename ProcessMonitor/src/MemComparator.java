import java.util.Comparator;

public class MemComparator implements Comparator<ProcessInfo> {

    @Override
    public int compare(ProcessInfo processInfo, ProcessInfo t1) {
        if (Integer.valueOf(processInfo.getMemuse()) < Integer.valueOf(t1.getMemuse())) return 1;
        if (Integer.valueOf(processInfo.getMemuse()) > Integer.valueOf(t1.getMemuse())) return -1;
        return Integer.valueOf(t1.getMemuse()).compareTo(Integer.valueOf(processInfo.getMemuse()));
    }
}
