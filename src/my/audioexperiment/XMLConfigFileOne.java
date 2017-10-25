package my.audioexperiment;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

/**
 *
 * @author nick
 */
public class XMLConfigFileOne {
    
    private float BaseMoneyMagnitude;
    private int DurationDelay;
    private String WavFile;
    private String configFilePath;
    private float MaxMoneyMagnitude;
    private float MinMoneyMagnitude;
    private int MaxStops;
    private int MaxRounds;
    private boolean ShowCountdownClock;
    private boolean ShowRoundNumber;
    private boolean ShowMessages;
    private boolean ShowStopButton;
    private String NoiseValueLabel;
    private String NoiseButtonLabel;
    private String SilenceValueLabel;
    private String SilenceButtonLabel;

    private String ResultsFolder;

    public String getResultsFolder() {
        return ResultsFolder;
    }

    public void setResultsFolder(String ResultsFolder) {
        this.ResultsFolder = ResultsFolder;
    }

    private static String BASE_MONEY_MAGNITUDE = "BaseMoneyMagnitude"; //starting amount of money for each trial
    private static String MIN_MONEY_MAGNITUDE = "MinMoneyMagnitude"; //minimum amount of money
    private static String MAX_MONEY_MAGNITUDE = "MaxMoneyMagnitude"; //maximum amount of money
    private static String DURATION_DELAY = "DurationDelay"; //length of each trail
    private static String WAV_FILE = "WavFile"; //location of WAV audio file used for trials
    private static String RESULTS_FOLDER = "ResultsFolder"; //location of where to write out results
    private static String MAX_STOPS = "MaxStops"; //number of times a participant is allowed to stop a trial
    private static String MAX_ROUNDS = "MaxRounds"; //number of trials in a block, not including the initial test round
    private static String SHOW_COUNTDOWN_CLOCK = "ShowCountdownClock"; //show (true) or hide (false) a countdown clock on the screen
    private static String SHOW_ROUND_NUMBER = "ShowRoundNumber"; //show (true) or hide (false) display of the round number on the screen, will still appear in the terminal log 
    private static String SHOW_MESSAGES = "ShowMessages"; //show (true) or hide (false) messages on the screen, will still appear in the terminal log
    private static String SHOW_STOP_BUTTON = "ShowStopButton"; //show (true) or hide (false) the Stop button
    private static String NOISE_LABEL_TEXT = "NoiseValueLabel"; //label on textfield for noise value
    private static String NOISE_BUTTON_TEXT = "NoiseButtonLabel"; //label on button to play noise
    private static String SILENCE_LABEL_TEXT = "SilenceValueLabel"; //label on textfield for silence value
    private static String SILENCE_BUTTON_TEXT = "SilenceButtonLabel"; //label on button to silence noise

    public String getSilenceButtonLabel() {
        return SilenceButtonLabel;
    }
    public void setSilenceButtonLabel(String SilenceButtonLabel) {
        this.SilenceButtonLabel = SilenceButtonLabel;
    }

    public String getSilenceValueLabel() {
        return SilenceValueLabel;
    }
    public void setSilenceValueLabel(String SilenceValueLabel) {
        this.SilenceValueLabel = SilenceValueLabel;
    }

    public String getNoiseButtonLabel() {
        return NoiseButtonLabel;
    }
    public void setNoiseButtonLabel(String NoiseButtonLabel) {
        this.NoiseButtonLabel = NoiseButtonLabel;
    }

    public String getNoiseValueLabel() {
        return NoiseValueLabel;
    }
    public void setNoiseValueLabel(String NoiseValueLabel) {
        this.NoiseValueLabel = NoiseValueLabel;
    }
    
    public boolean showStopButton() {
        return ShowStopButton;
    }
    public void setShowStopButton(boolean ShowStopButton) {
        this.ShowStopButton = ShowStopButton;
    }
    
    public boolean showMessages() {
        return ShowMessages;
    }
    public void setShowMessages(boolean ShowMessages) {
        this.ShowMessages = ShowMessages;
    }


    public boolean showRoundNumber() {
        return ShowRoundNumber;
    }
    public void setShowRoundNumber(boolean ShowRoundNumber) {
        this.ShowRoundNumber = ShowRoundNumber;
    }


    public boolean showCountdownClock() {
        return ShowCountdownClock;
    }
    public void setShowCountdownClock(boolean ShowCountdownClock) {
        this.ShowCountdownClock = ShowCountdownClock;
    }

    public float getMinMoneyMagnitude() {
        return MinMoneyMagnitude;
    }
    public void setMinMoneyMagnitude(float MinMoneyMagnitude) {
        this.MinMoneyMagnitude = MinMoneyMagnitude;
    }
    
    public float getMaxMoneyMagnitude() {
        return MaxMoneyMagnitude;
    }
    public void setMaxMoneyMagnitude(float MaxMoneyMagnitude) {
        this.MaxMoneyMagnitude = MaxMoneyMagnitude;
    }

