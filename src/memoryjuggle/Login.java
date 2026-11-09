package memoryjuggle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public Login() {
        setTitle("Memory Juggle - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setContentPane(panel);

        panel.add(makeLabel("Memory Juggle", 28, Font.BOLD));
        panel.add(Box.createVerticalStrut(20));

        panel.add(makeLabel("Username", 14, Font.PLAIN));
        usernameField = new JTextField(15);
        usernameField.setMaximumSize(new Dimension(250, 30));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(10));

        panel.add(makeLabel("Password", 14, Font.PLAIN));
        passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(new Dimension(250, 30));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(10));

        errorLabel = makeLabel(" ", 12, Font.PLAIN);
        errorLabel.setForeground(Color.YELLOW);
        panel.add(errorLabel);
        panel.add(Box.createVerticalStrut(10));

        JButton loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> attemptLogin());
        panel.add(loginBtn);

        // Pressing Enter also logs in
        getRootPane().setDefaultButton(loginBtn);

        setSize(360, 380);
        setLocationRelativeTo(null);
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in both fields.");
            return;
        }

        if (checkCredentials(username, password)) {
            new Mainmenu(new SetManager()).setVisible(true);
            dispose();
        } else {
            errorLabel.setText("Wrong username or password.");
            passwordField.setText("");
        }
    }

    // Placeholder check. Replace this with your database or file lookup.
    private boolean checkCredentials(String username, String password) {
        return username.equals("admin") && password.equals("1234");
    }

    private JLabel makeLabel(String text, int size, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Sylfaen", style, size));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Login().setVisible(true));
    }
}