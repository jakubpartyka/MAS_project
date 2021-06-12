package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login implements Runnable{
    private JTextField loginField;
    private JPasswordField passwordField;
    private JLabel loginLabel;
    private JLabel passLabel;
    private JButton zalogujButton;
    private JPanel mainPanel;

    private boolean authorized = false;

    private void addListeners() {
        zalogujButton.addActionListener(e -> {
            System.out.println("klikłeś");
        });
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Logowanie użytkownika");
        frame.add(mainPanel);
        frame.setSize(400,300);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        //binding
        addListeners();

        frame.setVisible(true);

    }

    public boolean isAuthorized() {
        return authorized;
    }
}
