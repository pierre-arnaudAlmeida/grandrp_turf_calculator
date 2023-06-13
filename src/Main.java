import com.grandrp.turf_calculator.TurfCalculator;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame window = new JFrame("Turf calculator");
                window.setContentPane(new TurfCalculator(window).mainPanel);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.pack();
                window.setVisible(true);
            }
        });
    }
}