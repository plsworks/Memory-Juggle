
package memoryjuggle;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import javax.swing.border.Border;

public class MatchCards {

    class Card {
        private String cardText;
        private int pairID;
        private JButton button;

        Card(String cardText, int pairID) {
            this.cardText = cardText;
            this.pairID = pairID;

            this.button = new JButton(cardText, cardFrontImageIcon);
            this.button.setHorizontalTextPosition(JButton.CENTER);
            this.button.setVerticalTextPosition(JButton.CENTER);
            this.button.setForeground(Color.WHITE);
            this.button.setForeground(Color.BLACK);
            this.button.setBackground(Color.WHITE);
            this.button.setOpaque(true);
            this.button.setContentAreaFilled(true);
            this.button.setBorderPainted(true);

            this.button.setPreferredSize(
                new Dimension(cardWidth, cardHeight)
            );

            this.button.setFont(new Font("Arial", Font.BOLD, 16));
            this.button.setFocusable(false);
        }

        public String toString() {
            return cardText;
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

    private Difficulty diff;
    private int rows;
    private int columns;

    final private int cardWidth = 90;
    final private int cardHeight = 128;

    private ArrayList<CardPair> cards;
    ArrayList<Card> cardSet;

    ImageIcon cardBackImageIcon;
    ImageIcon cardFrontImageIcon;

    private int boardWidth;
    private int boardHeight;

    JFrame frame = new JFrame("Memory Juggle");

    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();

    JButton restartButton = new JButton();

    private int errorCount = 0;

    ArrayList<JButton> board;

    Timer previewTimer, hideCardTimer, swapTimer;

    // true only when the player may click (false during preview and swaps)
    boolean gameReady = false;

    Card[] flashed = new Card[2];
    final Border normalBorder = new JButton().getBorder();

    // These must be Card, not JButton
    Card card1Selected;
    Card card2Selected;

    public MatchCards(ArrayList<CardPair> cards, Difficulty diff) {

        this.cards = cards;
        this.diff = diff;

        this.rows = diff.getRows();
        this.columns = diff.getColumns();

        this.boardWidth = columns * cardWidth;
        this.boardHeight = rows * cardHeight;

        System.out.println("Hello from MatchCards");

        setupCards();
        shuffleCards();

        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(Color.decode("#605063"));

        // Error counter display
        textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("ERRORS: " + errorCount);

        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);

        frame.add(textPanel, BorderLayout.NORTH);

        board = new ArrayList<JButton>();

        boardPanel.setLayout(new GridLayout(rows, columns));

        // Create the card buttons
        for (int i = 0; i < cardSet.size(); i++) {

            Card card = cardSet.get(i);
            JButton tile = card.getButton();

            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setFocusable(false);

            tile.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    if (!gameReady) {
                        return;
                    }

                    // Only allow face-down cards to be selected
                    if (tile.getIcon() == cardBackImageIcon) {

                        if (card1Selected == null) {

                            card1Selected = card;

                            tile.setIcon(cardFrontImageIcon);
                            tile.setText(card.getCardText());

                        } else if (card2Selected == null) {

                            card2Selected = card;

                            tile.setIcon(cardFrontImageIcon);
                            tile.setText(card.getCardText());

                            // Check if the selected cards match
                            if (isMatching(card1Selected, card2Selected)) {

                                System.out.println("MATCH!");

                                card1Selected.getButton().setEnabled(false);
                                card2Selected.getButton().setEnabled(false);

                                card1Selected = null;
                                card2Selected = null;
                                afterAttempt();

                            } else {

                                // Count incorrect matches
                                errorCount++;
                                textLabel.setText("ERRORS: " + errorCount);

                                // Hide the cards after a delay
                                hideCardTimer.restart();
                            }
                        }
                    }
                }
            });