    public String getConfigFilePath() {
        return configFilePath;
    }
    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }
    
    public float getBaseMoneyMagnitude() {
        return BaseMoneyMagnitude;
    }
    public void setBaseMoneyMagnitude(float b) {
        this.BaseMoneyMagnitude = b;
    }  

    public int getDurationDelay() {
        return DurationDelay;
    }
    public void setDurationDelay(int d) {
        this.DurationDelay = d;
    }

    public String getWavFile() {
        return WavFile;
    }
    public void setWavFile(String w) {
        this.WavFile = w;
    }    
    
    public int getMaxRounds() {
        return MaxRounds;
    }
    public void setMaxRounds(int MaxRounds) {
        this.MaxRounds = MaxRounds;
    }

    public int getMaxStops() {
        return MaxStops;
    }
    public void setMaxStops(int MaxStops) {
        this.MaxStops = MaxStops;
    }
    
    public XMLConfigFileOne(String f) {
        
        this.configFilePath = f;
        this.setup();
    }
    
    public void setup() {
        try {

            File fXmlFile = new File(this.configFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            
            //STORE AND PRINT OUT FILE LOCATIONS
            this.WavFile = doc.getDocumentElement().getElementsByTagName(WAV_FILE).item(0).getTextContent();
            System.out.println("WavFile: " + this.WavFile);                   
            
            this.ResultsFolder = doc.getDocumentElement().getElementsByTagName(RESULTS_FOLDER).item(0).getTextContent();
            System.out.println("ResultsFolder: " + this.ResultsFolder);    
            System.out.println();
            
            //STORE AND PRINT OUT MAIN PROGRAM OPTIONS
            String lBaseMoneyMagnitude = doc.getDocumentElement().getElementsByTagName(BASE_MONEY_MAGNITUDE).item(0).getTextContent();
            this.BaseMoneyMagnitude = Float.parseFloat(lBaseMoneyMagnitude);
            System.out.println("BaseMoneyMagnitude: " + this.BaseMoneyMagnitude);
            
            String lMaxMoneyMagnitude = doc.getDocumentElement().getElementsByTagName(MAX_MONEY_MAGNITUDE).item(0).getTextContent();
            this.MaxMoneyMagnitude = Float.parseFloat(lMaxMoneyMagnitude);
            System.out.println("MaxMoneyMagnitude: " + this.MaxMoneyMagnitude);

            String lMinMoneyMagnitude = doc.getDocumentElement().getElementsByTagName(MIN_MONEY_MAGNITUDE).item(0).getTextContent();
            this.MinMoneyMagnitude = Float.parseFloat(lMinMoneyMagnitude);
            System.out.println("MinMoneyMagnitude: " + this.MinMoneyMagnitude);

            String lMaxStops = doc.getDocumentElement().getElementsByTagName(MAX_STOPS).item(0).getTextContent();
            this.MaxStops = Integer.parseInt(lMaxStops);
            System.out.println("MaxStops: " + this.MaxStops);

            String lMaxRounds = doc.getDocumentElement().getElementsByTagName(MAX_ROUNDS).item(0).getTextContent();
            this.MaxRounds = Integer.parseInt(lMaxRounds);
            System.out.println("MaxRounds: " + this.MaxRounds);
            
            String lDurationDelay = doc.getDocumentElement().getElementsByTagName(DURATION_DELAY).item(0).getTextContent();
            this.DurationDelay = Integer.parseInt(lDurationDelay);
            System.out.println("DurationDelay: " + this.DurationDelay);            
            System.out.println();
            
            
            //STORE AND PRINT OUT DISPLAY OPTIONS
            String lShowCountdownClock = doc.getDocumentElement().getElementsByTagName(SHOW_COUNTDOWN_CLOCK).item(0).getTextContent();
            this.ShowCountdownClock = Boolean.parseBoolean(lShowCountdownClock);
            System.out.println("ShowCountdownClock: " + this.ShowCountdownClock);
            
            String lShowRoundNumber = doc.getDocumentElement().getElementsByTagName(SHOW_ROUND_NUMBER).item(0).getTextContent();
            this.ShowRoundNumber = Boolean.parseBoolean(lShowRoundNumber);
            System.out.println("ShowRoundNumber: " + this.ShowRoundNumber);

            String lShowStopButton = doc.getDocumentElement().getElementsByTagName(SHOW_STOP_BUTTON).item(0).getTextContent();
            this.ShowStopButton = Boolean.parseBoolean(lShowStopButton);
            System.out.println("ShowStopButton: " + this.ShowStopButton);
            
            String lShowMessages = doc.getDocumentElement().getElementsByTagName(SHOW_MESSAGES).item(0).getTextContent();
            this.ShowMessages = Boolean.parseBoolean(lShowMessages);
            System.out.println("ShowMessages: " + this.ShowMessages);

            this.NoiseValueLabel = doc.getDocumentElement().getElementsByTagName(NOISE_LABEL_TEXT).item(0).getTextContent();
            System.out.println("NoiseValueLabel: \"" + this.NoiseValueLabel + "\"");
    
            this.NoiseButtonLabel = doc.getDocumentElement().getElementsByTagName(NOISE_BUTTON_TEXT).item(0).getTextContent();
            System.out.println("NoiseButtonLabel: \"" + this.NoiseButtonLabel + "\"");
    
            this.SilenceValueLabel = doc.getDocumentElement().getElementsByTagName(SILENCE_LABEL_TEXT).item(0).getTextContent();
            System.out.println("SilenceValueLabel: \"" + this.SilenceValueLabel + "\"");
    
            this.SilenceButtonLabel = doc.getDocumentElement().getElementsByTagName(SILENCE_BUTTON_TEXT).item(0).getTextContent();
            System.out.println("SilenceButtonLabel: \"" + this.SilenceButtonLabel + "\"");
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
