package Processor;

import java.util.ArrayList;
public class Recipe {
    public ArrayList<String> Inputs = new ArrayList<>();
    public ArrayList<String> Outputs = new ArrayList<>();
    public ArrayList<Float> InputAmounts = new ArrayList<>();
    public ArrayList<Float> OutputAmounts = new ArrayList<>();
    public String Machine;
    public Float Time;
    public String MF;
    public Float Efficiency;
    public Boolean equals(Recipe Other) {
        return (Machine.equals(Other.Machine))
            && (Time.equals(Other.Time))
            && (MF.equals(Other.MF))
            && (Efficiency.equals(Other.Efficiency))
            && (Inputs.equals(Other.Inputs))
            && (InputAmounts.equals(Other.InputAmounts))
            && (Outputs.equals(Other.Outputs))
            && (OutputAmounts.equals(Other.OutputAmounts));
    }
}
