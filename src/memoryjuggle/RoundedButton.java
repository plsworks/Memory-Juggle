package memoryjuggle;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    private int radius = 20;

    public RoundedButton(String text) {
        super(text);

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setBackground(Color.decode("#605063"));
        setFont(new Font("Arial", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(getBackground());

        g2.fillRoundRect(
            0, 0,
            getWidth(), getHeight(),
            radius, radius
        );

        super.paintComponent(g2);

        g2.dispose();
    }
}