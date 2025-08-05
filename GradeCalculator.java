import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GradeCalculator extends JFrame {

    private JTextField[] subjectFields;
    private JLabel totalMarksLabel;
    private JLabel averagePercentageLabel;
    private JLabel gradeLabel;
    private final int NUM_SUBJECTS = 5;

    public GradeCalculator() {
        setTitle("Student Grade Calculator");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        subjectFields = new JTextField[NUM_SUBJECTS];
        for (int i = 0; i < NUM_SUBJECTS; i++) {
            JLabel subjectLabel = new JLabel("Subject " + (i + 1) + " Marks:");
            gbc.gridx = 0;
            gbc.gridy = i;
            mainPanel.add(subjectLabel, gbc);

            subjectFields[i] = new JTextField(5);
            gbc.gridx = 1;
            gbc.gridy = i;
            mainPanel.add(subjectFields[i], gbc);
        }

        JButton calculateButton = new JButton("Calculate Grade");
        gbc.gridx = 0;
        gbc.gridy = NUM_SUBJECTS;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 15, 10);
        mainPanel.add(calculateButton, gbc);

        gbc.insets = new Insets(5, 10, 5, 10);
        
        totalMarksLabel = new JLabel("Total Marks: ");
        totalMarksLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = NUM_SUBJECTS + 1;
        gbc.gridwidth = 2;
        mainPanel.add(totalMarksLabel, gbc);

        averagePercentageLabel = new JLabel("Average Percentage: ");
        averagePercentageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = NUM_SUBJECTS + 2;
        gbc.gridwidth = 2;
        mainPanel.add(averagePercentageLabel, gbc);

        gradeLabel = new JLabel("Grade: ");
        gradeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = NUM_SUBJECTS + 3;
        gbc.gridwidth = 2;
        mainPanel.add(gradeLabel, gbc);

        add(mainPanel);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateResults();
            }
        });
    }

    private void calculateResults() {
        try {
            double totalMarks = 0;
            for (int i = 0; i < NUM_SUBJECTS; i++) {
                String text = subjectFields[i].getText();
                if (text.isEmpty()) {
                     JOptionPane.showMessageDialog(this, "Please enter marks for all subjects.", "Input Error", JOptionPane.ERROR_MESSAGE);
                     return;
                }
                double marks = Double.parseDouble(text);
                if (marks < 0 || marks > 100) {
                    JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                totalMarks += marks;
            }

            double averagePercentage = totalMarks / NUM_SUBJECTS;
            String grade = calculateGrade(averagePercentage);

            totalMarksLabel.setText(String.format("Total Marks: %.2f / %d", totalMarks, NUM_SUBJECTS * 100));
            averagePercentageLabel.setText(String.format("Average Percentage: %.2f%%", averagePercentage));
            gradeLabel.setText("Grade: " + grade);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for all subjects.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String calculateGrade(double percentage) {
        if (percentage >= 90) {
            return "A+ (Outstanding)";
        } else if (percentage >= 80) {
            return "A (Excellent)";
        } else if (percentage >= 70) {
            return "B (Good)";
        } else if (percentage >= 60) {
            return "C (Satisfactory)";
        } else if (percentage >= 50) {
            return "D (Pass)";
        } else {
            return "F (Fail)";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GradeCalculator().setVisible(true);
            }
        });
    }
}