import javax.swing.*;
import java.awt.*;

public class SudokuSolver extends JFrame
{
    private final JTextField[][] cells = new JTextField[9][9];

    public SudokuSolver()
    {
        setTitle("Sudoku Solver");
        setLayout(new BorderLayout());

        // 1. Setup 9x9 Grid UI
        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        for (int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                cells[r][c] = new JTextField();
                cells[r][c].setHorizontalAlignment(JTextField.CENTER);
                cells[r][c].setFont(new Font("Arial", Font.BOLD, 20));  // Enlarged font for readability

                // Calculate border thickness
                int top = (r % 3 == 0) ? 3 : 1;
                int left = (c % 3 == 0) ? 3 : 1;
                int bottom = (r == 8) ? 3: 0;
                int right = (c == 8) ? 3: 0;

                // Apply MatteBorder
                cells[r][c].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
                gridPanel.add(cells[r][c]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        // 2. Setup Buttons
        JPanel buttonPanel = new JPanel();
        JButton solveBtn = new JButton("Solve");
        JButton clearBtn = new JButton("Reset");

        solveBtn.addActionListener(_ -> solveGame());
        clearBtn.addActionListener(_ -> resetGame());

        buttonPanel.add(solveBtn);
        buttonPanel.add(clearBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // 3. Reset Feature
    private void resetGame()
    {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                cells[r][c].setText("");
    }

    // 4. Input Parsing & Error Handling
    private void solveGame()
    {
        int[][] board = new int[9][9];
        try
        {
            for (int r = 0; r < 9; r++)
            {
                for (int c = 0; c < 9; c++)
                {
                    String txt = cells[r][c].getText().trim();
                    board[r][c] = txt.isEmpty() ? 0 : Integer.parseInt(txt);
                }
            }
            if (solve(board))
            {
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

    // 5. Backtracking Algorithm
    private boolean solve(int[][] board)
    {
        for (int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                if (board[r][c] == 0) // Find empty cell
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

    // Validation Check for Backtracking
    private boolean isValid(int[][] board, int r, int c, int n)
    {
        for (int i = 0; i < 9; i++)
        {
            if (board[r][i] == n || board[i][c] == n) return false;  // Check row & col
        }
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

    static void main()
    {
        SwingUtilities.invokeLater(SudokuSolver::new);
    }
}