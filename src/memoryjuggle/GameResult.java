package memoryjuggle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

public class GameResult extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GameResult.class.getName());

    public GameResult(ArrayList<CardPair> cards, Difficulty diff, int errors) {
        initComponents();
 
        int correct = cards.size();
        int tries = correct + errors;
        int percent = correct * 100 / tries;
 
        String message;
        if (percent >= 75) {
            message = "Great memory!";
        } else if (percent >= 50) {
            message = "Good job!";
        } else if (percent >= 25) {
            message = "Nice try, keep practicing!";
        } else {
            message = "Don't give up, try again!";
        }
 
        setTitle("Game Result");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 
        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setContentPane(panel);
 
        panel.add(makeLabel("Game Complete!", 28, Font.BOLD));
        panel.add(Box.createVerticalStrut(15));
        panel.add(makeLabel("Score: " + percent + "%", 44, Font.BOLD));
        panel.add(makeLabel(message, 18, Font.PLAIN));
        panel.add(Box.createVerticalStrut(20));
        panel.add(makeLabel("Correct pairs: " + correct, 16, Font.PLAIN));
        panel.add(makeLabel("Wrong tries: " + errors, 16, Font.PLAIN));
        panel.add(makeLabel("Difficulty: " + diff.getDifficulty(), 16, Font.PLAIN));
        panel.add(Box.createVerticalStrut(25));
 
        JButton playAgainBtn = new JButton("Play Again");
        playAgainBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainBtn.addActionListener(e -> {
            new Game(cards, diff).setVisible(true);
            dispose();
        });
        panel.add(playAgainBtn);
        panel.add(Box.createVerticalStrut(10));
 
        JButton menuBtn = new JButton("Main Menu");
        menuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuBtn.addActionListener(e -> {
            new Mainmenu(new SetManager()).setVisible(true);
            dispose();
        });
        panel.add(menuBtn);
 
        setSize(380, 420);
        setLocationRelativeTo(null);
    }
 
    private JLabel makeLabel(String text, int size, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Sylfaen", style, size));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        ArrayList<CardPair> cards = new ArrayList<>();
        cards.add(new CardPair("Dog", "Aso"));
        cards.add(new CardPair("Cat", "Pusa"));
        cards.add(new CardPair("Sun", "Araw"));
        cards.add(new CardPair("Moon", "Buwan"));
        Difficulty diff = new Difficulty();
        diff.easy();
        java.awt.EventQueue.invokeLater(() -> new GameResult(cards, diff, 3).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
