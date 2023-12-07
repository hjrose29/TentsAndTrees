import java.util.random.*;
public class PuzzleGenerator{
    int[][] field;
    PuzzleGenerator(int x, int y){
        field = placeTrees(placeTents(initializeGame(x, y)));
    }   
    //Initialize empty field
    public int[][] initializeGame(int x, int y){
        int[][] out = new int[x][y];
        
        for(int i = 0; i < x; i ++){
            for(int j = 0; j < y; j++){
                out[i][j] = 0;
            }
        }
        return out;
    }

    public int[][] placeTents(int[][] blankField){
        int[][] out = blankField;
        Double rand;
        for(int i = 0; i < out.length;i++){
            for(int j = 0; j < out[0].length;j++){
                 rand = Math.random();
                 if(rand > .5 && isValidTent(i, j, out)){
                    out[i][j] = 2;
                 }

            }
        }

        return out;
    }

    public int[][] placeTrees(int[][] tentedField){
        int[][] out = tentedField;
        int Dx,Dy,rand;
        for(int i = 0; i < out.length; i++){
            for(int j = 0; j < out[0].length; j++){
                       //(int)Math.floor(Math.random() * (max - min + 1) + min);
                if(out[i][j] == 2){
                    Dx=0;Dy=0;
                    //Inefficient
                    while(!isValidTree(i + Dx, j + Dy, tentedField)){
                        Dx=0;Dy=0;rand=-1;
                        rand = (int)Math.floor(Math.random() * (3 + 0 + 1) + 0);
                        if(rand == 0) Dx = 1;
                        else if(rand == 1) Dx = -1;
                        else if(rand == 2) Dy = 1;
                        else if(rand == 3) Dy = -1;
                    }
                    //System.out.println("Rand: " + rand + "\s-\s\s" + i + "\s" + j + "\s->\s" + (i+Dx) + "\s" + (j+Dy));
                    tentedField[i + Dx][j + Dy] = 1;
                }
            }
        }

        return out;
    }

    public int[][] getInitialGameState(int[][] field){
        int[][] out = new int[field.length][field[0].length];
        for(int i = 0; i < out.length; i++){
            for(int j = 0; j < out[0].length; j++){
                out[i][j] = field[i][j];
            }
        }

        for(int i = 0; i < out.length; i++){
            for(int j = 0; j < out.length; j++){
                if(out[i][j] == 2) out[i][j] = 0;
            }
        }
        return out;
    }
    public boolean isValidTent(int row, int col, int[][] field){
        boolean out = true;

        int numRows = field.length;
        int numCols = field[0].length;
        if (row + 1 < numRows && field[row + 1][col] == 2) return false;
        else if (row - 1 >= 0 && field[row - 1][col] == 2) return false;
        else if (col + 1 < numCols && field[row][col + 1] == 2) return false;
        else if (col - 1 >= 0 && field[row][col - 1] == 2) return false;
        else if (row + 1 < numRows && col - 1 >= 0 && field[row + 1][col - 1] == 2) return false;
        else if (row - 1 >= 0 && col - 1 >= 0 && field[row - 1][col - 1] == 2) return false;
        else if (row + 1 < numRows && col + 1 < numCols && field[row + 1][col + 1] == 2) return false;
        else if (row - 1 >= 0 && col + 1 < numCols && field[row - 1][col + 1] == 2) return false;
        return out;
    }

    public boolean isValidTree(int row, int col, int[][] field){
        System.out.println("Invalid");
        boolean out = true;
        
        int numRows = field.length;
        int numCols = field[0].length;

        if(row < 0 || col < 0) return false;
        if(row >= numRows || col >= numCols) return false;
        if(field[row][col] == 1 || field[row][col] == 2) return false;
        
        return out;
    }

    public int[][] getRowTotals(int x, int y, int[][] field){

        int[][] out = new int[2][x];
        for(int l = 0; l < 2; l++){
            for(int k = 0; k < x; k++){
                out[l][k] = 0;
            }
        }

        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                if(field[i][j] == 2){
                    out[0][j]++;
                    out[1][i]++;
                }
            }
        }

        return out;
    }
}
