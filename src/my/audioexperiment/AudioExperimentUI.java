package my.audioexperiment;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Scanner;
import javax.swing.border.TitledBorder;

/**
 *
 * @author nick
 */

enum ExperimentNumber {
    ONE,    //adjust the amount of money for listening to noise
    TWO,    //adjust the amount of time to listen to noise
    THREE,  //adjust the amount of time to listen to silence
    UNKNOWN //do not know what this is
}

public class AudioExperimentUI extends javax.swing.JFrame {

    public AudioExperimentUI() {
        initComponents();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        this.configFile = new XMLConfigFile("resources/TimedAudioExperiment_Config.xml");
        
        if (configFile.showCountdownClock() == false) {
            this.CountdownLabel.setVisible(false);
            this.CountdownTextField.setVisible(false);
        }
        
        if (configFile.showMessages() == false) {
            this.MessagesLabel.setVisible(false);
            ((TitledBorder)this.MessagePanel.getBorder()).setTitle("");
        }
        
        if (configFile.showStopButton()== false) {
            this.StopNoiseButton.setVisible(false);
            this.StopNoiseButton.setEnabled(false);
        }
        
        if ((configFile.showMessages() == false) && (configFile.showStopButton()== false)) {
            this.MessagePanel.setVisible(false);
        }
        
        if (configFile.showRoundNumber() == false) {
            this.RoundLabel.setVisible(false);
            this.RoundTextField.setVisible(false);
        }
        
        if (configFile.showTimeLengths()== false) {
            this.LeftLenLabel.setVisible(false);
            this.LeftLenTextField.setVisible(false);
            this.RightLenLabel.setVisible(false);
            this.RightLenTextField.setVisible(false);
        }

        if (configFile.showMoneyValues()== false) {
            this.LeftValLabel.setVisible(false);
            this.LeftValTextField.setVisible(false);
            this.RightValLabel.setVisible(false);
            this.RightValTextField.setVisible(false);
        }
        
        this.readIdentifierFromCommandLine();
                  
        myTrialData = new TrialData(identifierValue, this.configFile.getResultsFolder());
        this.enableButtonsForPlaying();
        this.updateStats(true);
                
        this.MyWavPlayer = new AePlayWave(this.configFile.getWavFile());
        
        stopReaderThread = new InputReaderThread(this, this.MyWavPlayer);
        stopReaderThread.start();
    }
    
    private void readIdentifierFromCommandLine() {

        Scanner lScanner = new Scanner(System.in);
        
        do {
            System.out.print("\n" + "Type Identifier (and hit ENTER): ");
               
            this.identifierValue = lScanner.nextLine().trim();

            if (identifierValue == null || identifierValue.length() == 0) {
                this.displayErrorMessage("<html>Identifier Required!</html>");
            } else {
                this.displayInfoMessage("<html>Identifier '" + identifierValue + "' - Ready for trial</html>");

            } 
        } while (identifierValue == null || identifierValue.length() == 0);
        
    }
    
