import javax.swing.*;
import java.awt.*;

/**
 * SudokuSolver - A simple Swing-based Sudoku solver application.
 * Features:
 *  - 9x9 Sudoku grid with bold borders for subgrids
 *  - Solve button (uses backtracking algorithm)
 *  - Reset button (clears the grid)
 */

public class SudokuSolver extends JFrame
{
    // ==============================
    // 1. UI Components
    // ==============================
    private final JTextField[][] cells = new JTextField[9][9];  // 9x9 grid of text fields

    // ==============================
    // 2. Constructor - Setup UI
    // ==============================
    public SudokuSolver()
    {
        setTitle("Sudoku Solver");
        setLayout(new BorderLayout());

        // --- Grid Panel (9x9 Sudoku Board) ---
        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        for (int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                cells[r][c] = new JTextField();
                cells[r][c].setHorizontalAlignment(JTextField.CENTER);
                cells[r][c].setFont(new Font("Arial", Font.BOLD, 20));  // Larger font for readability

                // Add thick borders for 3x3 Subgrid separation
                int top = (r % 3 == 0) ? 3 : 1;
                int left = (c % 3 == 0) ? 3 : 1;
                int bottom = (r == 8) ? 3: 0;
                int right = (c == 8) ? 3: 0;
                cells[r][c].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                gridPanel.add(cells[r][c]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        // --- Button Panel (Solve & Reset) ---
        JPanel buttonPanel = new JPanel();
        JButton solveBtn = new JButton("Solve");
        JButton clearBtn = new JButton("Reset");

        solveBtn.addActionListener(_ -> solveGame());
        clearBtn.addActionListener(_ -> resetGame());

        buttonPanel.add(solveBtn);
        buttonPanel.add(clearBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Frame Settings ---
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ==============================
    // 3. Reset Feature
    // ==============================
    private void resetGame()
    {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                cells[r][c].setText("");
    }

    // ==============================
    // 4. Input Parsing & Solve Button
    // ==============================
    private void solveGame()
    {
        int[][] board = new int[9][9];
        try
        {
            // Parse user input into board array
            for (int r = 0; r < 9; r++)
            {
                for (int c = 0; c < 9; c++)
                {
                    String txt = cells[r][c].getText().trim();
                    board[r][c] = txt.isEmpty() ? 0 : Integer.parseInt(txt);
                }
            }

            // Attempt to solve
            if (solve(board))
            {
                // Update UI with solved board
                for (int r = 0; r < 9; r++)
                    for (int c = 0; c < 9; c++)
                        cells[r][c].setText(String.valueOf(board[r][c]));
            }
            else
            {
                JOptionPane.showMessageDialog(this, "No solution exists for this grid!");
            }
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Invalid input! Please use numbers 1-9 only.");
        }
    }

    // ==============================
    // 5. Backtracking Algorithm
    // ==============================
    private boolean solve(int[][] board)
    {
        for (int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                if (board[r][c] == 0) // Empty cell found
                {
                    for (int n = 1; n <= 9; n++)  // Try numbers 1-9
                    {
                        if (isValid(board, r, c, n))
                        {
                            board[r][c] = n;
                            if (solve(board)) return true;  // Recursive step
                            board[r][c] = 0;  // Backtrack if it leads to a dead end
                        }
                    }
                    return false;  // Trigger backtracking
                }
            }
        }
        return true;  // Solved
    }

    // ==============================
    // 6. Validation Check
    // ==============================
    private boolean isValid(int[][] board, int r, int c, int n)
    {
        // Check row & column
        for (int i = 0; i < 9; i++)
        {
            if (board[r][i] == n || board[i][c] == n) return false;  // Check row & col
        }

        // Check 3x3 Subgrid
        int boxRow = r -r % 3, boxCol = c - c % 3;
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (board[boxRow + i][boxCol + j] == n) return false; // Check 3x3 box
            }
        }
        return true;
    }

    // ==============================
    // 7. Main Method
    // ==============================
    static void main()
    {
        SwingUtilities.invokeLater(SudokuSolver::new);
    }
}