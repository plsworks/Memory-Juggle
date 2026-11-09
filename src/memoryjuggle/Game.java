
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package memoryjuggle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(Game.class.getName());

    // CARD CLASS
    class Card {
        private String cardText;
        private int pairID;
        private JButton button;

        Card(String cardText, int pairID) {
            this.cardText = cardText;
            this.pairID = pairID;

            button = new JButton(cardText, cardFrontImageIcon);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setFocusable(false);
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setVerticalTextPosition(JButton.CENTER);
            button.setForeground(Color.BLACK);
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setPreferredSize(new Dimension(90, 128));
            button.setFocusable(false);
        }

        public String getCardText() {
            return cardText;
        }

        public int getPairID() {
            return pairID;
        }

        public JButton getButton() {
            return button;
        }
    }

    // GAME VARIABLES
    private int rows;
    private int columns;

    private final int cardWidth = 90;
    private final int cardHeight = 128;

    private ArrayList<CardPair> cards;
    private ArrayList<Card> cardSet;
    private ArrayList<JButton> board;

    private ImageIcon cardBackImageIcon;
    private ImageIcon cardFrontImageIcon;
    

    private JPanel boardPanel;

    private int errorCount = 0;

    private boolean gameReady = false;

    private Card card1Selected;
    private Card card2Selected;

    private Timer hideCardTimer;
    
    private JLabel errorLabel;
    
    private Difficulty diff;

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g); 
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        super.paint(g);
        
    }
    private void displayCardPairs() {
    DefaultListModel<String> model = new DefaultListModel<>();

    for (CardPair pair : cards) {
            model.addElement(pair.getCardA() + " - " + pair.getCardB());
        }

        jList1.setModel(model);
    }
    private void populateBoard() {

    boardPanel.removeAll();
    board.clear();

        for (int i = 0; i < cardSet.size(); i++) {

            Card card = cardSet.get(i);
            JButton button = card.getButton();

            button.setPreferredSize(new Dimension(cardWidth, cardHeight));

            button.addActionListener(e -> selectCard(card));

            board.add(button);

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = i % columns;
            gbc.gridy = i / columns;
            gbc.insets = new Insets(5, 5, 5, 5);

            boardPanel.add(button, gbc);
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    
    
    // CONSTRUCTOR
    public Game(ArrayList<CardPair> cards, Difficulty diff) {
        initComponents();

        this.cards = cards;
        this.diff = diff;

        rows = diff.getRows();
        columns = diff.getColumns();
        jList1.setBorder(null);
        jScrollPane1.setBorder(null);
        jList1.setOpaque(false);
        jList1.setBackground(new Color(255, 255, 255, 128));
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);

        

        // SETUP GAME PANEL
        jPanel4.setLayout(new BorderLayout());

        // SCORE PANEL
        JPanel scorePanel = new JPanel(new BorderLayout());
        scorePanel.setOpaque(false);

        errorLabel = new JLabel("Errors: 0", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 20));
        errorLabel.setForeground(Color.WHITE);

        scorePanel.add(errorLabel, BorderLayout.CENTER);

        jPanel4.add(scorePanel, BorderLayout.NORTH);

        setupCards();
        shuffleCards();

        board = new ArrayList<>();

        boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setBackground(Color.decode("#605063"));
        boardPanel.setBackground(Color.decode("#3B323D"));
        boardPanel.setOpaque(true);

        jPanel4.setBackground(Color.decode("#3B323D"));
        jPanel4.setOpaque(true);

        // ADD CARDS TO BOARD
        board = new ArrayList<>();

        boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setBackground(Color.decode("#605063"));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        centerPanel.add(boardPanel);
        jPanel4.add(centerPanel, BorderLayout.CENTER);

        populateBoard();

        // SCORE
        errorLabel.setText("Errors: 0");

        // RESTART BUTTON
        jButton2.addActionListener(e -> restartGame());

        // TIMER
        hideCardTimer = new Timer(diff.getPreviewMs(), e -> hideCards());
        hideCardTimer.setRepeats(false);

        // START INITIAL PREVIEW
        hideCardTimer.start();
    }

    // LOAD IMAGES AND CREATE CARDS
    private void setupCards() {

        cardSet = new ArrayList<>();

        java.net.URL backURL =
                getClass().getResource("Clover_Card_Back.png");

        java.net.URL frontURL =
                getClass().getResource("Clover_Card_Front.png");

        if (backURL == null || frontURL == null) {
            throw new IllegalStateException("Card images not found!");
        }

        Image backImage = new ImageIcon(backURL).getImage();
        Image frontImage = new ImageIcon(frontURL).getImage();

        cardBackImageIcon = new ImageIcon(
                backImage.getScaledInstance(
                        cardWidth, cardHeight, Image.SCALE_SMOOTH
                )
        );

        cardFrontImageIcon = new ImageIcon(
                frontImage.getScaledInstance(
                        cardWidth, cardHeight, Image.SCALE_SMOOTH
                )
        );

        int id = 0;

        for (CardPair pair : cards) {

            Card cardA = new Card(pair.getCardA(), id);
            Card cardB = new Card(pair.getCardB(), id);

            cardSet.add(cardA);
            cardSet.add(cardB);

            id++;
        }
    }

    // SHUFFLE CARDS
    private void shuffleCards() {
        Collections.shuffle(cardSet);
    }

    // SELECT CARD
    private void selectCard(Card card) {

        if (!gameReady) {
            return;
        }

        if (!card.getButton().isEnabled()) {
            return;
        }

        if (card == card1Selected) {
            return;
        }

        if (card.getButton().getIcon() != cardBackImageIcon) {
            return;
        }

        // REVEAL SELECTED CARD
        card.getButton().setIcon(cardFrontImageIcon);
        card.getButton().setText(card.getCardText());

        if (card1Selected == null) {

            card1Selected = card;

        } else {

            card2Selected = card;

            // CHECK IF CARDS MATCH
            if (isMatching(card1Selected, card2Selected)) {

                card1Selected.getButton().setEnabled(false);
                card2Selected.getButton().setEnabled(false);

                card1Selected = null;
                card2Selected = null;

                // CHECK IF GAME IS COMPLETED
                if (allCardsMatched()) {
                    gameReady = false;
                    errorLabel.setText("Completed! Errors: " + errorCount);
                }

            } else {

                errorCount++;

                errorLabel.setText("Errors: " + errorCount);

                // PREVENT MORE CLICKS UNTIL CARDS ARE HIDDEN
                gameReady = false;

                hideCardTimer.restart();
            }
        }
    }

    // CHECK MATCH
    private boolean isMatching(Card card1, Card card2) {
        return card1.getPairID() == card2.getPairID();
    }

    // CHECK IF ALL CARDS ARE MATCHED
    private boolean allCardsMatched() {

        for (Card card : cardSet) {

            if (card.getButton().isEnabled()) {
                return false;
            }
        }

        return true;
    }

    // HIDE CARDS
    private void hideCards() {

        // HIDE INCORRECTLY MATCHED CARDS
        if (card1Selected != null && card2Selected != null) {

            card1Selected.getButton().setText("");
            card1Selected.getButton().setIcon(cardBackImageIcon);

            card2Selected.getButton().setText("");
            card2Selected.getButton().setIcon(cardBackImageIcon);

            card1Selected = null;
            card2Selected = null;

        } else {

            // HIDE ALL UNMATCHED CARDS AFTER INITIAL PREVIEW
            for (Card card : cardSet) {

                if (card.getButton().isEnabled()) {
                    card.getButton().setText("");
                    card.getButton().setIcon(cardBackImageIcon);
                }
            }
        }

        gameReady = true;
    }

    // RESTART GAME
    private void restartGame() {

        hideCardTimer.stop();

        card1Selected = null;
        card2Selected = null;

        errorCount = 0;
        gameReady = false;

        errorLabel.setText("Errors: 0");

        // RESET ALL CARDS
        for (Card card : cardSet) {

            JButton button = card.getButton();

            button.setEnabled(true);
            button.setVisible(true);
            button.setText(card.getCardText());
            button.setIcon(cardFrontImageIcon);
        }

        // SHUFFLE AGAIN
        shuffleCards();

        // REBUILD BOARD
        populateBoard();

        // SHOW CARDS AGAIN BEFORE HIDING
        hideCardTimer.restart();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new GradientPanel();
        jPanel2 = new GradientPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        jList1.setBorder(null);
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.setOpaque(false);
        jScrollPane1.setViewportView(jList1);

        jButton2.setText("RESTART");

        jButton1.setText("BACK");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel4.setForeground(new java.awt.Color(51, 255, 51));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 222, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        hideCardTimer.stop();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

    ArrayList<CardPair> cards = new ArrayList<>();

    cards.add(new CardPair("Apple", "Mansanas"));
    cards.add(new CardPair("Dog", "Aso"));
    cards.add(new CardPair("Cat", "Pusa"));
    cards.add(new CardPair("Book", "Libro"));
    cards.add(new CardPair("Water", "Tubig"));
    cards.add(new CardPair("House", "Bahay"));
    cards.add(new CardPair("Sun", "Araw"));
    cards.add(new CardPair("Moon", "Buwan"));

    Difficulty diff = new Difficulty();
    diff.easy();

    java.awt.EventQueue.invokeLater(() ->
        new Game(cards, diff).setVisible(true)
    );
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
