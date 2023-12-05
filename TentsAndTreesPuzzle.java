import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TentsAndTreesPuzzle extends JFrame {

    private JButton[][] gridButtons;
    private JLabel[][] totals;  // 2D array for both row and column totals
    private int gridSize = 5; // Change the grid size as needed
    private int[][] solution = new int[gridSize][gridSize]; // 0: Empty, 1: Tree, 2: Tent

    // Path to your tree image file
    private static final String TREE_IMAGE_PATH = "./assets/tree-icon.png";
    private static final String TENT_IMAGE_PATH = "./assets/tent-icon.png";

    public TentsAndTreesPuzzle() {
        setTitle("Tents and Trees Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the grid and solution
        PuzzleGenerator puzzleGenerator = new PuzzleGenerator(gridSize);
        solution = puzzleGenerator.getPuzzleGrid();

        // Create the UI components
        createGridButtons();
        createTotals();

        // Set up the layout
        JPanel gridPanel = new JPanel(new GridLayout(gridSize, gridSize));
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridPanel.add(gridButtons[i][j]);
            }
        }

        JPanel totalsPanel = new JPanel(new GridLayout(gridSize + 1, gridSize + 1)); // +1 for row and column totals

        // Add column totals
        totalsPanel.add(new JLabel());  // Empty corner cell
        for (int i = 0; i < gridSize; i++) {
            totalsPanel.add(totals[0][i]);
        }

        // Add row and grid cells
        for (int i = 0; i < gridSize; i++) {
            totalsPanel.add(totals[1][i]);  // Row total
            for (int j = 0; j < gridSize; j++) {
                totalsPanel.add(gridButtons[i][j]);
            }
        }

        setLayout(new BorderLayout());
        add(gridPanel, BorderLayout.CENTER);
        add(totalsPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    private void createTotals() {
        // Create a 2D array for both row and column totals
        totals = new JLabel[2][gridSize];

        // Initialize row totals
        for (int i = 0; i < gridSize; i++) {
            totals[0][i] = new JLabel("", SwingConstants.CENTER);
        }

        // Initialize column totals
        for (int i = 0; i < gridSize; i++) {
            totals[1][i] = new JLabel("", SwingConstants.CENTER);
        }

        updateTotals();
    }

    private void updateTotals() {
        // Update row and column totals based on the current solution
        for (int i = 0; i < gridSize; i++) {
            int rowTotal = 0;
            int colTotal = 0;

            for (int j = 0; j < gridSize; j++) {
                rowTotal += (solution[i][j] == PuzzleGenerator.TENT) ? 1 : 0;
                colTotal += (solution[j][i] == PuzzleGenerator.TENT) ? 1 : 0;
            }

            totals[0][i].setText(Integer.toString(colTotal));  // Column total
            totals[1][i].setText(Integer.toString(rowTotal));  // Row total
        }
    }

    private void createGridButtons() {
        gridButtons = new JButton[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                JButton button = new JButton();

                // Load and resize the tree image for cells with a tree
                if (solution[i][j] == PuzzleGenerator.TREE) {
                    ImageIcon treeIcon = resizeImageIcon(TREE_IMAGE_PATH, 50, 50);
                    button.setIcon(treeIcon);
                }

                Color brownColor = new Color(139, 69, 19);
                button.setBackground(brownColor);
                button.setContentAreaFilled(false); // Make the content area transparent
                button.setOpaque(true); // Make sure the button is opaque
                button.setPreferredSize(new Dimension(50, 50));

                final int row = i;
                final int col = j;

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleButtonClick(row, col);
                    }
                });

                gridButtons[i][j] = button;
            }
        }
    }

    private ImageIcon resizeImageIcon(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();

        // Calculate the aspect ratio
        double aspectRatio = (double) image.getWidth(null) / image.getHeight(null);

        // Calculate the new width based on the aspect ratio
        int newWidth = (int) (height * aspectRatio);

        // Resize the image
        Image scaledImage = image.getScaledInstance(newWidth, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void handleButtonClick(int row, int col) {
        if (solution[row][col] == PuzzleGenerator.TENT) {
            // Tent already placed, remove it
            gridButtons[row][col].setIcon(null);
            solution[row][col] = PuzzleGenerator.EMPTY;
        } else if (isValidMove(row, col, PuzzleGenerator.TENT)) {
            // Place a tent
            ImageIcon tentIcon = resizeImageIcon(TENT_IMAGE_PATH, 30, 30);
            gridButtons[row][col].setIcon(tentIcon);
            solution[row][col] = PuzzleGenerator.TENT;
        }
        updateTotals(); // Update totals after each move
    }

    private boolean isValidMove(int row, int col, int value) {
      
        if(solution[row][col] == PuzzleGenerator.TREE){
            return false;
        }

        int numRows = solution.length;
        int numCols = solution[0].length;
        if (row + 1 < numRows && solution[row + 1][col] == PuzzleGenerator.TENT) return false;
        else if (row - 1 >= 0 && solution[row - 1][col] == PuzzleGenerator.TENT) return false;
        else if (col + 1 < numCols && solution[row][col + 1] == PuzzleGenerator.TENT) return false;
        else if (col - 1 >= 0 && solution[row][col - 1] == PuzzleGenerator.TENT) return false;
        else if (row + 1 < numRows && col - 1 >= 0 && solution[row + 1][col - 1] == PuzzleGenerator.TENT) return false;
        else if (row - 1 >= 0 && col - 1 >= 0 && solution[row - 1][col - 1] == PuzzleGenerator.TENT) return false;
        else if (row + 1 < numRows && col + 1 < numCols && solution[row + 1][col + 1] == PuzzleGenerator.TENT) return false;
        else if (row - 1 >= 0 && col + 1 < numCols && solution[row - 1][col + 1] == PuzzleGenerator.TENT) return false;


        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TentsAndTreesPuzzle();
            }
        });
    }
}
