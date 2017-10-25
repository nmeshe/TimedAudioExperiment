package my.audioexperiment;

import java.util.Date;

/**
 *
 * @author nick
 */
public class AePlayTimer extends Thread {
    
    public AePlayTimer(int p, AePlayWave w, AudioExpOneUI aeo) {
        playtimeLength = p;
        wavPlayer = w;
        audioExperiment = aeo;
        playNoise = true;
    }

    public AePlayTimer(int p, AePlayWave w, AudioExpOneUI aeo, boolean pN) {
        playtimeLength = p;
        wavPlayer = w;
        audioExperiment = aeo;
        playNoise = pN;
    }
    
    //length to play clip in seconds
    private int playtimeLength;
    
    //audio player
    private AePlayWave wavPlayer;
    
    //main program
    private AudioExpOneUI audioExperiment;
    
    private boolean success = false;
    
    private boolean playNoise = true;

    public AePlayWave getWavPlayer() {
        return wavPlayer;
    }

    public void setWavPlayer(AePlayWave wavPlayer) {
        this.wavPlayer = wavPlayer;
    }

    public int getPlaytimeLength() {
        return playtimeLength;
    }
    public void setPlaytimeLength(int playtimeLength) {
        this.playtimeLength = playtimeLength;
    }

    
    public void run() {
        System.out.println("Timer task started at:"+new Date());
        if (playNoise) {
            playNoise();
        } else {
            playSilence();
        }
        System.out.println("Timer task finished at:"+new Date());
    }

    private void playNoise() {
        try {
            success = true;
            this.audioExperiment.displayInfoMessage("Playing Noise");
            wavPlayer.startPlaying();
            
            int i=0;
            while (success == true && i<playtimeLength) {
                if (wavPlayer.isPlaying() == false) {
                    success = false;
                }
                this.audioExperiment.assignCountdownClock(playtimeLength - i);
                Thread.sleep(1000);
                i++;
            }
            
            wavPlayer.stopPlaying();
            
            if (success) {
                this.audioExperiment.trialRoundSucceded();
            } else {
                this.audioExperiment.trialRoundFailed();
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void playSilence() {
        try {
            success = true;
            this.audioExperiment.displayInfoMessage("Playing Silence");
            wavPlayer.setShouldPlay(true);
            
            int i=0;
            while (success == true && i<playtimeLength) {
                if (wavPlayer.shouldPlay() == false) {
                    success = false;
                }
                this.audioExperiment.assignCountdownClock(playtimeLength - i);
                Thread.sleep(1000);
                i++;
            }
            
            if (success) {
                this.audioExperiment.trialRoundSucceded();
            } else {
                this.audioExperiment.trialRoundFailed();
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }    
    
}
