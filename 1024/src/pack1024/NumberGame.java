package pack1024;
/**
 * Write a description of class NumberGame here.
 * 
 * @author CIS163 
 * @version Winter 2017
 */
import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;

public class NumberGame implements NumberSlider
{
    private final int ROWS = 4;
    private final int COLS = 4;
     int[][] board = new int[ROWS][COLS];
    private Stack < int[][] > history = new Stack < int[][] > ();
    private int winningValue = 1024;
    private ArrayList<Integer> gscores = new ArrayList<Integer>();
    private Stack <ArrayList<Integer>> historyScores = new Stack <ArrayList<Integer>>();
    
    public void resizeBoard (int height, int width, int winningValue) {
        int [][] newBoard = new int[height][width];
        board = newBoard;
        this.winningValue = winningValue;
    }

    public void reset() {
        for(int i = 0;i < board.length;i++) {
            for(int j = 0;j < board[0].length;j++) {
                board[i][j] = 0;
            }
        }
        placeRandomValue();
        placeRandomValue();
        historyScores.clear();
        Sequence.clearScores();
        gscores.clear();
    }

    public void setValues(final int[][] ref) {
        for (int i = 0;i < board.length;i++) {
            for(int j = 0;j < board[0].length;j++) {
                board[i][j] = ref[i][j];
            }
        }
    }

    public Cell placeRandomValue() {
        Random generator = new Random();
        int newRow = generator.nextInt(board.length);
        int newCol = generator.nextInt(board[0].length);
        while (board[newRow][newCol] != 0) {
            newRow = generator.nextInt(board.length);
            newCol = generator.nextInt(board[0].length);
        }
        board[newRow][newCol] = 2; // hard coded for now...
        return new Cell(newRow,newCol,board[newRow][newCol]);
    }

    private void pushCurrentBoard() {
        int [][] aux = new int [board.length][board[0].length];
        for(int i = 0;i < board.length;i++) {
            for(int j = 0; j < board[0].length;j++) {
                aux[i][j] = board[i][j];
            }
        }
        history.push(aux);
        historyScores.push(gscores);
    }
    
    public boolean slide (SlideDirection dir) {
        int [] auxRow = new int [board[0].length];
        int [] auxCol = new int [board.length];
        boolean moved = false;
        pushCurrentBoard();
        if (dir == SlideDirection.LEFT) {
            // Go through all the rows in the board
            for(int i = 0;i < board.length;i++) {
                Sequence res = new Sequence(board[i]);
                auxRow = res.combineLeft1024();
                Sequence auxSequence = new Sequence(auxRow);
                if (!res.equals(auxSequence)) { // This row changed
                    moved = true;
                }
                board[i] = auxRow;
            }
            if (moved) {
                placeRandomValue();
            }
            
            gscores.add(Sequence.scores());
            return moved;
        }
        if (dir == SlideDirection.RIGHT) {
            // Go through all the rows in the board
            for(int i = 0;i < board.length;i++) {
                Sequence res = new Sequence(board[i]);
                auxRow = res.combineRight1024();
                Sequence auxSequence = new Sequence(auxRow);
                if (!res.equals(auxSequence)) { // This row changed
                    moved = true;
                }
                board[i] = auxRow;
            }
            if (moved) {
                placeRandomValue();
            }
            gscores.add(Sequence.scores());
            return moved;
        }
        if (dir == SlideDirection.UP) {
            // Go through all the cols in the board
            for(int i = 0;i < board[0].length;i++) {
                // Extract col i
                for(int j = 0; j < board.length;j++) {
                    auxCol[j] = board[j][i];
                }
                Sequence res = new Sequence(auxCol);
                int [] resultCol = res.combineLeft1024();
                Sequence auxSequence = new Sequence(resultCol);
                if (!res.equals(auxSequence)) { // This row changed
                    moved = true;
                }
                for(int j = 0; j < board.length;j++) {
                    board[j][i] = resultCol[j];
                }
            }
            if (moved) {
                placeRandomValue();
            }
            gscores.add(Sequence.scores());
            return moved;
        }
         if (dir == SlideDirection.DOWN) {
            // Go through all the cols in the board
            for(int i = 0;i < board[0].length;i++) {
                // Extract col i
                for(int j = 0; j < board.length;j++) {
                    auxCol[j] = board[j][i];
                }
                Sequence res = new Sequence(auxCol);
                int [] resultCol = res.combineRight1024();
                Sequence auxSequence = new Sequence(resultCol);
                if (!res.equals(auxSequence)) { // This row changed
                    moved = true;
                }
                for(int j = 0; j < board.length;j++) {
                    board[j][i] = resultCol[j];
                }
            }
            if (moved) {
                placeRandomValue();
            }
            gscores.add(Sequence.scores());
            return moved;
        }
        return false;
    }

    public ArrayList<Cell> getNonEmptyTiles() {
        ArrayList < Cell > result = new ArrayList < Cell > ();
        for(int i = 0;i < board.length;i++) {
            for(int j = 0;j < board[0].length;j++) {
                if (board[i][j] != 0) {
                    Cell newCell = new Cell(i,j,board[i][j]);
                    result.add(newCell);
                }
            }
        }
        return result;
    }

    private boolean boardFull() {
      
        for(int i = 0;i < board.length;i++) {
            for(int j = 0;j < board[0].length;j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    public GameStatus getStatus() {
        // Check if the user won
        for (int i = 0;i < board.length;i++) {
            for(int j = 0;j < board[0].length;j++) {
                if (board[i][j] == winningValue) {
                    return GameStatus.USER_WON;
                }
            }
        } 
        // Check if the user lost
        if (boardFull() &&
        slide(SlideDirection.LEFT) == false &&
        slide(SlideDirection.RIGHT) == false &&
        slide(SlideDirection.UP) == false &&
        slide(SlideDirection.DOWN) == false) {
            return GameStatus.USER_LOST;
        }

        return GameStatus.IN_PROGRESS;
    }

    public void undo() {
        if (!history.isEmpty()) {
        	board = history.pop();
        }
        
        if(!gscores.isEmpty()){
        gscores.remove(gscores.size()-1);
        Sequence.undoScore();
    }
    }

	public Integer getScore() {
		if(gscores.size() > 0) {
		return gscores.get(gscores.size()-1);
		}
		return 0;
		
	}
}
