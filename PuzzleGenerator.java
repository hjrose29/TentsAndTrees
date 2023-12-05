import java.util.Arrays;
import java.util.Random;

public class PuzzleGenerator {

    public static final int EMPTY = 0;
    public static final int TREE = 1;
    public static final int TENT = 2;

    private int gridSize;
    private int[][] puzzleGrid;
    private int[] rowTotals;
    private int[] colTotals;

    public PuzzleGenerator(int size) {
        gridSize = size;
        puzzleGrid = new int[gridSize][gridSize];
        rowTotals = new int[gridSize];
        colTotals = new int[gridSize];
        generatePuzzle();
    }

    public int[][] getPuzzleGrid() {
        return puzzleGrid;
    }

    public int[] getRowTotals() {
        return rowTotals;
    }

    public int[] getColTotals() {
        return colTotals;
    }

    private void generatePuzzle() {
        // Clear the grid and totals
        for (int i = 0; i < gridSize; i++) {
            Arrays.fill(puzzleGrid[i], EMPTY);
        }
        
        Arrays.fill(rowTotals, 0);
        Arrays.fill(colTotals, 0);

        Random random = new Random();

        // Place trees randomly in the grid
        for (int i = 0; i < gridSize; i++) {
            int treeCount = random.nextInt(gridSize) + 1; // Random number of trees in each row
            for (int j = 0; j < treeCount; j++) {
                int col;
                do {
                    col = random.nextInt(gridSize);
                } while (puzzleGrid[i][col] != EMPTY); // Ensure the cell is empty
                puzzleGrid[i][col] = TREE;
                colTotals[col]++;
                rowTotals[i]++;
            }
        }

        // Generate the puzzle using backtracking
        solvePuzzle(0, 0);
    }

    private boolean solvePuzzle(int row, int col) {
        // Base case: If we have reached the end of the grid, the puzzle is solved
        if (row == gridSize) {
            return checkNumbers();
        }

        // Try placing a tent at the current position
        if (isValidMove(row, col, TENT)) {
            puzzleGrid[row][col] = TENT;
            colTotals[col]++;
            rowTotals[row]++;

            // Move to the next position
            int nextRow = (col == gridSize - 1) ? row + 1 : row;
            int nextCol = (col == gridSize - 1) ? 0 : col + 1;

            if (solvePuzzle(nextRow, nextCol)) {
                return true;
            }

            // If placing a tent doesn't lead to a solution, backtrack
            puzzleGrid[row][col] = EMPTY;
            colTotals[col]--;
            rowTotals[row]--;
        }

        // Move to the next position
        int nextRow = (col == gridSize - 1) ? row + 1 : row;
        int nextCol = (col == gridSize - 1) ? 0 : col + 1;

        return solvePuzzle(nextRow, nextCol);
    }

    private boolean isValidMove(int row, int col, int value) {
        // Check if placing the given value at the specified position is valid
        // Implement your rules here (e.g., adjacency rules)

        // Check if there's a tree in the same row or column
        for (int i = 0; i < gridSize; i++) {
            if (puzzleGrid[i][col] == TREE || puzzleGrid[row][i] == TREE) {
                return false;
            }
        }

        // Check if two tents are adjacent, even diagonally
        if (row > 0 && col > 0 && puzzleGrid[row - 1][col - 1] == TENT) return false; // Top-left
        if (row > 0 && puzzleGrid[row - 1][col] == TENT) return false; // Top
        if (row > 0 && col < gridSize - 1 && puzzleGrid[row - 1][col + 1] == TENT) return false; // Top-right
        if (col > 0 && puzzleGrid[row][col - 1] == TENT) return false; // Left

        // Placeholder: Allow any move for simplicity
        return true;
    }

    private boolean checkNumbers() {
        // Check if the numbers around the edge of the grid match the number of tents in each row and column
        // Implement your logic here

        // Placeholder: Always return true for simplicity
        return true;
    }
}
