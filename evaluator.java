import java.util.ArrayList;

public class Evaluator{
    PuzzleGenerator pg;

    public Evaluator(PuzzleGenerator pg){
        this.pg = pg;
    }

    public double heuristicFunction(int[][] solution, int[][] gameState){
        double fitness = 0;
        double curDiff = 0;
        if(TentsAndTreesPuzzle.checkForWin(solution, gameState)){
            fitness += 100;
            return fitness;
        }   
        else{
            int[] move = getRandValidMove(gameState);
            int[][] curTotals = pg.getRowTotals(gameState.length, gameState.length, gameState);
            int [][] solTotals = pg.getRowTotals(gameState.length, gameState.length, solution);
            double diff = euclideanDistance(curTotals[0], solTotals[0]) + euclideanDistance(curTotals[1], solTotals[1]);
            if(diff < curDiff){

            }
            


        }

        return fitness;
    }


    public int[] getRandValidMove(int[][] gameState){
        ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
        for(int i = 0; i < gameState.length; i++){
            for(int j = 0; j < gameState[0].length; j++){
                if(gameState[i][j] == 2){
                    int[] move = {i, j};
                    possibleMoves.add(move);
                }
                else if(pg.isValidTent(i, j, gameState) && TentsAndTreesPuzzle.isNextToTree(i, j, gameState)){
                    int[] move = {i, j};
                    possibleMoves.add(move);
                }
            }
        }
        return possibleMoves.get((int)Math.floor(Math.random() * ((possibleMoves.size() - 1) + 1)));
    }

    public ArrayList<int[]> validMoves(int[][] gameState){
        ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
        for(int i = 0; i < gameState.length; i++){
            for(int j = 0; j < gameState[0].length; j++){
                if(gameState[i][j] == 2){
                    int[] move = {i, j};
                    possibleMoves.add(move);
                }
                else if(pg.isValidTent(i, j, gameState) && TentsAndTreesPuzzle.isNextToTree(i, j, gameState)){
                    int[] move = {i, j};
                    possibleMoves.add(move);
                }
            }
        }
        return possibleMoves;
    }

    public static double euclideanDistance(int[] q, int[] p){
        int out = 0;
        if(q.length != p.length) return -1;
        for(int i = 0; i < q.length; i++){
            out += ((q[i] - p[i]) * (q[i] - p[i]));
        }
        return Math.sqrt(out);
    }

}
