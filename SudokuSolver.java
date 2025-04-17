import javax.swing.*;
import java.awt.*;

public class SudokuSolver {
    private static final int SIZE = 9;
    private final JTextField[][] cells = new JTextField[SIZE][SIZE];
    private final JFrame frame;

    public SudokuSolver() {
        frame = new JFrame("✨ Sudoku Solver - Pro Edition ✨");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 850);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 247, 255));

        JLabel title = new JLabel("🧩 Sudoku Solver", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(60, 63, 65));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        frame.add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        Font font = new Font("Segoe UI", Font.BOLD, 20);
        Color bg1 = new Color(230, 240, 255);
        Color bg2 = new Color(255, 255, 255);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(font);
                tf.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
                tf.setBackground(((row / 3 + col / 3) % 2 == 0) ? bg1 : bg2);
                tf.setForeground(Color.DARK_GRAY);
                cells[row][col] = tf;
                gridPanel.add(tf);
            }
        }

        // Buttons panel
        JButton solveBtn = styledButton("✅ Solve");
        JButton resetBtn = styledButton("🔄 Reset");

        solveBtn.addActionListener(e -> solveSudoku());
        resetBtn.addActionListener(e -> resetGrid());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(frame.getContentPane().getBackground());
        btnPanel.add(solveBtn);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(resetBtn);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(btnPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(new Color(40, 130, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(130, 40));
        button.setBorder(BorderFactory.createLineBorder(new Color(25, 100, 200), 2));
        return button;
    }

    private void resetGrid() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                cells[i][j].setText("");
                cells[i][j].setForeground(Color.DARK_GRAY);
            }
    }

    private void solveSudoku() {
        int[][] board = new int[SIZE][SIZE];
        try {
            for (int i = 0; i < SIZE; i++)
                for (int j = 0; j < SIZE; j++) {
                    String text = cells[i][j].getText().trim();
                    board[i][j] = text.isEmpty() ? 0 : Integer.parseInt(text);
                }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "❗ Please enter only digits 1-9.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (solve(board)) {
            for (int i = 0; i < SIZE; i++)
                for (int j = 0; j < SIZE; j++) {
                    cells[i][j].setText(String.valueOf(board[i][j]));
                    cells[i][j].setForeground(new Color(30, 100, 180));
                }
        } else {
            JOptionPane.showMessageDialog(frame, "🚫 Sorry!! No solution exists!", "Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean solve(int[][] board) {
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
                if (board[row][col] == 0)
                    for (int num = 1; num <= SIZE; num++)
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solve(board)) return true;
                            board[row][col] = 0;
                        }
        return !containsZero(board);
    }

    private boolean containsZero(int[][] board) {
        for (int[] row : board)
            for (int val : row)
                if (val == 0) return true;
        return false;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int x = 0; x < SIZE; x++)
            if (board[row][x] == num || board[x][col] == num ||
                board[(row / 3) * 3 + x / 3][(col / 3) * 3 + x % 3] == num)
                return false;
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuSolver::new);
    }
}
