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
    public int experimentNumber;
    public String trialRound;
    public float noiseDollarAmt;
    public float silenceDollarAmt;
    public int noiseTimeAmt;
    public int silenceTimeAmt;
    public String selectedOption;
    public float bankAmount = 0;
     
    public static final String SELECTED_OPTION_NOISE      = "NOISE";
    public static final String SELECTED_OPTION_SILENCE    = "SILENCE";

    public TrialBlock(String pIdentifier, int pExperimentNumber, String pTrialRound, 
            float pNoiseDollars, float pSilenceDollars, 
            int pNoiseTime, int pSilenceTime, String pSelectedOption, float pBankAmount) {
        indentifier = pIdentifier;
        experimentNumber = pExperimentNumber;
        trialRound = pTrialRound;
        noiseDollarAmt = pNoiseDollars;
        silenceDollarAmt = pSilenceDollars;
        noiseTimeAmt = pNoiseTime;
        silenceTimeAmt = pSilenceTime;
        selectedOption = pSelectedOption;
        bankAmount = pBankAmount;            
    }
    
    public TrialBlock copy() { 
        return  new TrialBlock(
                indentifier,
                experimentNumber,
                trialRound,
                noiseDollarAmt,
                silenceDollarAmt,
                noiseTimeAmt,
                silenceTimeAmt,
                selectedOption,
                bankAmount);
    } 
    
    public String toString() { 
        return  indentifier + "," +
                experimentNumber + "," +
                trialRound + "," +
                noiseDollarAmt + "," +
                silenceDollarAmt + "," +
                noiseTimeAmt + "," +
                silenceTimeAmt + "," +              
                selectedOption + "," +
                bankAmount + "";
    } 
    
    public String toStringWithLabels() { 
        return  "indentifier=" + indentifier + "," +
                "experimentNumber=" + experimentNumber + "," +
                "trialRound=" + trialRound + "," +
                "noiseDollarAmount=" + noiseDollarAmt + "," +
                "silenceDollarAmount=" + silenceDollarAmt + "," +
                "noiseTimeAmount=" + noiseTimeAmt + "," +
                "silenceTimeAmount=" + silenceTimeAmt + "," +                 
                "selectedOption=" + selectedOption + "," +
                "bankAmount=" + bankAmount + "";
    } 
}
