import com.google.gson.annotations.Expose;

public class OutputData {
    @Expose
    private String name;

    public OutputData(String name) {
        this.name = name;
    }
}
