package app;

import gui.GUI;
import gui.Login;

import javax.swing.*;

@SuppressWarnings("BusyWait")
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // SHOW LOGIN
        Login login = new Login();
        Thread loginThread = new Thread(login);
        loginThread.start();

        while (loginThread.isAlive())
            Thread.sleep(200);

        // in case that login thread failed but user did not authorize - exit
        if (!login.isAuthorized()) {
            JOptionPane.showMessageDialog(null, "User unauthorized", "Access denied", JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }

        // START MAIN GUI
        SwingUtilities.invokeLater(new GUI());
    }
}
