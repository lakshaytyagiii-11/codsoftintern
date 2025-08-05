import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class NumberGame extends JFrame {

    private int randomNumber;
    private int attemptsLeft;
    private int score;
    private final int MAX_ATTEMPTS = 7;
    private final int MAX_NUMBER = 100;

    private JLabel instructionLabel;
    private JTextField guessField;
    private JButton guessButton;
    private JLabel feedbackLabel;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JButton playAgainButton;

    public NumberGame() {
        setTitle("Number Guessing Game");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        
        initComponents();
        startNewRound();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        
        instructionLabel = new JLabel("I have a number between 1 and " + MAX_NUMBER + ". Can you guess it?");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 0;
        add(instructionLabel, gbc);

        JPanel guessPanel = new JPanel(new FlowLayout());
        guessField = new JTextField(10);
        guessButton = new JButton("Guess");
        guessPanel.add(guessField);
        guessPanel.add(guessButton);
        gbc.gridy = 1;
        add(guessPanel, gbc);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 2;
        add(feedbackLabel, gbc);

        attemptsLabel = new JLabel("Attempts left: " + MAX_ATTEMPTS);
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(attemptsLabel, gbc);

        scoreLabel = new JLabel("Score: 0");
        gbc.gridx = 1;
        add(scoreLabel, gbc);

        playAgainButton = new JButton("Play Again");
        playAgainButton.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(playAgainButton, gbc);

        guessButton.addActionListener(e -> handleGuess());
        playAgainButton.addActionListener(e -> startNewRound());
        getRootPane().setDefaultButton(guessButton);
    }

    private void startNewRound() {
        Random rand = new Random();
        randomNumber = rand.nextInt(MAX_NUMBER) + 1;
        attemptsLeft = MAX_ATTEMPTS;
        
        instructionLabel.setText("I have a number between 1 and " + MAX_NUMBER + ". Can you guess it?");
        feedbackLabel.setText("Good luck!");
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        guessField.setText("");
        guessField.setEditable(true);
        guessButton.setEnabled(true);
        playAgainButton.setVisible(false);
    }

    private void handleGuess() {
        try {
            int userGuess = Integer.parseInt(guessField.getText());
            
            if (userGuess < 1 || userGuess > MAX_NUMBER) {
                feedbackLabel.setText("Invalid guess. Enter a number between 1 and " + MAX_NUMBER + ".");
                return;
            }

            attemptsLeft--;

            if (userGuess == randomNumber) {
                feedbackLabel.setText("Correct! You guessed the number!");
                score++;
                endRound();
            } else if (attemptsLeft > 0) {
                feedbackLabel.setText(userGuess > randomNumber ? "Too high!" : "Too low!");
                attemptsLabel.setText("Attempts left: " + attemptsLeft);
            } else {
                feedbackLabel.setText("You're out of attempts! The number was " + randomNumber + ".");
                endRound();
            }
        } catch (NumberFormatException ex) {
            feedbackLabel.setText("Invalid input. Please enter a number.");
        }
    }

    private void endRound() {
        guessField.setEditable(false);
        guessButton.setEnabled(false);
        playAgainButton.setVisible(true);
        scoreLabel.setText("Score: " + score);
        attemptsLabel.setText("Attempts left: 0");
        getRootPane().setDefaultButton(playAgainButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NumberGame().setVisible(true);
        });
    }
}