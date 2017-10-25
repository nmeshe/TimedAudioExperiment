package my.audioexperiment;

import java.io.File; 
import java.io.IOException; 
import javax.sound.sampled.AudioFormat; 
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.DataLine; 
import javax.sound.sampled.FloatControl; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.SourceDataLine; 
import javax.sound.sampled.UnsupportedAudioFileException; 
 
public class AePlayWave { 
 
    private String filename;
 
    private Balance currentBalance;
    
    private boolean currentlyPlaying = false;
    private boolean shouldPlay = true;
 
    private final int EXTERNAL_BUFFER_SIZE = 1024; //1kb
    //524288 = 128Kb 
 
    enum Balance { 
        LEFT, RIGHT, NORMAL
    };
 
    public AePlayWave(String wavfile) { 
        filename = wavfile;
        currentBalance = Balance.NORMAL;
    } 
 
    public AePlayWave(String wavfile, Balance b) { 
        filename = wavfile;
        currentBalance = b;
    } 
    
    public void startPlaying() { 
        if (currentlyPlaying == false) {
            shouldPlay = true;
            currentlyPlaying = true;
            
            new AePlayWaveThread().start();
        }
    }
    
    public void stopPlaying() {
        shouldPlay = false;
    }

    public boolean shouldPlay() {
        return shouldPlay;
    }
    
    public void setShouldPlay(boolean b) {
        shouldPlay = b;
    } 
    
    public boolean isPlaying() {
        return shouldPlay && currentlyPlaying;
    }
    
    private class AePlayWaveThread extends Thread {
        public void run() { 
 
            File soundFile = new File(filename);
            if (!soundFile.exists()) { 
                System.err.println("Wave file not found: " + filename);
                System.out.println("Working Directory: " + System.getProperty("user.dir"));
                return;
            } 
            
            while (shouldPlay) {
                
                AudioInputStream audioInputStream = null;
                try { 
                    audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                } catch (UnsupportedAudioFileException e1) { 
                    e1.printStackTrace();
                    return;
                } catch (IOException e1) { 
                    e1.printStackTrace();
                    return;
                } 

                AudioFormat format = audioInputStream.getFormat();
                SourceDataLine auline = null;
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                try { 
                    auline = (SourceDataLine) AudioSystem.getLine(info);
                    auline.open(format);
                } catch (LineUnavailableException e) { 
                    e.printStackTrace();
                    return;
                } catch (Exception e) { 
                    e.printStackTrace();
                    return;
                } 

                if (auline.isControlSupported(FloatControl.Type.PAN)) { 
                    FloatControl pan = (FloatControl) auline
                            .getControl(FloatControl.Type.PAN);
                    if (currentBalance == Balance.RIGHT) 
                        pan.setValue(1.0f);
                    else if (currentBalance == Balance.LEFT) 
                        pan.setValue(-1.0f);
                } 

                auline.start();
                int nBytesRead = 0;
                byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

                try { 
                    while (shouldPlay && nBytesRead != -1) { 
                        nBytesRead = audioInputStream.read(abData, 0, abData.length);
                        if (nBytesRead >= 0) 
                            auline.write(abData, 0, nBytesRead);
                    } 
                } catch (IOException e) { 
                    e.printStackTrace();
                    shouldPlay = true;
                    return;
                } finally { 
                    auline.drain();
                    auline.close();
                } 
            }
            shouldPlay = true;
            currentlyPlaying = false;
        }
    } 
} 