public class ProcessInfo {

    private String name;
    private String path;
    private String type;
    private String status;
    private String memuse;
    private String cputimeuse;

    public ProcessInfo() {
        this.name=this.path=this.type=this.status="";
    }

    public ProcessInfo(String name) {
        this.name = name;
    }

    public String getCputimeuse() {
        return cputimeuse;
    }

    public void setCputimeuse(String cputimeuse) {
        this.cputimeuse = cputimeuse;
    }

    public String getMemuse() {
        return memuse;
    }

    public void setMemuse(String memuse) {
        this.memuse = memuse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ")
                .append(this.name)
                .append(" Path: ")
                .append(this.path)
                .append(" Type: ")
                .append(this.type)
                .append(" Status: ")
                .append(this.status);
        return sb.toString();
    }

}
