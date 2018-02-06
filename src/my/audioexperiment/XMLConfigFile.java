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
public class XMLConfigFile {
    
    private ExperimentNumber ExperimentNumber;
    private float BaseMoneyMagnitude;
    private int BaseTimeMagnitude;
    private String WavFile;
    private String configFilePath;
    private float MaxMoneyMagnitude;
    private float MinMoneyMagnitude;
    private int MaxTimeMagnitude;
    private int MinTimeMagnitude;    
    private int MaxStops;
    private int MaxRounds;
    private boolean ShowCountdownClock;
    private boolean ShowRoundNumber;
    private boolean ShowMessages;
    private boolean ShowStopButton; 
    private boolean ShowMoneyValues;
    private boolean ShowTimeLengths;
    private String NoiseValueLabel;
    private String NoiseLengthLabel;
    private String NoiseButtonLabel;
    private String SilenceValueLabel;
    private String SilenceLengthLabel;
    private String SilenceButtonLabel;    

    private String ResultsFolder;

    public String getResultsFolder() {
        return ResultsFolder;
    }

    public void setResultsFolder(String ResultsFolder) {
        this.ResultsFolder = ResultsFolder;
    }

    private static String EXPERIMENT_NUMBER = "ExperimentNumber"; //experiment one, two, or three
    private static String BASE_MONEY_MAGNITUDE = "BaseMoneyMagnitude"; //starting amount of money for each trial
    private static String MIN_MONEY_MAGNITUDE = "MinMoneyMagnitude"; //minimum amount of money
    private static String MAX_MONEY_MAGNITUDE = "MaxMoneyMagnitude"; //maximum amount of money
    private static String BASE_TIME_MAGNITUDE = "BaseTimeMagnitude"; //starting time length of each trail
    private static String MIN_TIME_MAGNITUDE = "MinTimeMagnitude"; //minimum amount of money
    private static String MAX_TIME_MAGNITUDE = "MaxTimeMagnitude"; //maximum amount of money
    private static String WAV_FILE = "WavFile"; //location of WAV audio file used for trials
    private static String RESULTS_FOLDER = "ResultsFolder"; //location of where to write out results
    private static String MAX_STOPS = "MaxStops"; //number of times a participant is allowed to stop a trial
    private static String MAX_ROUNDS = "MaxRounds"; //number of trials in a block, not including the initial test round
    private static String SHOW_COUNTDOWN_CLOCK = "ShowCountdownClock"; //show (true) or hide (false) a countdown clock on the screen
    private static String SHOW_ROUND_NUMBER = "ShowRoundNumber"; //show (true) or hide (false) display of the round number on the screen, will still appear in the terminal log 
    private static String SHOW_MESSAGES = "ShowMessages"; //show (true) or hide (false) messages on the screen, will still appear in the terminal log
    private static String SHOW_STOP_BUTTON = "ShowStopButton"; //show (true) or hide (false) the Stop button
    private static String SHOW_MONEY_VALUES = "ShowMoneyValues"; //show (true) or hide (false) the value of each button click
    private static String SHOW_TIME_LENGTHS = "ShowTimeLengths"; //show (true) or hide (false) the time length of each button click
    private static String NOISE_VALUE_LABEL_TEXT = "NoiseValueLabel"; //label on textfield for noise value
    private static String NOISE_LENGTH_LABEL_TEXT = "NoiseLengthLabel"; //label on textfield for noise length
    private static String NOISE_BUTTON_TEXT = "NoiseButtonLabel"; //label on button to play noise
    private static String SILENCE_VALUE_LABEL_TEXT = "SilenceValueLabel"; //label on textfield for silence value
    private static String SILENCE_LENGTH_LABEL_TEXT = "SilenceLengthLabel"; //label on textfield for silence length
    private static String SILENCE_BUTTON_TEXT = "SilenceButtonLabel"; //label on button to silence noise

    public ExperimentNumber getExperimentNumber() {
        return ExperimentNumber;
    }
    
    public void setExperimentNumber(ExperimentNumber experimentNumber) {
        this.ExperimentNumber = experimentNumber;
    }

    public int getExperimentNumberAsInt() {
        if (this.ExperimentNumber == ExperimentNumber.ONE) return 1;
        if (this.ExperimentNumber == ExperimentNumber.TWO) return 2;
        if (this.ExperimentNumber == ExperimentNumber.THREE) return 3;
        return -1;
    }
    
    public void setExperimentNumberFromInt(int experimentNumberInt) {
        
        if (experimentNumberInt == 1)  {
            this.ExperimentNumber = ExperimentNumber.ONE;
        } else if (experimentNumberInt == 2) {
            this.ExperimentNumber = ExperimentNumber.TWO;
        } else if (experimentNumberInt == 3) {
            this.ExperimentNumber = ExperimentNumber.THREE;
        } else {
            this.ExperimentNumber = ExperimentNumber.UNKNOWN;
        }       
    }    
    