    private void enableButtonsForPlaying() {
        
        this.NoiseOnLeft = (Math.random() >= .5);
        
        if (this.NoiseOnLeft) {
            this.LeftValLabel.setText(this.configFile.getNoiseValueLabel());
            this.LeftValTextField.setText(printAsMoney(noiseDollarAmount)); 
            this.LeftLenLabel.setText(this.configFile.getNoiseLengthLabel());
            this.LeftLenTextField.setText(noiseTimeAmount + " seconds"); 
            this.LeftStartButton.setText(this.configFile.getNoiseButtonLabel());

            this.RightValLabel.setText(this.configFile.getSilenceValueLabel());
            this.RightValTextField.setText(printAsMoney(silenceDollarAmount)); 
            this.RightLenLabel.setText(this.configFile.getSilenceLengthLabel());
            this.RightLenTextField.setText(silenceTimeAmount + " seconds");
            this.RightStartButton.setText(this.configFile.getSilenceButtonLabel());

        } else {

            this.LeftValLabel.setText(this.configFile.getSilenceValueLabel());
            this.LeftValTextField.setText(printAsMoney(silenceDollarAmount));
            this.LeftLenLabel.setText(this.configFile.getSilenceLengthLabel());
            this.LeftLenTextField.setText(silenceTimeAmount + " seconds"); 
            this.LeftStartButton.setText(this.configFile.getSilenceButtonLabel());
            
            this.RightValLabel.setText(this.configFile.getNoiseValueLabel());
            this.RightValTextField.setText(printAsMoney(noiseDollarAmount));  
            this.RightLenLabel.setText(this.configFile.getNoiseLengthLabel());
            this.RightLenTextField.setText(noiseTimeAmount + " seconds");            
            this.RightStartButton.setText(this.configFile.getNoiseButtonLabel());   
        }

        this.assignRoundNumber(roundNumber);
        this.BankTextField.setText(printAsMoney(bankAmount));
        
        if (roundNumber == 0 && playingSecondOptionInRoundZero) {

            /*
                this could be done in two lines of convoluted code like 
                this.LeftStartButton.setEnabled((selectedOption == TrialBlock.SELECTED_OPTION_NOISE) != this.NoiseOnLeft)
                but good luck reading that in the future
            */
            if (currentTrialBlock.selectedOption == TrialBlock.SELECTED_OPTION_NOISE) {
                if (this.NoiseOnLeft) {
                    this.LeftStartButton.setEnabled(true);                     
                    this.RightStartButton.setEnabled(false);
                } else {
                    this.LeftStartButton.setEnabled(false); 
                    this.RightStartButton.setEnabled(true);
                }
            } else {
                if (this.NoiseOnLeft) {
                    this.LeftStartButton.setEnabled(false);                     
                    this.RightStartButton.setEnabled(true);
                } else {
                    this.LeftStartButton.setEnabled(true);  
                    this.RightStartButton.setEnabled(false);
                }
            }
            
        } else {

            this.RightStartButton.setEnabled(true);
            this.LeftStartButton.setEnabled(true);            
        }
        
        if (configFile.showStopButton()) {
            this.StopNoiseButton.setEnabled(true);
        }
        
    }
    
    private void disableButtonsForPlaying() {
        this.LeftStartButton.setEnabled(false);        
        this.RightStartButton.setEnabled(false);
    }
    
    public void assignRoundNumber(int round) {
        this.roundNumber = round;
        System.out.println("round# " + round);
        if (configFile.showRoundNumber()) {
            this.RoundTextField.setText("" + round);
        }    
    }
    
    public void assignCountdownClock(int count) {
        countDownClock = count;
        if (configFile.showCountdownClock()) {
            this.CountdownTextField.setText("" + count);
        }
    }

    public void displayInfoMessage(String message) {
        System.out.println("INFO: " + message);
        if (configFile.showMessages()) {
            this.MessagesLabel.setForeground(Color.BLACK);
            this.MessagesLabel.setText(message);   
        }
    }
    
    public void displayErrorMessage(String message) {
        System.err.println("ERROR: " + message);
        if (configFile.showMessages()) {
            this.MessagesLabel.setForeground(Color.RED);
            this.MessagesLabel.setText(message);
        }
    }    
    
