package com.grandrp.turf_calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TurfCalculator {

    // SWING Objects private fields
    private JButton calculate_btn;
    public JPanel mainPanel;
    private JComboBox attackSide1;
    private JComboBox attackSide3;
    private JComboBox defenseSide3;
    private JComboBox attackSide2;
    private JComboBox defenseSide1;
    private JComboBox hour1;
    private JComboBox minute1;
    private JComboBox defenseSide2;
    private JComboBox hour2;
    private JComboBox minute2;
    private JComboBox hour3;
    private JComboBox minute3;
    private JComboBox logHour;
    private JComboBox logMinute;
    private JRadioButton isFirstCaptureBtn;
    private JRadioButton isTheSecondCaptureBtn;
    private JRadioButton moreThan2CapturesBtn;
    private JPanel actualCapturePanel;
    private JPanel lastCapturePanel;
    private JPanel secondToLastCapturePanel;

    // Private values
    private boolean isFirstCapture = false;
    private boolean isSecondCapture = false;
    private String errorMessage;
    private String attackSide1Value;
    private String defenseSide1Value;
    private String attackSide2Value;
    private String defenseSide2Value;
    private String attackSide3Value;
    private String defenseSide3Value;

    private List<String> secondToLastTurfGangs;
    private List<String> lastTurfGangs;


    public TurfCalculator(JFrame frame) {
        this.showAllPanel(false, frame);

        calculate_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "WRONG";
                errorMessage = "";

                updateInputData();
                if (!isFirstCapture && !isSecondCapture && !moreThan2CapturesBtn.isSelected()) {
                    errorMessage = "\nPlease select an option";
                } else if (isCorrectCaptureAppointment()) {
                    message = "CORRECT";
                }
                JOptionPane.showMessageDialog(frame, message + errorMessage);
            }
        });

        isFirstCaptureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isFirstCapture = isFirstCaptureBtn.isSelected();
                showAllPanel(false, frame);

                if (isFirstCapture) {
                    isSecondCapture = false;
                    isTheSecondCaptureBtn.setSelected(false);
                    moreThan2CapturesBtn.setSelected(false);
                    actualCapturePanel.setVisible(true);
                    frame.setSize(500, 500);
                }
            }
        });

        isTheSecondCaptureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSecondCapture = isTheSecondCaptureBtn.isSelected();
                showAllPanel(false, frame);

                if (isSecondCapture) {
                    isFirstCapture = false;
                    isFirstCaptureBtn.setSelected(false);
                    moreThan2CapturesBtn.setSelected(false);
                    actualCapturePanel.setVisible(true);
                    lastCapturePanel.setVisible(true);
                    frame.setSize(500, 650);
                }
            }
        });

        moreThan2CapturesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllPanel(false, frame);

                if (moreThan2CapturesBtn.isSelected()) {
                    isFirstCapture = false;
                    isSecondCapture = false;
                    isFirstCaptureBtn.setSelected(false);
                    isTheSecondCaptureBtn.setSelected(false);
                    showAllPanel(true, frame);
                    frame.setSize(500, 750);
                }
            }
        });
    }

    private boolean isCorrectCaptureAppointment() {
        if (!checkData()) {
            this.errorMessage = "\nGangs are same for some capture";
            return false;
        }

        if (!checkAppointmentLapsTime()) {
            this.errorMessage = "\nNot informed between 30 minutes and 20 minutes";
            return false;
        }

        if (!checkAllCooldowns()) {
            this.errorMessage = "\nCapture don't respect cooldown";
            return false;
        }

        return true;
    }

    private boolean checkAppointmentLapsTime() {
        int minAppointmentTime = 30;
        int maxAppointmentTime = 20;
        int appointmentTime = convertTime(logHour, logMinute);
        int captureTime = convertTime(hour3, minute3);

        return (captureTime - appointmentTime) <= minAppointmentTime && (captureTime - appointmentTime) >= maxAppointmentTime;
    }

    private boolean checkData() {
        boolean first = Objects.equals(this.attackSide3Value, this.defenseSide3Value);
        boolean second = Objects.equals(this.attackSide2Value, this.defenseSide2Value);
        boolean third = Objects.equals(this.attackSide1Value, this.defenseSide1Value);

        if (this.isFirstCapture) {
            return !first;
        } else if (this.isSecondCapture) {
            return !first && !second;
        } else {
            return !first && !second && !third;
        }
    }

    private boolean checkAllCooldowns() {
        if (this.isFirstCapture) {
            return true;
        } else if (this.isSecondCapture) {
            if (this.lastTurfGangs.contains(this.attackSide3Value) || this.lastTurfGangs.contains(this.defenseSide3Value)) {
                return checkCooldown(false);
            }
        } else {
            if (this.lastTurfGangs.contains(this.attackSide3Value) || this.lastTurfGangs.contains(this.defenseSide3Value)) {
                return checkCooldown(false);
            } else if (this.secondToLastTurfGangs.contains(this.attackSide3Value) || this.secondToLastTurfGangs.contains(this.defenseSide3Value)) {
                return checkCooldown(true);
            }
        }

        return true;
    }

    private boolean checkCooldown(boolean isSecondToLastTurf) {
        int captureDuration = 16;
        int captureCoolDown = 25;
        int captureTime = convertTime(hour3, minute3);
        int previousCaptureTime = convertTime(hour2, minute2);

        if (isSecondToLastTurf) {
            if ((captureTime - previousCaptureTime) <= captureDuration) {
                return false;
            }
            previousCaptureTime = convertTime(hour1, minute1);
        }

        return (captureTime - previousCaptureTime) >= (captureCoolDown + captureDuration);
    }

    private int convertTime(JComboBox hourItem, JComboBox minuteItem) {
        int hour = Integer.parseInt(Objects.requireNonNull(hourItem.getSelectedItem()).toString());
        int minute = Integer.parseInt(Objects.requireNonNull(minuteItem.getSelectedItem()).toString());
        return (hour*60) + minute;
    }

    private String getStringJComboSelectedItem(JComboBox element) {
        return Objects.requireNonNull(element.getSelectedItem()).toString();
    }

    private void showAllPanel(boolean show, JFrame frame) {
        actualCapturePanel.setVisible(show);
        lastCapturePanel.setVisible(show);
        secondToLastCapturePanel.setVisible(show);

        if (!show) {
            frame.setSize(300,250);
        }
    }

    private void updateInputData() {
        this.attackSide1Value = getStringJComboSelectedItem(this.attackSide1);
        this.defenseSide1Value = getStringJComboSelectedItem(this.defenseSide1);
        this.attackSide2Value = getStringJComboSelectedItem(this.attackSide2);
        this.defenseSide2Value = getStringJComboSelectedItem(this.defenseSide2);
        this.attackSide3Value = getStringJComboSelectedItem(this.attackSide3);
        this.defenseSide3Value = getStringJComboSelectedItem(this.defenseSide3);

        this.secondToLastTurfGangs = Arrays.asList(this.attackSide1Value, this.defenseSide1Value);
        this.lastTurfGangs = Arrays.asList(this.attackSide2Value, this.defenseSide2Value);
    }
}
