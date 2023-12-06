import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TentsAndTreesPuzzle extends JFrame{

    private JButton[][] gridButtons;
    private JLabel[][] totals;  
    private int gridSize = 5; // Change the grid size as needed
    private int[][] solution;

    private static final String TREE_IMAGE_PATH = "./assets/tree-icon.png";
    private static final String TENT_IMAGE_PATH = "./assets/tent-icon.png";


    public TentsAndTreesPuzzle(){
        setTitle("Tents and Trees Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PuzzleGenerator pg = new PuzzleGenerator(gridSize, gridSize);
        solution = pg.field; // 0: Empty, 1: Tree, 2: Tent

    }
    public static void main(String[] args) {
        
        PuzzleGenerator pg = new PuzzleGenerator(5, 5);
        int[][] field = pg.field;
        printField(field);

        System.out.println();

        field = pg.placeTents(field);
        printField(field);

        System.out.println();

        field = pg.placeTrees(field);
        printField(field);
        System.out.println("\n\n\n\n\n\n\n\n\n");
        printSolution(field);

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



    

}