    public String getSilenceButtonLabel() {
        return SilenceButtonLabel;
    }
    public void setSilenceButtonLabel(String silenceButtonLabel) {
        this.SilenceButtonLabel = silenceButtonLabel;
    }

    public String getSilenceValueLabel() {
        return SilenceValueLabel;
    }
    public void setSilenceValueLabel(String silenceValueLabel) {
        this.SilenceValueLabel = silenceValueLabel;
    }

    public String getSilenceLengthLabel() {
        return SilenceLengthLabel;
    }
    public void setSilenceLengthLabel(String silenceLengthLabel) {
        this.SilenceLengthLabel = silenceLengthLabel;
    }

    public String getNoiseButtonLabel() {
        return NoiseButtonLabel;
    }
    public void setNoiseButtonLabel(String noiseButtonLabel) {
        this.NoiseButtonLabel = noiseButtonLabel;
    }

    public String getNoiseValueLabel() {
        return NoiseValueLabel;
    }
    public void setNoiseValueLabel(String noiseValueLabel) {
        this.NoiseValueLabel = noiseValueLabel;
    }

    public String getNoiseLengthLabel() {
        return NoiseLengthLabel;
    }
    public void setNoiseLengthLabel(String noiseLengthLabel) {
        this.NoiseLengthLabel = noiseLengthLabel;
    }
    
    public boolean showStopButton() {
        return ShowStopButton;
    }
    public void setShowStopButton(boolean showStopButton) {
        this.ShowStopButton = showStopButton;
    }
    
    public boolean showMessages() {
        return ShowMessages;
    }
    public void setShowMessages(boolean showMessages) {
        this.ShowMessages = showMessages;
    }


    public boolean showRoundNumber() {
        return ShowRoundNumber;
    }
    public void setShowRoundNumber(boolean showRoundNumber) {
        this.ShowRoundNumber = showRoundNumber;
    }


    public boolean showCountdownClock() {
        return ShowCountdownClock;
    }
    public void setShowCountdownClock(boolean showCountdownClock) {
        this.ShowCountdownClock = showCountdownClock;
    }

    public boolean showMoneyValues() {
        return ShowMoneyValues;
    }
    public void setShowMoneyValues(boolean showMoneyValues) {
        this.ShowMoneyValues = showMoneyValues;
    }

    public boolean showTimeLengths() {
        return ShowTimeLengths;
    }
    public void setShowTimeLengths(boolean showTimeLengths) {
        this.ShowTimeLengths = showTimeLengths;
    }

    public float getMinMoneyMagnitude() {
        return MinMoneyMagnitude;
    }
    public void setMinMoneyMagnitude(float minMoneyMagnitude) {
        this.MinMoneyMagnitude = minMoneyMagnitude;
    }
    
    public float getMaxMoneyMagnitude() {
        return MaxMoneyMagnitude;
    }
    public void setMaxMoneyMagnitude(float maxMoneyMagnitude) {
        this.MaxMoneyMagnitude = maxMoneyMagnitude;
    }

    public int getMinTimeMagnitude() {
        return MinTimeMagnitude;
    }
    public void setMinTimeMagnitude(int minTimeMagnitude) {
        this.MinTimeMagnitude = minTimeMagnitude;
    }
    