    public void trialRoundSucceded() {
        
        String selectedOption = currentTrialBlock.selectedOption;
        
        this.assignCountdownClock(0);
                         
        float currentBank = currentTrialBlock.bankAmount;

        if (selectedOption == TrialBlock.SELECTED_OPTION_NOISE ) {
            currentBank += currentTrialBlock.noiseDollarAmt;
        } else {
            currentBank += currentTrialBlock.silenceDollarAmt;
        }
        this.currentTrialBlock.bankAmount = roundAsMoney(currentBank);

        this.myTrialData.addTrialBlock(this.currentTrialBlock);

        this.displayInfoMessage("<html>SUCCESS<br/>You get money</html>");
        
        if (roundNumber != 0 || playingSecondOptionInRoundZero) {

            updateStats(false);
                      
        } else {

            this.displayInfoMessage("<html>Select the other option</html>");
            
            playingSecondOptionInRoundZero = true;
            currentTrialBlock.selectedOption = 
                (currentTrialBlock.selectedOption == TrialBlock.SELECTED_OPTION_NOISE) ?
                TrialBlock.SELECTED_OPTION_SILENCE :
                TrialBlock.SELECTED_OPTION_NOISE ;

            updateStats(true);
            
            this.bankAmount = this.currentTrialBlock.bankAmount;
            this.BankTextField.setText(printAsMoney(bankAmount));
            
        }
    }
    
    public static float roundAsMoney(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }    
    