            board.add(tile);
            boardPanel.add(tile);
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        // Restart button
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);

        restartGamePanel.add(restartButton);

        frame.add(restartGamePanel, BorderLayout.SOUTH);

        // Restart button action
        restartButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        // Preview ends -> hide everything, then do the visible start swaps
        previewTimer = new Timer(diff.getPreviewMs(), e -> {
            hideAll();
            runSwaps(diff.getStartSwaps());
        });
        previewTimer.setRepeats(false);

        // Wrong pair flips back after a fixed delay on every level
        hideCardTimer = new Timer(1500, e -> hideCards());
        hideCardTimer.setRepeats(false);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Start game
        previewTimer.start();
    }

    // Display original pairs in the console
    public void displayCards() {

        for (CardPair card : cards) {
            System.out.println(
                card.getCardA() + " - " + card.getCardB()
            );
        }
    }

    // Load images and create individual cards
    void setupCards() {

        cardSet = new ArrayList<Card>();

        java.net.URL imageURL1 =
            getClass().getResource("Clover_Card_Back.png");

        java.net.URL imageURL2 =
            getClass().getResource("Clover_Card_Front.png");

        if (imageURL1 == null || imageURL2 == null) {
            throw new IllegalStateException("Card image not found!");
        }

        Image cardBackImg = new ImageIcon(imageURL1).getImage();
        Image cardFrontImg = new ImageIcon(imageURL2).getImage();

        cardBackImageIcon = new ImageIcon(
            cardBackImg.getScaledInstance(
                cardWidth,
                cardHeight,
                Image.SCALE_SMOOTH
            )
        );

        cardFrontImageIcon = new ImageIcon(
            cardFrontImg.getScaledInstance(
                cardWidth,
                cardHeight,
                Image.SCALE_SMOOTH
            )
        );

        int id = 0;

        for (CardPair pair : cards) {

            Card cardA = new Card(pair.getCardA(), id);
            cardSet.add(cardA);

            Card cardB = new Card(pair.getCardB(), id);
            cardSet.add(cardB);

            id++;
        }
    }

    // Shuffle individual cards
    void shuffleCards() {
        Collections.shuffle(cardSet);
    }

    // Check matching words, regardless of their original pair ID
    boolean isMatching(Card card1, Card card2) {

        for (CardPair pair : cards) {

            // Card 1 is A and Card 2 is B
            if (pair.getCardA().equalsIgnoreCase(card1.getCardText())
                    && pair.getCardB().equalsIgnoreCase(card2.getCardText())) {

                return true;
            }

            // Card 1 is B and Card 2 is A
            if (pair.getCardA().equalsIgnoreCase(card2.getCardText())
                    && pair.getCardB().equalsIgnoreCase(card1.getCardText())) {

                return true;
            }
        }

        return false;
    }

    // Hide every card (end of preview)
    void hideAll() {
        for (JButton b : board) {
            b.setText("");
            b.setIcon(cardBackImageIcon);
        }
    }

    // Hide the two cards of an incorrect match
    void hideCards() {

        if (card1Selected != null && card2Selected != null) {

            card1Selected.getButton().setText("");
            card1Selected.getButton().setIcon(cardBackImageIcon);

            card2Selected.getButton().setText("");
            card2Selected.getButton().setIcon(cardBackImageIcon);

            card1Selected = null;
            card2Selected = null;

            afterAttempt();
        }
    }

    // Impossible: swap a few cards in plain sight after every attempt
    void afterAttempt() {
        runSwaps(diff.getAttemptSwaps());
    }

    // Do `count` visible swaps, one per tick, then unlock input.
    // Two unmatched cards trade places and get a yellow border until the next tick.
    void runSwaps(int count) {

        if (swapTimer != null) {
            swapTimer.stop();
        }

        if (count == 0) {
            gameReady = true;
            return;
        }

        gameReady = false;
        int[] left = {count};

        swapTimer = new Timer(diff.getSwapMs(), e -> {
            clearFlash();
            if (left[0]-- == 0) {
                swapTimer.stop();
                gameReady = true;
                return;
            }
            swapRandomPair();
        });
        swapTimer.start();
    }

    void swapRandomPair() {

        ArrayList<Integer> live = new ArrayList<>();
        for (int i = 0; i < cardSet.size(); i++) {
            if (cardSet.get(i).getButton().isEnabled()) {   // not matched yet
                live.add(i);
            }
        }
        if (live.size() < 2) {
            return;
        }

        Collections.shuffle(live);
        int a = live.get(0);
        int b = live.get(1);

        flashed = new Card[]{cardSet.get(a), cardSet.get(b)};
        Collections.swap(cardSet, a, b);
        rebuildBoard();

        for (Card c : flashed) {
            c.getButton().setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
        }
    }

    void clearFlash() {
        for (Card c : flashed) {
            if (c != null) {
                c.getButton().setBorder(normalBorder);
            }
        }
        flashed = new Card[2];
    }

    // Re-add the same buttons in cardSet order (listeners stay attached)
    void rebuildBoard() {

        boardPanel.removeAll();
        board.clear();

        for (Card card : cardSet) {
            board.add(card.getButton());
            boardPanel.add(card.getButton());
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // Restart the game
    void restartGame() {

        previewTimer.stop();
        hideCardTimer.stop();
        if (swapTimer != null) {
            swapTimer.stop();
        }
        clearFlash();

        card1Selected = null;
        card2Selected = null;

        errorCount = 0;
        textLabel.setText("ERRORS: " + errorCount);

        gameReady = false;

        // Face-up again for the preview
        for (Card card : cardSet) {

            JButton button = card.getButton();

            button.setVisible(true);
            button.setEnabled(true);

            button.setText(card.getCardText());
            button.setIcon(cardFrontImageIcon);
        }

        shuffleCards();
        rebuildBoard();

        previewTimer.restart();
    }
}
