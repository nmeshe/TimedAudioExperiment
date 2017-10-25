/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.audioexperiment;

/**
 *
 * @author nick
 */
public class TrialBlock {
    public String indentifier;
    public String trialRound;
    public int durationDelay;
    public float noiseAmt;
    public float silenceAmt;
    public String selectedOption;
    public float bankAmount = 0;
     
    public static final String SELECTED_OPTION_NOISE      = "NOISE";
    public static final String SELECTED_OPTION_SILENCE    = "SILENCE";

    public TrialBlock(String i, String t, int d, float n, float si, String se, float b) {
        indentifier = i;
        trialRound = t;
        durationDelay = d;
        noiseAmt = n;
        silenceAmt = si;
        selectedOption = se;
        bankAmount = b;            
    }
    
    public TrialBlock copy() { 
        return  new TrialBlock(
                indentifier,
                trialRound,
                durationDelay,
                noiseAmt,
                silenceAmt,
                selectedOption,
                bankAmount);
    } 
    
    public String toString() { 
        return  indentifier + "," +
                trialRound + "," +
                durationDelay + "," +
                noiseAmt + "," +
                silenceAmt + "," +
                selectedOption + "," +
                bankAmount + "";
    } 
    
    public String toStringWithLabels() { 
        return  "indentifier=" + indentifier + "," +
                "trialRound=" + trialRound + "," +
                "durationDelay=" + durationDelay + "," +
                "noiseAmount=" + noiseAmt + "," +
                "silenceAmount=" + silenceAmt + "," +
                "selectedOption=" + selectedOption + "," +
                "bankAmount=" + bankAmount + "";
    } 
}