    public static String printAsMoney(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);       
        return "$"+ bd;
    }
    
    private void adjustNoiseDollarValue() {
        
        if (this.configFile.getExperimentNumber() == ExperimentNumber.ONE) {
            amtDiffFromPrevRound = roundAsMoney(amtDiffFromPrevRound/2);

            String selectedOption = currentTrialBlock.selectedOption;
            if (selectedOption == TrialBlock.SELECTED_OPTION_NOISE ) {
                noiseDollarAmount = roundAsMoney(currentTrialBlock.noiseDollarAmt - amtDiffFromPrevRound);
                if (noiseDollarAmount < configFile.getMinMoneyMagnitude()) {
                    noiseDollarAmount = configFile.getMinMoneyMagnitude();
                }
            } else {
                noiseDollarAmount = roundAsMoney(currentTrialBlock.noiseDollarAmt + amtDiffFromPrevRound);  
                if (noiseDollarAmount > configFile.getMaxMoneyMagnitude()) {
                    noiseDollarAmount = configFile.getMaxMoneyMagnitude();
                }
            }   
        }
    }
    
    private void adjustNoiseDuration() {

        if (this.configFile.getExperimentNumber() == ExperimentNumber.TWO) {
            amtDiffFromPrevRound = Math.round(amtDiffFromPrevRound/2);

            String selectedOption = currentTrialBlock.selectedOption;
            if (selectedOption == TrialBlock.SELECTED_OPTION_NOISE ) {
                noiseTimeAmount = currentTrialBlock.noiseTimeAmt + Math.round(amtDiffFromPrevRound);
                if (noiseTimeAmount > configFile.getMaxTimeMagnitude()) {
                    noiseTimeAmount = configFile.getMaxTimeMagnitude();
                }
            } else {
                noiseTimeAmount = currentTrialBlock.noiseTimeAmt - Math.round(amtDiffFromPrevRound); 
                if (noiseTimeAmount < configFile.getMinTimeMagnitude()) {
                    noiseTimeAmount = configFile.getMinTimeMagnitude();
                }
            }   
        }        
    }
    
    private void adjustSilenceDuration() {
        if (this.configFile.getExperimentNumber() == ExperimentNumber.THREE) {
            amtDiffFromPrevRound = Math.round(amtDiffFromPrevRound/2);

            String selectedOption = currentTrialBlock.selectedOption;
            if (selectedOption == TrialBlock.SELECTED_OPTION_NOISE ) {
                silenceTimeAmount = currentTrialBlock.silenceTimeAmt - Math.round(amtDiffFromPrevRound); 
                if (silenceTimeAmount < configFile.getMinTimeMagnitude()) {
                    silenceTimeAmount = configFile.getMinTimeMagnitude();
                }
            } else {
                silenceTimeAmount = currentTrialBlock.silenceTimeAmt + Math.round(amtDiffFromPrevRound);
                if (silenceTimeAmount > configFile.getMaxTimeMagnitude()) {
                    silenceTimeAmount = configFile.getMaxTimeMagnitude();
                }
            }   
        }        
    }
    
    private void assignInitialDiffAmount() {
        if (this.configFile.getExperimentNumber() == ExperimentNumber.ONE) {
            amtDiffFromPrevRound = configFile.getBaseMoneyMagnitude();
        } else {
            amtDiffFromPrevRound = configFile.getBaseTimeMagnitude();
        }
    }

    private void assignInitialSilenceDollarAmount() {
        if (this.configFile.getExperimentNumber() == ExperimentNumber.ONE) {
           silenceDollarAmount = configFile.getBaseMoneyMagnitude();
        } else {
            silenceDollarAmount = 0;
        }
    }

    private void assignInitialNoiseDollarAmount() {
        noiseDollarAmount = configFile.getBaseMoneyMagnitude();
    }
    
    public void updateStats(boolean initial) {
        
        if (initial) {
            bankAmount = 0;
            roundNumber = 0;
            this.assignInitialDiffAmount();
            this.assignInitialNoiseDollarAmount();
            this.assignInitialSilenceDollarAmount();
            noiseTimeAmount = configFile.getBaseTimeMagnitude();
            silenceTimeAmount = configFile.getBaseTimeMagnitude();
            
        } else {
            bankAmount = currentTrialBlock.bankAmount;
            
            roundNumber++;
            
            if (roundNumber > 1) {
            
                this.adjustNoiseDollarValue();
                this.adjustNoiseDuration();
                this.adjustSilenceDuration();
            }    
        }
            
        this.enableButtonsForPlaying();
        
        if (roundNumber > configFile.getMaxRounds()) {
            this.assignRoundNumber(configFile.getMaxRounds());
            this.endSession();
        }
    }
    
    public void endSession() {
            this.disableButtonsForPlaying();
            this.LeftPlayPanel.removeAll();
            this.RightPlayPanel.removeAll();
            this.LeftPlayPanel.revalidate();
            this.RightPlayPanel.revalidate();
            this.StopNoiseButton.setEnabled(false);
            this.repaint();
            this.displayInfoMessage("YOU ARE DONE");        
    }

    public void trialRoundFailed() {
        stopCount++;
        
        int lMaxStops = configFile.getMaxStops();
        
        this.displayErrorMessage("<html>You chose to stop<br/>You can stop "+ (lMaxStops-stopCount) +" more times</html>");
        this.assignCountdownClock(0);
        
        TrialBlock lFailBlock = this.currentTrialBlock.copy();
        lFailBlock.selectedOption += " STOPPED";
        this.myTrialData.addTrialBlock(lFailBlock);
        
        if (stopCount < lMaxStops) {
            this.enableButtonsForPlaying();
        } else {
            this.endSession();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        TopInfoPanel = new javax.swing.JPanel();
        RoundLabel = new javax.swing.JLabel();
        RoundTextField = new javax.swing.JTextField();
        BankLabel = new javax.swing.JLabel();
        BankTextField = new javax.swing.JTextField();
        CountdownLabel = new javax.swing.JLabel();
        CountdownTextField = new javax.swing.JTextField();
        PlayNoiseSilencePanel = new javax.swing.JPanel();
        LeftPlayPanel = new javax.swing.JPanel();
        LeftValuePanel = new javax.swing.JPanel();
        LeftValLabel = new javax.swing.JLabel();
        LeftValTextField = new javax.swing.JTextField();
        LeftLengthPanel = new javax.swing.JPanel();
        LeftLenTextField = new javax.swing.JTextField();
        LeftLenLabel = new javax.swing.JLabel();
        LeftStartButton = new javax.swing.JButton();
        RightPlayPanel = new javax.swing.JPanel();
        RightValuePanel = new javax.swing.JPanel();
        RightValLabel = new javax.swing.JLabel();
        RightValTextField = new javax.swing.JTextField();
        RightLengthPanel = new javax.swing.JPanel();
        RightLenLabel = new javax.swing.JLabel();
        RightLenTextField = new javax.swing.JTextField();
        RightStartButton = new javax.swing.JButton();
        MessagePanel = new javax.swing.JPanel();
        MessagesLabel = new javax.swing.JLabel();
        StopNoiseButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TopInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        RoundLabel.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        RoundLabel.setText("Round #");

        RoundTextField.setEditable(false);
        RoundTextField.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        RoundTextField.setEnabled(false);

        BankLabel.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        BankLabel.setText("Bank Amount:");

        BankTextField.setEditable(false);
        BankTextField.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        BankTextField.setEnabled(false);

        CountdownLabel.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        CountdownLabel.setText("Countdown:");

        CountdownTextField.setEditable(false);
        CountdownTextField.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        CountdownTextField.setEnabled(false);

        javax.swing.GroupLayout TopInfoPanelLayout = new javax.swing.GroupLayout(TopInfoPanel);
        TopInfoPanel.setLayout(TopInfoPanelLayout);
        TopInfoPanelLayout.setHorizontalGroup(
            TopInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TopInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TopInfoPanelLayout.createSequentialGroup()
                        .addComponent(RoundLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RoundTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BankLabel))
                    .addGroup(TopInfoPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(CountdownLabel)))
                .addGap(67, 67, 67)
                .addGroup(TopInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CountdownTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BankTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        TopInfoPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BankTextField, CountdownTextField});

        TopInfoPanelLayout.setVerticalGroup(
            TopInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopInfoPanelLayout.createSequentialGroup()
                .addGroup(TopInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BankTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BankLabel)
                    .addComponent(RoundLabel)
                    .addComponent(RoundTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(TopInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CountdownLabel)
                    .addComponent(CountdownTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        LeftValLabel.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        LeftValLabel.setText("Silence Value:");

        LeftValTextField.setEditable(false);
        LeftValTextField.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        LeftValTextField.setEnabled(false);

        javax.swing.GroupLayout LeftValuePanelLayout = new javax.swing.GroupLayout(LeftValuePanel);
        LeftValuePanel.setLayout(LeftValuePanelLayout);
        LeftValuePanelLayout.setHorizontalGroup(
            LeftValuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftValuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LeftValuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, LeftValuePanelLayout.createSequentialGroup()
                        .addComponent(LeftValLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(LeftValTextField, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        LeftValuePanelLayout.setVerticalGroup(
            LeftValuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftValuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LeftValLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LeftValTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        LeftLenTextField.setEditable(false);
        LeftLenTextField.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        LeftLenTextField.setEnabled(false);

        LeftLenLabel.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        LeftLenLabel.setText("Silence Length:");

        javax.swing.GroupLayout LeftLengthPanelLayout = new javax.swing.GroupLayout(LeftLengthPanel);
        LeftLengthPanel.setLayout(LeftLengthPanelLayout);
        LeftLengthPanelLayout.setHorizontalGroup(
            LeftLengthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLengthPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LeftLengthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LeftLenTextField)
                    .addGroup(LeftLengthPanelLayout.createSequentialGroup()
                        .addComponent(LeftLenLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        LeftLengthPanelLayout.setVerticalGroup(
            LeftLengthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLengthPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LeftLenLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LeftLenTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        LeftStartButton.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        LeftStartButton.setText("Play Silence");
        LeftStartButton.setEnabled(false);
        LeftStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LeftStartButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LeftPlayPanelLayout = new javax.swing.GroupLayout(LeftPlayPanel);
        LeftPlayPanel.setLayout(LeftPlayPanelLayout);
        LeftPlayPanelLayout.setHorizontalGroup(
            LeftPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPlayPanelLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(LeftPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LeftStartButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LeftLengthPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LeftValuePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(86, 86, 86))
        );
        LeftPlayPanelLayout.setVerticalGroup(
            LeftPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPlayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LeftValuePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LeftLengthPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LeftStartButton))
        );

        RightValLabel.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        RightValLabel.setText("Noise Value:");

        RightValTextField.setEditable(false);
        RightValTextField.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        RightValTextField.setEnabled(false);

        javax.swing.GroupLayout RightValuePanelLayout = new javax.swing.GroupLayout(RightValuePanel);
        RightValuePanel.setLayout(RightValuePanelLayout);
        RightValuePanelLayout.setHorizontalGroup(
            RightValuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightValuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RightValuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RightValTextField)
                    .addComponent(RightValLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        RightValuePanelLayout.setVerticalGroup(
            RightValuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightValuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RightValLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RightValTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        RightLenLabel.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        RightLenLabel.setText("Noise Length:");

        RightLenTextField.setEditable(false);
        RightLenTextField.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        RightLenTextField.setEnabled(false);

        javax.swing.GroupLayout RightLengthPanelLayout = new javax.swing.GroupLayout(RightLengthPanel);
        RightLengthPanel.setLayout(RightLengthPanelLayout);
        RightLengthPanelLayout.setHorizontalGroup(
            RightLengthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLengthPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RightLengthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RightLenTextField)
                    .addComponent(RightLenLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        RightLengthPanelLayout.setVerticalGroup(
            RightLengthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLengthPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RightLenLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RightLenTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        RightStartButton.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        RightStartButton.setText("Play Noise");
        RightStartButton.setEnabled(false);
        RightStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RightStartButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RightPlayPanelLayout = new javax.swing.GroupLayout(RightPlayPanel);
        RightPlayPanel.setLayout(RightPlayPanelLayout);
        RightPlayPanelLayout.setHorizontalGroup(
            RightPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightPlayPanelLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(RightPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(RightStartButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RightValuePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RightLengthPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        RightPlayPanelLayout.setVerticalGroup(
            RightPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightPlayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RightValuePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RightLengthPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(46, 46, 46)
                .addComponent(RightStartButton))
        );

        javax.swing.GroupLayout PlayNoiseSilencePanelLayout = new javax.swing.GroupLayout(PlayNoiseSilencePanel);
        PlayNoiseSilencePanel.setLayout(PlayNoiseSilencePanelLayout);
        PlayNoiseSilencePanelLayout.setHorizontalGroup(
            PlayNoiseSilencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayNoiseSilencePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LeftPlayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(RightPlayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PlayNoiseSilencePanelLayout.setVerticalGroup(
            PlayNoiseSilencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayNoiseSilencePanelLayout.createSequentialGroup()
                .addGroup(PlayNoiseSilencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LeftPlayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RightPlayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(94, Short.MAX_VALUE))
        );

        MessagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Messages", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 36))); // NOI18N
        MessagePanel.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N

        MessagesLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        MessagesLabel.setForeground(new java.awt.Color(255, 0, 51));

        StopNoiseButton.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        StopNoiseButton.setText("Stop Noise");
        StopNoiseButton.setEnabled(false);
        StopNoiseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopNoiseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MessagePanelLayout = new javax.swing.GroupLayout(MessagePanel);
        MessagePanel.setLayout(MessagePanelLayout);
        MessagePanelLayout.setHorizontalGroup(
            MessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MessagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MessagesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 727, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(StopNoiseButton)
                .addContainerGap())
        );
        MessagePanelLayout.setVerticalGroup(
            MessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MessagePanelLayout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addComponent(StopNoiseButton)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MessagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MessagesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(MessagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TopInfoPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PlayNoiseSilencePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TopInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PlayNoiseSilencePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(MessagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LeftStartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LeftStartButtonActionPerformed
        this.disableButtonsForPlaying();
        
        String lSelectedOption = (this.NoiseOnLeft) 
                ? TrialBlock.SELECTED_OPTION_NOISE
                : TrialBlock.SELECTED_OPTION_SILENCE;
        
        currentTrialBlock = new TrialBlock(
            identifierValue, 
            this.configFile.getExperimentNumberAsInt(),
            new Integer(roundNumber).toString(), 
            noiseDollarAmount,
            silenceDollarAmount,
            noiseTimeAmount,
            silenceTimeAmount,
            lSelectedOption,
            bankAmount );
        
        AePlayTimer timer = new AePlayTimer(
                (this.NoiseOnLeft ? noiseTimeAmount : silenceTimeAmount), 
                MyWavPlayer, 
                this, 
                this.NoiseOnLeft );
        
        timer.start();  
    }//GEN-LAST:event_LeftStartButtonActionPerformed

    private void RightStartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RightStartButtonActionPerformed
        this.disableButtonsForPlaying();
        
        String lSelectedOption = (this.NoiseOnLeft) 
                ? TrialBlock.SELECTED_OPTION_SILENCE
                : TrialBlock.SELECTED_OPTION_NOISE;
        
        currentTrialBlock = new TrialBlock(
                identifierValue, 
                this.configFile.getExperimentNumberAsInt(),
                new Integer(roundNumber).toString(), 
                noiseDollarAmount,
                silenceDollarAmount,
                noiseTimeAmount,
                silenceTimeAmount,
                lSelectedOption,
                bankAmount ); 
        
        AePlayTimer timer = new AePlayTimer(
                (this.NoiseOnLeft ? silenceTimeAmount : noiseTimeAmount),
                MyWavPlayer, 
                this, 
                ! this.NoiseOnLeft );
        
        timer.start();
    }//GEN-LAST:event_RightStartButtonActionPerformed

    private void StopNoiseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopNoiseButtonActionPerformed
        this.displayInfoMessage("You chose to stop the noise.");
        MyWavPlayer.stopPlaying();
    }//GEN-LAST:event_StopNoiseButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AudioExperimentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AudioExperimentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AudioExperimentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AudioExperimentUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AudioExperimentUI().setVisible(true);
            }
        });
    }

    InputReaderThread stopReaderThread;
    AePlayWave MyWavPlayer;
    XMLConfigFile configFile;
    TrialData myTrialData;
    TrialBlock currentTrialBlock;
    int roundNumber = 0;
    int countDownClock = 0;
    String identifierValue = "";
    float noiseDollarAmount = 0;
    float silenceDollarAmount = 0;
    int noiseTimeAmount = 0;
    int silenceTimeAmount = 0;    
    float bankAmount = 0;
    float amtDiffFromPrevRound = 0;
    int stopCount = 0;
    boolean playingSecondOptionInRoundZero = false;
    boolean NoiseOnLeft = true; 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BankLabel;
    private javax.swing.JTextField BankTextField;
    private javax.swing.JLabel CountdownLabel;
    private javax.swing.JTextField CountdownTextField;
    private javax.swing.JLabel LeftLenLabel;
    private javax.swing.JTextField LeftLenTextField;
    private javax.swing.JPanel LeftLengthPanel;
    private javax.swing.JPanel LeftPlayPanel;
    private javax.swing.JButton LeftStartButton;
    private javax.swing.JLabel LeftValLabel;
    private javax.swing.JTextField LeftValTextField;
    private javax.swing.JPanel LeftValuePanel;
    private javax.swing.JPanel MessagePanel;
    private javax.swing.JLabel MessagesLabel;
    private javax.swing.JPanel PlayNoiseSilencePanel;
    private javax.swing.JLabel RightLenLabel;
    private javax.swing.JTextField RightLenTextField;
    private javax.swing.JPanel RightLengthPanel;
    private javax.swing.JPanel RightPlayPanel;
    private javax.swing.JButton RightStartButton;
    private javax.swing.JLabel RightValLabel;
    private javax.swing.JTextField RightValTextField;
    private javax.swing.JPanel RightValuePanel;
    private javax.swing.JLabel RoundLabel;
    private javax.swing.JTextField RoundTextField;
    private javax.swing.JButton StopNoiseButton;
    private javax.swing.JPanel TopInfoPanel;
    private javax.swing.JPanel jPanel6;
    // End of variables declaration//GEN-END:variables
}
