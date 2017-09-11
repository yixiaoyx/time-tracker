import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

/**
 * User Interface
 * Created by Yi on 11/9/17.
 */
public class UI {
    private JButton clockButton;
    private JLabel durationDisplay;
    private JComboBox selectTask;
    private JButton addTask;
    private JPanel panel;
    private Driver driver;
    private ScheduledExecutorService display;

    public UI() {
        driver = new Driver();

        durationDisplay.setFont(new Font("Menlo", Font.BOLD, 24));


        // clock IN button action
        clockButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (driver.taskStatus()==false) {
                    driver.startClock();
                    clockButton.setText("Clock OUT");
                    display = Executors.newSingleThreadScheduledExecutor();
                    display.scheduleAtFixedRate(this::displayDuration, 0, 1, TimeUnit.SECONDS);
                } else {
                    driver.stopClock();
                    clockButton.setText("Clock IN");
                    display.shutdown();
                }
            }

            public void displayDuration() {
                durationDisplay.setText(driver.showActiveRuntime());
            }

        });

        // add task button action
        addTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                driver.createTask();
                selectTask.addItem(driver.displayTask());
                durationDisplay.setText(driver.showActiveRuntime());
            }

        });
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Track Attack");
        frame.setContentPane(new UI().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
