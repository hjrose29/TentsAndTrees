import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TentsAndTreesPuzzle extends JFrame{

    private JButton[][] gridButtons;
    private JLabel[][] totals;  
    private int gridSize; // Change the grid size as needed
    private int[][] solution;
    private int[][] gameState;
    private TreeNode root;
    
    private static final String TREE_IMAGE_PATH = "./assets/tree-icon.png";
    private static final String TENT_IMAGE_PATH = "./assets/tent-icon.png";
    private static final String HINT_IMAGE_PATH = "./assets/hint-icon.png";



    public TentsAndTreesPuzzle(int gridSize){
        this.gridSize = gridSize;
        setTitle("Tents and Trees Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PuzzleGenerator pg = new PuzzleGenerator(gridSize, gridSize);
        solution = pg.field; // 0: Empty, 1: Tree, 2: Tent
        System.out.println(solution.length);
        gameState = pg.getInitialGameState(solution);
        totals = createTotals(pg.getRowTotals(gridSize, gridSize, solution));
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                if(gameState[i][j] == 2) gameState[i][j] = 0;
            }
        }
        GameTreeBuilder builder = new GameTreeBuilder(pg, 4);
        createGridButtons(pg, gameState, builder);
        
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

        
    
        TreeNode root = new TreeNode(gameState, new int[0], 0);


        final int depth = 20;

        //builder.buildFullTreeParallel(root, depth);
        builder.buildFullTreeParallel(root, depth);
        printSolution(root.gameState);
        System.out.println(root.children.size());

        ArrayList<TreeNode> winningPath = builder.findWinningPath(root);
        ImageIcon hintIcon = resizeImageIcon(HINT_IMAGE_PATH, 30, 30);

        int[] firstMove = winningPath.get(1).move;
    
        gridButtons[firstMove[0]][firstMove[1]].setIcon(hintIcon);

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

    private void createGridButtons(PuzzleGenerator pg, int[][] field, GameTreeBuilder builder) {
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

                        handleButtonClick(pg, row, col, field, builder);
                    }
                });
                

                gridButtons[i][j] = button;
            }
        }
    }
    public int[][] getSolution(){
        return solution;
    }
    private void handleButtonClick(PuzzleGenerator pg, int row, int col, int[][] field, GameTreeBuilder builder) {
        if (field[row][col] == 2) {

            // Tent already placed, remove it
            field[row][col] = 0;
            gridButtons[row][col].setIcon(null);

            if(checkForWin(solution, field)){ setTitle("You win!"); return;}
            
        } else if (pg.isValidTent(row, col, field) && isNextToTree(row, col, solution)) {
            // Place a tent
            ImageIcon tentIcon = resizeImageIcon(TENT_IMAGE_PATH, 30, 30);
            gridButtons[row][col].setIcon(tentIcon);
            field[row][col] = 2;
            
            //Suggest Next Move
            root = new TreeNode(field, new int[0], 0);
            builder.buildFullTree(root);
            ArrayList<TreeNode> winningPath = builder.findWinningPath(root);
            if(checkForWin(solution, field)){ setTitle("You win!"); return;}
            if(winningPath.size() > 0){
                ImageIcon hintIcon = resizeImageIcon(HINT_IMAGE_PATH, 30, 30);
                int[] firstMove = winningPath.get(1).move;
                gridButtons[firstMove[0]][firstMove[1]].setIcon(hintIcon);
            }
        }

        
        
        //check for win
    }
    public static boolean isNextToTree(int row, int col, int[][] field){

        if(row + 1 < field.length && field[row + 1][col] == 1) return true;
        else if(col - 1  >= 0 && field[row][col - 1] == 1) return true;
        else if(col + 1 < field.length && field[row][col + 1] == 1) return true;
        else if(row - 1 >= 0 && field[row - 1][col] == 1) return true;
        return false;
    }

    public static boolean checkForWin(int[][] solution, int[][] field){
        for(int i = 0; i < solution.length; i++){
            for(int j = 0; j < solution.length; j++){
                if(solution[i][j] != field[i][j]) return false;

            }
        }
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
                new TentsAndTreesPuzzle(7);
            }
        });

        

    }

    public static String formatArr(int[] arr){
        String out = "[ ";
        for(int i = 0; i < arr.length; i++){
            if(i == arr.length - 1) out += arr[i] + "]";
            else out += arr[i] + ",";
        }
        return out;
    }

    
    // public static void main(String[] args) {
        
    //         int gridSize = 5;

    //         PuzzleGenerator pg = new PuzzleGenerator(gridSize, gridSize);
    //         int[][] solution = pg.field;
    //         int[][] gameState = pg.getInitialGameState(solution);
    
    //         GameTreeBuilder builder = new GameTreeBuilder(pg);
    
    //         TreeNode root = new TreeNode(gameState, new int[0], 0);
    //         builder.buildFullTree(root);
    
    //         ArrayList<TreeNode> winningPath = builder.findWinningPath(root);
    
    //         if (winningPath != null) {
    //             System.out.println("Found winning path:");
    //             for (TreeNode node : winningPath) {
    //                 printNodeDetails(node);
    //             }
    //         } else {
    //             System.out.println("No winning path found.");
    //         }
    //     }
    
        private static void printNodeDetails(TreeNode node) {
            // Implement how you want to print details of a node
            // For example, print game state, move, score, etc.
            System.out.println("GameState: ");
            TentsAndTreesPuzzle.printSolution(node.gameState);
            System.out.println("Move: " + TentsAndTreesPuzzle.formatArr(node.move));
            System.out.println("Score: " + node.score);
            System.out.println("--------------");
    }

}