    public int getMaxTimeMagnitude() {
        return MaxTimeMagnitude;
    }
    public void setMaxTimeMagnitude(int maxTimeMagnitude) {
        this.MaxTimeMagnitude = maxTimeMagnitude;
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

    public int getBaseTimeMagnitude() {
        return BaseTimeMagnitude;
    }
    public void setBaseTimeMagnitude(int time) {
        this.BaseTimeMagnitude = time;
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
    
    public XMLConfigFile(String f) {
        
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
            
            String lExperimentNumber = doc.getDocumentElement().getElementsByTagName(EXPERIMENT_NUMBER).item(0).getTextContent();
            int lExperimentNumberInt = Integer.parseInt(lExperimentNumber);
            this.setExperimentNumberFromInt(lExperimentNumberInt);
            if (this.getExperimentNumber() == ExperimentNumber.UNKNOWN)  {
                throw new RuntimeException("Unknown experiment number in configuration file");
            }
            System.out.println("ExperimentNumber: " + lExperimentNumber);
            
            String lBaseMoneyMagnitude = doc.getDocumentElement().getElementsByTagName(BASE_MONEY_MAGNITUDE).item(0).getTextContent();
            this.BaseMoneyMagnitude = Float.parseFloat(lBaseMoneyMagnitude);
            System.out.println("BaseMoneyMagnitude: " + this.BaseMoneyMagnitude);

            String lMinMoneyMagnitude = doc.getDocumentElement().getElementsByTagName(MIN_MONEY_MAGNITUDE).item(0).getTextContent();
            this.MinMoneyMagnitude = Float.parseFloat(lMinMoneyMagnitude);
            System.out.println("MinMoneyMagnitude: " + this.MinMoneyMagnitude);
            
            String lMaxMoneyMagnitude = doc.getDocumentElement().getElementsByTagName(MAX_MONEY_MAGNITUDE).item(0).getTextContent();
            this.MaxMoneyMagnitude = Float.parseFloat(lMaxMoneyMagnitude);
            System.out.println("MaxMoneyMagnitude: " + this.MaxMoneyMagnitude);

            String lMaxStops = doc.getDocumentElement().getElementsByTagName(MAX_STOPS).item(0).getTextContent();
            this.MaxStops = Integer.parseInt(lMaxStops);
            System.out.println("MaxStops: " + this.MaxStops);

            String lMaxRounds = doc.getDocumentElement().getElementsByTagName(MAX_ROUNDS).item(0).getTextContent();
            this.MaxRounds = Integer.parseInt(lMaxRounds);
            System.out.println("MaxRounds: " + this.MaxRounds);
            
            String lBaseTime = doc.getDocumentElement().getElementsByTagName(BASE_TIME_MAGNITUDE).item(0).getTextContent();
            this.BaseTimeMagnitude = Integer.parseInt(lBaseTime);
            System.out.println("BaseTimeMagnitude: " + this.BaseTimeMagnitude);            
            System.out.println();
            
            String lMinTimeMagnitude = doc.getDocumentElement().getElementsByTagName(MIN_TIME_MAGNITUDE).item(0).getTextContent();
            this.MinTimeMagnitude = Integer.parseInt(lMinTimeMagnitude);
            System.out.println("MinTimeMagnitude: " + this.MinTimeMagnitude);
            
            String lMaxTimeMagnitude = doc.getDocumentElement().getElementsByTagName(MAX_TIME_MAGNITUDE).item(0).getTextContent();
            this.MaxTimeMagnitude = Integer.parseInt(lMaxTimeMagnitude);
            System.out.println("MaxTimeMagnitude: " + this.MaxTimeMagnitude);

            
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

            String lShowMoneyValues = doc.getDocumentElement().getElementsByTagName(SHOW_MONEY_VALUES).item(0).getTextContent();
            this.ShowMoneyValues = Boolean.parseBoolean(lShowMoneyValues);
            System.out.println("ShowMoneyValues: " + this.ShowMoneyValues);

            String lShowTimeLengths = doc.getDocumentElement().getElementsByTagName(SHOW_TIME_LENGTHS).item(0).getTextContent();
            this.ShowTimeLengths = Boolean.parseBoolean(lShowTimeLengths);
            System.out.println("ShowTimeLengths: " + this.ShowTimeLengths);
            
            this.NoiseValueLabel = doc.getDocumentElement().getElementsByTagName(NOISE_VALUE_LABEL_TEXT).item(0).getTextContent();
            System.out.println("NoiseValueLabel: \"" + this.NoiseValueLabel + "\"");
    
            this.NoiseLengthLabel = doc.getDocumentElement().getElementsByTagName(NOISE_LENGTH_LABEL_TEXT).item(0).getTextContent();
            System.out.println("NoiseValueLabel: \"" + this.NoiseLengthLabel + "\"");

            this.NoiseButtonLabel = doc.getDocumentElement().getElementsByTagName(NOISE_BUTTON_TEXT).item(0).getTextContent();
            System.out.println("NoiseButtonLabel: \"" + this.NoiseButtonLabel + "\"");
    
            this.SilenceValueLabel = doc.getDocumentElement().getElementsByTagName(SILENCE_VALUE_LABEL_TEXT).item(0).getTextContent();
            System.out.println("SilenceValueLabel: \"" + this.SilenceValueLabel + "\"");

            this.SilenceLengthLabel = doc.getDocumentElement().getElementsByTagName(SILENCE_LENGTH_LABEL_TEXT).item(0).getTextContent();
            System.out.println("SilenceValueLabel: \"" + this.SilenceLengthLabel + "\"");             
            
            this.SilenceButtonLabel = doc.getDocumentElement().getElementsByTagName(SILENCE_BUTTON_TEXT).item(0).getTextContent();
            System.out.println("SilenceButtonLabel: \"" + this.SilenceButtonLabel + "\"");
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
