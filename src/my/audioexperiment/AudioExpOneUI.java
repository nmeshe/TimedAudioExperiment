package my.audioexperiment;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Scanner;
import javax.swing.border.TitledBorder;

/**
 *
 * @author nick
 */
public class AudioExpOneUI extends javax.swing.JFrame {

    /**
     * Creates new form ContactEditorUI
     */
    public AudioExpOneUI() {
        initComponents();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        this.configFile = new XMLConfigFileOne("resources/TimedAudioExperimentOne_Config.xml");
        
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
        
        if (configFile.showRoundNumber() == false) {
            this.RoundLabel.setVisible(false);
            this.RoundTextField.setVisible(false);
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
            this.LeftValTextField.setText(printAsMoney(noiseAmount)); 
            this.LeftStartButton.setText(this.configFile.getNoiseButtonLabel());

            this.RightValLabel.setText(this.configFile.getSilenceValueLabel());
            this.RightValTextField.setText(printAsMoney(silenceAmount));             
            this.RightStartButton.setText(this.configFile.getSilenceButtonLabel());

        } else {

            this.LeftValLabel.setText(this.configFile.getSilenceValueLabel());
            this.LeftValTextField.setText(printAsMoney(silenceAmount));
            this.LeftStartButton.setText(this.configFile.getSilenceButtonLabel());
            
            this.RightValLabel.setText(this.configFile.getNoiseValueLabel());
            this.RightValTextField.setText(printAsMoney(noiseAmount));   
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
            currentBank += currentTrialBlock.noiseAmt;
        } else {
            currentBank += currentTrialBlock.silenceAmt;
        }
        this.currentTrialBlock.bankAmount = currentBank;

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
    
    public void updateStats(boolean initial) {
        
        if (initial) {
            bankAmount = 0;
            roundNumber = 0;
            amtDiffFromPrevRound = configFile.getBaseMoneyMagnitude();
            noiseAmount = configFile.getBaseMoneyMagnitude();
            silenceAmount = configFile.getBaseMoneyMagnitude();
        } else {
            bankAmount = currentTrialBlock.bankAmount;
            
            roundNumber++;
            
            if (roundNumber > 1) {
            
                amtDiffFromPrevRound = roundAsMoney(amtDiffFromPrevRound/2);

                String selectedOption = currentTrialBlock.selectedOption;
                if (selectedOption == TrialBlock.SELECTED_OPTION_NOISE ) {
                    noiseAmount = roundAsMoney(currentTrialBlock.noiseAmt - amtDiffFromPrevRound);
                    if (noiseAmount < configFile.getMinMoneyMagnitude()) {
                        noiseAmount = configFile.getMinMoneyMagnitude();
                    }
                } else {
                    noiseAmount = roundAsMoney(currentTrialBlock.noiseAmt + amtDiffFromPrevRound);  
                    if (noiseAmount > configFile.getMaxMoneyMagnitude()) {
                        noiseAmount = configFile.getMaxMoneyMagnitude();
                    }
                }
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

        TopInfoPanel = new javax.swing.JPanel();
        CountdownLabel = new javax.swing.JLabel();
        CountdownTextField = new javax.swing.JTextField();
        BankLabel = new javax.swing.JLabel();
        BankTextField = new javax.swing.JTextField();
        RoundLabel = new javax.swing.JLabel();
        RoundTextField = new javax.swing.JTextField();
        MessagePanel = new javax.swing.JPanel();
        MessagesLabel = new javax.swing.JLabel();
        StopNoiseButton = new javax.swing.JButton();
        PlayNoiseSilencePanel = new javax.swing.JPanel();
        RightPlayPanel = new javax.swing.JPanel();
        RightValLabel = new javax.swing.JLabel();
        RightValTextField = new javax.swing.JTextField();
        RightStartButton = new javax.swing.JButton();
        LeftPlayPanel = new javax.swing.JPanel();
        LeftValLabel = new javax.swing.JLabel();
        LeftValTextField = new javax.swing.JTextField();
        LeftStartButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TopInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Name", 0, 0, new java.awt.Font("Lucida Grande", 1, 12))); // NOI18N

        CountdownLabel.setText("Countdown:");

        CountdownTextField.setEditable(false);
        CountdownTextField.setEnabled(false);

        BankLabel.setText("Bank Amout:");

        BankTextField.setEditable(false);
        BankTextField.setEnabled(false);

        RoundLabel.setText("Round #");

        RoundTextField.setEditable(false);
        RoundTextField.setEnabled(false);

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
                        .addComponent(RoundTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 321, Short.MAX_VALUE)
                        .addComponent(BankLabel))
                    .addGroup(TopInfoPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(CountdownLabel)))
                .addGap(0, 12, Short.MAX_VALUE)
                .addGroup(TopInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BankTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                    .addComponent(CountdownTextField))
                .addContainerGap())
        );
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

        MessagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Messages"));

        MessagesLabel.setForeground(new java.awt.Color(255, 0, 51));

