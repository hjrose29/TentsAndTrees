import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.NodeChangeListener;

class TreeNode{
    int[][] gameState;
    int[] move;
    double score;
    ArrayList<TreeNode> children;

    public TreeNode(int[][] gameState, int[] move, double score){
        this.gameState = gameState;
        this.move = move;
        this.score = score;
        this.children = new ArrayList<TreeNode>();
    }
    
}

public class GameTreeBuilder{

    PuzzleGenerator pg;
    int depth;

    public GameTreeBuilder(PuzzleGenerator pg, int depth){
        this.pg = pg;
        this.depth = depth;
    }

    public void buildTree(TreeNode curNode, Evaluator ev){
        if(depth == 0) return;

        ArrayList<int[]> moves = ev.validMoves(curNode.gameState);

        for (int[] move : moves) {
            int[][] newState = applyMove(curNode.gameState, move);
            double score = evaluateMove(newState, pg);
    
            TreeNode childNode = new TreeNode(newState, move, score);
            curNode.children.add(childNode);
    
            GameTreeBuilder newBuilder = new GameTreeBuilder(pg, depth - 1);
            newBuilder.buildTree(childNode, ev);
        }

    }

    public void printSubtree(int indent, TreeNode node) {
        final int elemWidth = 4; // Adjust the width as needed
    
        for (int i = 0; i < indent; ++i) {
            System.out.print(" ");
        }
    
        if (node != null) {
            System.out.println("(" + Arrays.toString(node.move) + "," + node.score);
            for(int i = 0; i < node.children.size(); i++){
                printSubtree(indent + elemWidth, node.children.get(i));
            }
    
            for (int i = 0; i < indent; ++i) {
                System.out.print(" ");
            }
            System.out.println(")");
        } else {
            System.out.println("()");
        }
    }
    

    public int[][] applyMove(int[][] inState, int[] move){
        int[][] outState = inState;
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
        double diff = Evaluator.euclideanDistance(curTotals[0], solTotals[0]) + Evaluator.euclideanDistance(curTotals[1], solTotals[1]);
        score -= diff;

        return score;
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