import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.Format.Field;

public class TentsAndTreesPuzzle extends JFrame{

    private JButton[][] gridButtons;
    private JLabel[][] totals;  
    private static int gridSize = 6; // Change the grid size as needed
    private static int[][] solution;
    private int[][] gameState;

    private static final String TREE_IMAGE_PATH = "./assets/tree-icon.png";
    private static final String TENT_IMAGE_PATH = "./assets/tent-icon.png";


    public TentsAndTreesPuzzle(){
        setTitle("Tents and Trees Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PuzzleGenerator pg = new PuzzleGenerator(gridSize, gridSize);
        solution = pg.field; // 0: Empty, 1: Tree, 2: Tent
        gameState = pg.getInitialGameState(solution);
        totals = createTotals(pg.getRowTotals(gridSize, gridSize, solution));
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                if(gameState[i][j] == 2) gameState[i][j] = 0;
            }
        }
        createGridButtons(pg, gameState);
        
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
            System.out.println(totals[0][i]);
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
    private JLabel[][] createTotals(int[][] pgTotals) {
        printSolution(pgTotals);
        // Create a 2D array for both row and column totals
        totals = new JLabel[2][gridSize];
        for(int i = 0; i < 2;i++){
            for(int j = 0; j < gridSize; j++){
                totals[i][j] = new JLabel(String.valueOf(pgTotals[i][j]));
                System.out.println(pgTotals[i][j]);
            }
        }
        return totals;
    }


    

    public static void printSolution(int[][] field){
        for(int i = 0; i < field.length; i++){
            for(int j =0; j<field[0].length;j++){   
                System.out.print(field[i][j]);
            }
            System.out.print("\n");
        }
    }
    public static void printField(int[][] field){
        for(int i = 0; i < field.length; i++){
            for(int j =0; j<field[0].length;j++){
                if(field[i][j] == 2) System.out.print(0);
                else System.out.print(field[i][j]);
            }
            System.out.print("\n");
        }
    }

    private void createGridButtons(PuzzleGenerator pg, int[][] field) {
        gridButtons = new JButton[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                JButton button = new JButton();

                // Load and resize the tree image for cells with a tree
                if (field[i][j] == 1) {
                    ImageIcon treeIcon = resizeImageIcon(TREE_IMAGE_PATH, 50, 50);
                    button.setIcon(treeIcon);
                }
                else if(field[i][j] == 2){
                    ImageIcon tentIcon = resizeImageIcon(TENT_IMAGE_PATH, 30, 30);
                    button.setIcon(tentIcon);
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
                        // Add handleButtonClick

                        handleButtonClick(pg, row, col, field);
                    }
                });
                

                gridButtons[i][j] = button;
            }
        }
    }
    public static int[][] getSolution(){
        return solution;
    }
    private void handleButtonClick(PuzzleGenerator pg, int row, int col, int[][] field) {
        if (field[row][col] == 2) {

            // Tent already placed, remove it
            field[row][col] = 0;
            gridButtons[row][col].setIcon(null);
            solution[row][col] = 0;
        } else if (pg.isValidTent(row, col, field) && isNextToTree(row, col, solution)) {
            // Place a tent
            ImageIcon tentIcon = resizeImageIcon(TENT_IMAGE_PATH, 30, 30);
            gridButtons[row][col].setIcon(tentIcon);
            field[row][col] = 2;
            

        }

        if(checkForWin(solution, field)) {System.out.println("WIN"); };
        
        //check for win
    }
    public static boolean isNextToTree(int row, int col, int[][] field){

        if(row + 1 < gridSize && field[row + 1][col] == 1) return true;
        else if(col -1  >= 0 && field[row][col - 1] == 1) return true;
        else if(col + 1 < gridSize && field[row][col + 1] == 1) return true;
        else if(row - 1 >= 0 && field[row - 1][col] == 1) return true;
        return false;
    }

    public static boolean checkForWin(int[][] solution, int[][] field){
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                if(solution[i][j] != field[i][j]){System.out.println("Solution " + solution[i][j] + "Index "
                + i + j + "Field " + field[i][j] + "Index " + i + j); return false;
            }}
        }
        System.out.println("Win");
        return true;
        

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


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TentsAndTreesPuzzle();
            }
        });

    }


    

}