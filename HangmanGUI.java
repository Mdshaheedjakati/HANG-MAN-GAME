package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class HangmanGUI {
    private JFrame frame;
    private JPanel wordPanel;
    private JTextField guessField;
    private JButton guessButton;
    private JLabel messageLabel;
    private JLabel guessCountLabel;
    private JLabel hintLabel;
    private JLabel gallowsLabel;
    private String[][] guesses = {
            {"burger", "pizza", "sushi", "pasta", "taco"}, 
            {"paris", "london", "newyork", "tokyo", "sydney"}, 
            {"brazil", "canada", "china", "germany", "india"},
            {"cricket","football","kabaddi","tabletennis"} 
    };
    private String[] categories = {"Food", "Places", "Countries","sports"};
    private char[] randomWordToGuess;
    private char[] playerGuess;
    private int tries;
    private static final int MAX_TRIES = 5;
    private static final Font MONTSERRAT_BOLD_24 = new Font("Montserrat", Font.BOLD, 24);
    private ImageIcon[] gallowsImages = new ImageIcon[7];

    public HangmanGUI() {
        frame = new JFrame("Hangman Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); 

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("HANGMAN GAME", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        panel.add(titleLabel);

        wordPanel = new JPanel();
        wordPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        wordPanel.setBackground(Color.WHITE);
        panel.add(wordPanel);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        guessField = new JTextField(5);
        guessField.setPreferredSize(new Dimension(100, 20));
        guessButton = new JButton("Guess");
        guessButton.setForeground(Color.BLACK);
        inputPanel.add(guessField);
        inputPanel.add(guessButton);
        panel.add(inputPanel);

        guessCountLabel = new JLabel("ONLY " + MAX_TRIES + " GUESSES LEFT", SwingConstants.CENTER);
        guessCountLabel.setFont(MONTSERRAT_BOLD_24);
        guessCountLabel.setForeground(Color.BLACK);
        panel.add(guessCountLabel);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(MONTSERRAT_BOLD_24);
        messageLabel.setForeground(Color.BLACK);
        panel.add(messageLabel);

        hintLabel = new JLabel("", SwingConstants.CENTER);
        hintLabel.setFont(MONTSERRAT_BOLD_24);
        hintLabel.setForeground(Color.BLACK);
        panel.add(hintLabel);

        mainPanel.add(panel, BorderLayout.CENTER);

        gallowsLabel = new JLabel();
        gallowsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gallowsLabel.setVerticalAlignment(SwingConstants.CENTER);
        gallowsLabel.setBackground(Color.BLACK);
        gallowsLabel.setOpaque(true);
        mainPanel.add(gallowsLabel, BorderLayout.EAST);

        frame.add(mainPanel);
        frame.setVisible(true);

        loadFonts();
        loadGallowsImages();
        startNewGame();

        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeGuess();
            }
        });

        guessField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    makeGuess();
                }
            }
        });
    }

    private void loadFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new java.io.File("src/fonts/Montserrat-Bold.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGallowsImages() {
        for (int i = 0; i <= MAX_TRIES; i++) {
            gallowsImages[i] = new ImageIcon("Gallows" + i + ".gif");
        }
    }

    private void startNewGame() {
        Random random = new Random();
        int categoryIndex = random.nextInt(guesses.length);
        int wordIndex = random.nextInt(guesses[categoryIndex].length);
        randomWordToGuess = guesses[categoryIndex][wordIndex].toCharArray();
        playerGuess = new char[randomWordToGuess.length];
        tries = 0;

        for (int i = 0; i < playerGuess.length; i++) {
            playerGuess[i] = '_';
        }

        hintLabel.setText("Hint: " + categories[categoryIndex]);
        updateWordPanel();
        updateGuessCountLabel();
        updateGallowsImage();
        messageLabel.setText("SINGLE CHARACTER");
    }

    private void updateWordPanel() {
        wordPanel.removeAll();
        for (char c : playerGuess) {
            JPanel letterPanel = new JPanel();
            letterPanel.setPreferredSize(new Dimension(40, 40));
            letterPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            letterPanel.setBackground(Color.WHITE);
            JLabel letterLabel = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            letterLabel.setFont(MONTSERRAT_BOLD_24);
            letterLabel.setForeground(Color.BLACK);
            letterPanel.add(letterLabel);
            wordPanel.add(letterPanel);
        }
        wordPanel.revalidate();
        wordPanel.repaint();
    }

    private void updateGuessCountLabel() {
        guessCountLabel.setText("ONLY " + (MAX_TRIES - tries) + " GUESSES LEFT");
    }

    private void updateGallowsImage() {
        gallowsLabel.setIcon(gallowsImages[tries]);
    }

    private void makeGuess() {
        String input = guessField.getText();
        guessField.setText("");
        if (input.length() != 1) {
            messageLabel.setText("ONLY SINGLE CHARACTER");
            return;
        }

        char guess = input.charAt(0);
        boolean correctGuess = false;
        for (int i = 0; i < randomWordToGuess.length; i++) {
            if (randomWordToGuess[i] == guess) {
                playerGuess[i] = guess;
                correctGuess = true;
            }
        }

        if (!correctGuess) {
            tries++;
        }

        updateWordPanel();
        updateGuessCountLabel();
        updateGallowsImage();

        if (isTheWordGuessed()) {
            messageLabel.setText("Congratulations! You've guessed the word.");
            int response = JOptionPane.showConfirmDialog(frame, "You won! Do you want to play again?", "Play Again",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                startNewGame();
            } else {
                System.exit(0);
            }
        } else if (tries >= MAX_TRIES) {
            messageLabel.setText("You've run out of guesses.");
            int response = JOptionPane.showConfirmDialog(frame, "Game over! Do you want to play again?", "Play Again",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                startNewGame();
            } else {
                System.exit(0);
            }
        }
    }

    private boolean isTheWordGuessed() {
        for (char c : playerGuess) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HangmanGUI();
            }
        });
    }
}
