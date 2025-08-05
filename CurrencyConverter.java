import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter extends JFrame {

    private JComboBox<String> baseCurrencyBox;
    private JComboBox<String> targetCurrencyBox;
    private JTextField amountField;
    private JLabel resultLabel;

    private final String[] currencies = {
        "USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD", "INR"
    };

    public CurrencyConverter() {
        setTitle("Currency Converter");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel amountLabel = new JLabel("Amount:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(amountLabel, gbc);

        amountField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(amountField, gbc);

        JLabel baseCurrencyLabel = new JLabel("From:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(baseCurrencyLabel, gbc);

        baseCurrencyBox = new JComboBox<>(currencies);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(baseCurrencyBox, gbc);
        
        JLabel targetCurrencyLabel = new JLabel("To:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(targetCurrencyLabel, gbc);
        
        targetCurrencyBox = new JComboBox<>(currencies);
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(targetCurrencyBox, gbc);

        JButton convertButton = new JButton("Convert");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(convertButton, gbc);

        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(resultLabel, gbc);

        add(mainPanel);

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });
    }

    private void convertCurrency() {
        try {
            String baseCurrency = (String) baseCurrencyBox.getSelectedItem();
            String targetCurrency = (String) targetCurrencyBox.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());

            String urlString = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                conn.disconnect();

                // Manual JSON parsing
                String response = content.toString();
                String searchString = "\"" + targetCurrency + "\":";
                int startIndex = response.indexOf(searchString);
                
                if (startIndex == -1) {
                    throw new RuntimeException("Target currency not found in API response.");
                }
                
                startIndex += searchString.length();
                int endIndex = response.indexOf(",", startIndex);
                if (endIndex == -1) { // It might be the last rate in the list
                    endIndex = response.indexOf("}", startIndex);
                }
                
                String rateString = response.substring(startIndex, endIndex).trim();
                double exchangeRate = Double.parseDouble(rateString);
                
                double convertedAmount = amount * exchangeRate;

                resultLabel.setText(String.format("Result: %.2f %s", convertedAmount, targetCurrency));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CurrencyConverter().setVisible(true);
            }
        });
    }
}