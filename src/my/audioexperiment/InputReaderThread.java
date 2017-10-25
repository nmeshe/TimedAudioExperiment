/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.audioexperiment;

import java.util.Scanner;

/**
 *
 * @author nick
 */
public class InputReaderThread extends Thread {

    private AudioExpOneUI audioExperiment;
    private AePlayWave wavPlayer;
    
    public InputReaderThread(AudioExpOneUI aeo, AePlayWave w) {
        audioExperiment = aeo;
        wavPlayer = w;
    }    
    
    public void run() {
        System.out.println("Type 'stop' and hit ENTER to stop a trial round.");
        
        while (true) {

            Scanner scanner = new Scanner(System.in);

            String stopLine = scanner.nextLine();

            if (stopLine.toLowerCase().contains("stop")) {
                System.out.println(" 'stop' detected");
                wavPlayer.stopPlaying();
            }
        }
    }
}

