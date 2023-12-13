import java.time.format.TextStyle;
import java.util.ArrayList;



class TreeNode{
    int[][] gameState;
    int[] move;
    double score;
    ArrayList<TreeNode> children;
    TreeNode parentNode;


    public TreeNode(int[][] gameState, int[] move, double score){
        this.gameState = gameState;
        this.move = move;
        this.score = score;
        this.children = new ArrayList<TreeNode>();
    }

    public TreeNode(int[][] gameState, int[] move, double score, TreeNode parentNode){
        this.gameState = gameState;
        this.move = move;
        this.score = score;
        this.parentNode = parentNode;
        this.children = new ArrayList<TreeNode>();
    }
    
}

public class GameTreeBuilder{

    PuzzleGenerator pg;

    public GameTreeBuilder(PuzzleGenerator pg){
        this.pg = pg;
    }

    public TreeNode buildFullTree(TreeNode curNode) {
        if (curNode.children.isEmpty()) {
            // Node is a leaf, explore valid moves
            ArrayList<int[]> validMoves = validMoves(curNode.gameState);

            for (int[] move : validMoves) {
                int[][] newState = applyMove(curNode.gameState, move);
                double score = evaluateMove(newState, pg);

                TreeNode childNode = new TreeNode(newState, move, score, curNode);
                curNode.children.add(childNode);

                buildFullTree(childNode);  // Recursively build the subtree
            }
        }
        return curNode;
    }

    public ArrayList<TreeNode> findWinningPath(TreeNode root) {
        ArrayList<TreeNode> currentPath = new ArrayList<>();
        return findWinningPathRecursive(root, currentPath);
    }

    private ArrayList<TreeNode> findWinningPathRecursive(TreeNode node, ArrayList<TreeNode> currentPath) {
        currentPath.add(node);

        
        if (TentsAndTreesPuzzle.checkForWin(pg.field, node.gameState)) {
            return new ArrayList<>(currentPath); // Found a winning path
        }

        for (TreeNode child : node.children) {
            ArrayList<TreeNode> winningPath = findWinningPathRecursive(child, currentPath);
            if (winningPath != null) {
                return winningPath; // Found a winning path in the subtree
            }
        }

        currentPath.remove(currentPath.size() - 1); // Remove the current node as it didn't lead to a winning path
        return null; // No winning path found in this subtree
    }

    // public ArrayList<TreeNode> findBestPath(TreeNode root) {
    //     ArrayList<TreeNode> winningPath = new ArrayList<>();
    //     double bestScore = Integer.MIN_VALUE;

    //     for (TreeNode child : root.children) {
    //         ArrayList<TreeNode> currentPath = new ArrayList<>();
    //         currentPath.add(root);  // Start the path with the root
    //         currentPath.add(child);
    //         double childScore = miniMax(child, Double.MIN_VALUE, Double.MAX_VALUE, false, currentPath);

    //         if (childScore > bestScore) {
    //             bestScore = childScore;
    //             winningPath = new ArrayList<>(currentPath);
    //         }
    //     }

    //     return winningPath;
    // }

    // private double miniMax(TreeNode node, double alpha, double beta, boolean isMaximizing, ArrayList<TreeNode> currentPath) {
    //     if (node.children.isEmpty()) {
    //         return node.score;
    //     }

    //     double bestScore = isMaximizing ? Double.MIN_VALUE : Double.MAX_VALUE;

    //     for (TreeNode child : node.children) {
    //         currentPath.add(child);
    //         double score = miniMax(child, alpha, beta, !isMaximizing, currentPath);
    //         currentPath.remove(currentPath.size() - 1);

    //         if (isMaximizing) {
    //             bestScore = Math.max(bestScore, score);
    //             alpha = Math.max(alpha, bestScore);
    //         } else {
    //             bestScore = Math.min(bestScore, score);
    //             beta = Math.min(beta, bestScore);
    //         }

    //         if (beta <= alpha) {
    //             break; // Prune the remaining branches
    //         }
    //     }

    //     return bestScore;
    // }
    
    


    

    public int[][] applyMove(int[][] inState, int[] move){
        int[][] outState = new int[inState.length][inState[0].length];
        for(int i = 0; i < inState.length; i++){
            for(int j = 0; j < inState[0].length;j++){
                outState[i][j] = inState[i][j];
            }
        }

        if(move.length != 2) return null;

        if(outState[move[0]][move[1]] == 2) outState[move[0]][move[1]] = 0;
        else outState[move[0]][move[1]] = 2;

        return outState;
    }

    public double evaluateMove(int[][] newState, PuzzleGenerator pg){
        double score = 0;
        if(newState == null){System.out.println("NULL FOUND"); return 0.0;} 

        int[][] curTotals = pg.getRowTotals(newState.length, newState.length, newState);
        int [][] solTotals = pg.getRowTotals(newState.length, newState.length, pg.field);
        double diff = euclideanDistance(curTotals[0], solTotals[0]) + euclideanDistance(curTotals[1], solTotals[1]);
        score -= diff;

        return score;
    }

    public int[] getRandValidMove(int[][] gameState){
        ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
        for(int i = 0; i < gameState.length; i++){
            for(int j = 0; j < gameState[0].length; j++){
                if(pg.isValidTent(i, j, gameState) && TentsAndTreesPuzzle.isNextToTree(i, j, gameState)){
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
                if(pg.isValidTent(i, j, gameState) && TentsAndTreesPuzzle.isNextToTree(i, j, gameState)){
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
    private double miniMax(TreeNode node) {
        if (node.children.isEmpty()) {
            return node.score; // Leaf node
        }
        double maxEval = Double.MIN_VALUE;

        for (TreeNode child : node.children) {
            double eval = miniMax(child);
            maxEval = Math.max(maxEval, eval);
        }
        return maxEval;
    }

    public int[] makeDecision(TreeNode root) {
        TreeNode bestChild = null;
        double bestScore = Integer.MIN_VALUE;

        for (TreeNode child : root.children) {
            double childScore = miniMax(child); 

            if (childScore > bestScore) {
                bestScore = childScore;
                bestChild = child;
            }
        }

        return (bestChild != null) ? bestChild.move : null;
    }
    

}