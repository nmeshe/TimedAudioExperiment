package my.audioexperiment;

import java.util.Date;

/**
 *
 * @author nick
 */

enum Mode {NOISE, SILENCE}

public class AePlayTimer extends Thread {
    
    public AePlayTimer(int p, AePlayWave w, AudioExperimentUI aeo) {
        playtimeLength = p;
        wavPlayer = w;
        audioExperiment = aeo;
        playMode = Mode.NOISE;
    }

    public AePlayTimer(int p, AePlayWave w, AudioExperimentUI aeo, boolean pM) {
        playtimeLength = p;
        wavPlayer = w;
        audioExperiment = aeo;
        playMode = (pM ? Mode.NOISE : Mode.SILENCE);
    }
    
    //length to play clip in seconds
    private int playtimeLength;
    
    //audio player
    private AePlayWave wavPlayer;
    
    //main program
    private AudioExperimentUI audioExperiment;
    
    private boolean success = false;
    
    private Mode playMode;

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
        
        try {
            success = true;
            this.displayModeInfoMessage();
            this.startWavPlayer();
            
            int i=0;
            while (success == true && i<playtimeLength) {
                if (this.hasWavPlayerBeenStopped()) {
                    success = false;
                }
                this.audioExperiment.assignCountdownClock(playtimeLength - i);
                Thread.sleep(1000);
                i++;
            }
            
            this.stopWavPlayer();
            
            if (success) {
                this.audioExperiment.trialRoundSucceded();
            } else {
                this.audioExperiment.trialRoundFailed();
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }        

        System.out.println("Timer task finished at:"+new Date());
    }
    
    private void displayModeInfoMessage() {
        if (playMode == Mode.NOISE) {
            this.audioExperiment.displayInfoMessage("Playing Noise");
        } else {
            this.audioExperiment.displayInfoMessage("Playing Silence");
        }
    }
    
    private void startWavPlayer() {
        if (playMode == Mode.NOISE) {
            wavPlayer.startPlaying();
        } else {
            wavPlayer.setShouldPlay(true);
        }
    }
    
    private boolean hasWavPlayerBeenStopped() {
        if (playMode == Mode.NOISE) {
            return (wavPlayer.isPlaying() == false);
        } else {
            return (wavPlayer.shouldPlay() == false);
        }
    }
    
    private void stopWavPlayer() {
        if (playMode == Mode.NOISE) {
            wavPlayer.stopPlaying();
        }
    }
        
}
