/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.audioexperiment;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class TrialData {
    
    private String identifier;
    private String dirPrefix = "";
    private String filePath;
    private String EXTENSION = ".csv";
    private ArrayList<TrialBlock> trialList;
    
    public TrialData(String id, String directory) {
        identifier = id;
        dirPrefix = directory;
        trialList = new ArrayList<TrialBlock>();
        this.startOutputFile();
    }
    
    public void writeToOutputFile(String message) {
        try {
            Files.write(Paths.get(getFilePath()), (message+"\n").getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getFilePath() {
        
        if (filePath == null || filePath.length() == 0) {
            if (dirPrefix != null && dirPrefix.length() > 0) {
                filePath = dirPrefix + "/";
            }
            filePath += identifier + "_" + System.currentTimeMillis() + EXTENSION;
        }
        
        return filePath;
    }
    
    private void startOutputFile() {
        
        String lFilePath = this.getFilePath();
               
        System.out.println("saving output file: " + lFilePath);
        
        Path lPath = Paths.get(lFilePath);

        try {
            if (Files.exists(lPath) == false) {
                Files.createDirectories(lPath.getParent());
                Files.createFile(lPath);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
                    
        String headerRow = 
                "indentifier," + 
                "experimentNumber," +
                "trialRound," + 
                "noiseDollarAmount," + 
                "silenceDollarAmount," + 
                "noiseTimeAmount," +
                "silenceTimeAmount," +
                "selectedOption," + 
                "bankAmount";
        
        this.writeToOutputFile(headerRow);
    }
    
    public TrialBlock getLastTrialBlock() {
        
        int blockCount = this.trialList.size();
        
        if (blockCount > 0) {
            return this.trialList.get(blockCount - 1);
        } else {
            return null;
        }
    }
    
    public void addTrialBlock(TrialBlock tb) {
        trialList.add(tb);
        
        if (tb != null) {       
            System.out.println(tb.toStringWithLabels());
            this.writeToOutputFile(tb.toString());
        }
    }
    
    public void addTrialBlock(int pExperimentNumber, String pTrialRound, 
            float pNoiseDollars, float pSilenceDollar, 
            int pNoiseTime, int pSilenceTime, String pSelectedOption, float pBankAmount) {
      
        TrialBlock tb = new TrialBlock(identifier, pExperimentNumber, pTrialRound, 
                pNoiseDollars, pSilenceDollar, 
                pNoiseTime, pSilenceTime, pSelectedOption, pBankAmount);
        this.addTrialBlock(tb);
    }
    
}
