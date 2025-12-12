
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Recipe {
    public Map<Integer, String> Inputs = new HashMap<>();
    public Map<Integer, String> Outputs = new HashMap<>();
    public ArrayList<Float> InputAmounts = new ArrayList<>();
    public ArrayList<Float> OutputAmounts = new ArrayList<>();
    public String Machine;
    public float Time;
    public float MF;
    public float Efficiency;
    public void CalculateEfficiency(Integer OutputIndex) {
        Efficiency = OutputAmounts.get(OutputIndex) / Time;
    }
}