        StopNoiseButton.setText("Stop Noise");
        StopNoiseButton.setEnabled(false);
        StopNoiseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StopNoiseButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout MessagePanelLayout = new javax.swing.GroupLayout(MessagePanel);
        MessagePanel.setLayout(MessagePanelLayout);
        MessagePanelLayout.setHorizontalGroup(
            MessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MessagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MessagesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StopNoiseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        MessagePanelLayout.setVerticalGroup(
            MessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MessagePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(MessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MessagesLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MessagePanelLayout.createSequentialGroup()
                        .addComponent(StopNoiseButton)
                        .addContainerGap())))
        );

        RightValLabel.setText("Noise Value:");

        RightValTextField.setEditable(false);
        RightValTextField.setEnabled(false);
        RightValTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RightValTextFieldActionPerformed(evt);
            }
        });

        RightStartButton.setText("Play Noise");
        RightStartButton.setEnabled(false);
        RightStartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RightStartButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout RightPlayPanelLayout = new javax.swing.GroupLayout(RightPlayPanel);
        RightPlayPanel.setLayout(RightPlayPanelLayout);
        RightPlayPanelLayout.setHorizontalGroup(
            RightPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightPlayPanelLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(RightPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(RightValLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RightStartButton, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                    .addComponent(RightValTextField))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        RightPlayPanelLayout.setVerticalGroup(
            RightPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightPlayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RightValLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RightValTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RightStartButton)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        LeftValLabel.setText("Silence Value:");

        LeftValTextField.setEditable(false);
        LeftValTextField.setEnabled(false);

        LeftStartButton.setText("Play Silence");
        LeftStartButton.setEnabled(false);
        LeftStartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LeftStartButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 55, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout LeftPlayPanelLayout = new javax.swing.GroupLayout(LeftPlayPanel);
        LeftPlayPanel.setLayout(LeftPlayPanelLayout);
        LeftPlayPanelLayout.setHorizontalGroup(
            LeftPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPlayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LeftPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LeftPlayPanelLayout.createSequentialGroup()
                        .addComponent(LeftValLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(LeftStartButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LeftValTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                .addGap(86, 86, 86))
        );
        LeftPlayPanelLayout.setVerticalGroup(
            LeftPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPlayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LeftPlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(LeftPlayPanelLayout.createSequentialGroup()
                        .addComponent(LeftValLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LeftValTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LeftStartButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(PlayNoiseSilencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RightPlayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LeftPlayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PlayNoiseSilencePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MessagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TopInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {MessagePanel, TopInfoPanel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TopInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PlayNoiseSilencePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(MessagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RightStartButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RightStartButtonMouseClicked
        
        this.disableButtonsForPlaying();
        
        String lSelectedOption = (this.NoiseOnLeft) 
                ? TrialBlock.SELECTED_OPTION_SILENCE
                : TrialBlock.SELECTED_OPTION_NOISE;
        
        currentTrialBlock = new TrialBlock(
                identifierValue, 
                new Integer(roundNumber).toString(), 
                configFile.getDurationDelay(),
                noiseAmount,
                silenceAmount,
                lSelectedOption,
                bankAmount ); 
        
        AePlayTimer timer = new AePlayTimer(
                configFile.getDurationDelay(), 
                MyWavPlayer, 
                this, 
                ! this.NoiseOnLeft );
        
        timer.start();
    }//GEN-LAST:event_RightStartButtonMouseClicked

    private void StopNoiseButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StopNoiseButtonMouseClicked
        this.displayInfoMessage("You chose to stop the noise.");
        MyWavPlayer.stopPlaying();
    }//GEN-LAST:event_StopNoiseButtonMouseClicked

    private void RightValTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RightValTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RightValTextFieldActionPerformed

    private void LeftStartButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LeftStartButtonMouseClicked
  
        this.disableButtonsForPlaying();
        
        String lSelectedOption = (this.NoiseOnLeft) 
                ? TrialBlock.SELECTED_OPTION_NOISE
                : TrialBlock.SELECTED_OPTION_SILENCE;
        
        currentTrialBlock = new TrialBlock(
            identifierValue, 
            new Integer(roundNumber).toString(), 
            configFile.getDurationDelay(),
            noiseAmount,
            silenceAmount,
            lSelectedOption,
            bankAmount );
        
        AePlayTimer timer = new AePlayTimer(
                configFile.getDurationDelay(), 
                MyWavPlayer, 
                this, 
                this.NoiseOnLeft );
        
        timer.start();                   
    }//GEN-LAST:event_LeftStartButtonMouseClicked

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
            java.util.logging.Logger.getLogger(AudioExpOneUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AudioExpOneUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AudioExpOneUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AudioExpOneUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AudioExpOneUI().setVisible(true);
            }
        });
    }

    InputReaderThread stopReaderThread;
    AePlayWave MyWavPlayer;
    XMLConfigFileOne configFile;
    TrialData myTrialData;
    TrialBlock currentTrialBlock;
    int roundNumber = 0;
    int countDownClock = 0;
    String identifierValue = "";
    float noiseAmount = 0;
    float silenceAmount = 0;
    float bankAmount = 0;
    float amtDiffFromPrevRound=0;
    int stopCount = 0;
    boolean playingSecondOptionInRoundZero = false;
    boolean NoiseOnLeft = true; 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BankLabel;
    private javax.swing.JTextField BankTextField;
    private javax.swing.JLabel CountdownLabel;
    private javax.swing.JTextField CountdownTextField;
    private javax.swing.JPanel LeftPlayPanel;
    private javax.swing.JButton LeftStartButton;
    private javax.swing.JLabel LeftValLabel;
    private javax.swing.JTextField LeftValTextField;
    private javax.swing.JPanel MessagePanel;
    private javax.swing.JLabel MessagesLabel;
    private javax.swing.JPanel PlayNoiseSilencePanel;
    private javax.swing.JPanel RightPlayPanel;
    private javax.swing.JButton RightStartButton;
    private javax.swing.JLabel RightValLabel;
    private javax.swing.JTextField RightValTextField;
    private javax.swing.JLabel RoundLabel;
    private javax.swing.JTextField RoundTextField;
    private javax.swing.JButton StopNoiseButton;
    private javax.swing.JPanel TopInfoPanel;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables
}
