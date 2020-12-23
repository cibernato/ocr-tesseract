import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ICR");
        frame.setContentPane(new Main().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
