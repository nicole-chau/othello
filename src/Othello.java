import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class Othello {

    private int[][] board;
    private int numTurns;
    private int p1score;
    private int p2score;
    private boolean player1; // p1 = black, p2 = white
    private boolean gameOver;
    

    private boolean[] possibleDir; // true if there is an opposite disc surrounding played position
    private boolean[] possibleFlip; // true if there is a flip possible in that direction 
    private static final int[] ROW_OFFSETS = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] COL_OFFSETS = {-1, 0, 1, -1, 1, -1, 0, 1};
    private static final int ASCII_OFFSET = 48;
    
    private LinkedList<Coordinate> moves;
    
    public Othello() {
        reset();
    }
    
    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[8][8];
        numTurns = 0;
        player1 = true;
        gameOver = false;
        possibleDir = new boolean[8];
        possibleFlip = new boolean[8];
        moves = new LinkedList<Coordinate>();
        
        // place starting discs
        board[3][3] = 1;
        board[3][4] = 2;
        board[4][3] = 2;
        board[4][4] = 1;
    }   
    
    /******************************/
    /***** MAIN GAME FUNCTION *****/
    /******************************/
    
    /**
     * playTurn allows players to play a turn. Returns true 
     * if the move is successful and false if a player tries
     * to play in a location that is taken or after the game
     * has ended. If the turn is successful and the game has 
     * not ended, the player is changed. If the turn is 
     * unsuccessful or the game has ended, the player is not 
     * changed.
     * 
     * RETURNS:
     * - -1 if no moves being played
     * - 0 if successful
     * - 1 if user tries to play multiple discs
     * - 2 if not a valid move
     * - 3 if game over
     */

    public int playTurn(LinkedList<Coordinate> moves) {
        if (moves.isEmpty()) {
            return -1; 
        } else if (moves.size() > 1) {
            return 1;
        }
        
        Coordinate c = moves.getFirst();
        int row = c.getRow();
        int col = c.getCol();      

        // figure out who's playing
        int disc;
        if (player1) {
            disc = 1;
        } else {
            disc = 2;
        }
         
        if (gameOver) {
            moves.clear();
            return 3; 
            
        // if position picked will result in flip, then flip 
        } else if (possibleMove(row, col, disc)) {
            for (int i = 0; i < possibleDir.length; i++) {
                if (possibleDir[i]) {                    
                    int numFlips = canFlip(row, col, disc, i);
                    
                    if (numFlips != 0) {
                        possibleFlip[i] = true;
                        flip(row, col, disc, i, numFlips);                        
                    } else {
                        possibleFlip[i] = false;
                    }
                }
            }
            numTurns++;
            player1 = !player1;
            // clear list of moves for next player
            moves.clear();
            endGame();           
            return 0;
        } else if (endGame() != 0) {
            moves.clear();
            return 3;
        } else {
            endGame();
            return 2;
        }
    }
                
    /**
     * check if a move will cause any update to the game state
     * (i.e. if it is a valid move)
     * 
     * returns false if no flips are possible, else true 
     */
    private boolean possibleMove(int row, int col, int disc) {        
        if (!immediateGrid(row, col, disc)) {
            return false; 
        } else {
            for (int i = 0; i < possibleDir.length; i++) {
                if (possibleDir[i]) {                    
                    int numFlips = canFlip(row, col, disc, i);                    
                    if (numFlips != 0) {
                        possibleFlip[i] = true;                     
                    } else {
                        possibleFlip[i] = false;
                    }
                }
            }
        }
        
        for (int i = 0; i < possibleFlip.length; i++) {
            if (possibleFlip[i]) {
                return true;
            }
        }                
        return false;       
    }
    
    /** checks the grid surrounding the position played
     *  return true if there is at least one grid with
     *  opposite disc color, else return false 
     *  
     *  updates possibleDir array
     */
    private boolean immediateGrid(int row, int col, int disc) {
        int gridNum = -1;
                       
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {              
                if (i != row || j != col) {
                    gridNum++;
                    if (i >= 0 && j >= 0 && i < board.length && j < board[i].length) {
                        if (board[i][j] != 0 && board[i][j] != disc) {                           
                            possibleDir[gridNum] = true;
                        }
                    }
                }
            }
        }
        
        for (int i = 0; i < possibleDir.length; i++) {
            if (possibleDir[i]) {
                return true;
            }
        }        
        return false;
    }
    
    /**
     * checks if a flip can be made given *one specific direction*
     * 
     * returns number of discs that can be flipped, 0 if none 
     */
    private int canFlip(int row, int col, int disc, int idx) {
        int numFlips = 0; 
        int rowOffset = ROW_OFFSETS[idx];
        int colOffset = COL_OFFSETS[idx];        
        
        int r = row + rowOffset;
        int c = col + colOffset; 
        while (r >= 0 && r < board.length && c >= 0 && c < board[r].length) {
            if (board[r][c] != disc && board[r][c] != 0) {
                numFlips++;
            } else if (board[r][c] == 0) { 
                return 0;
            } else if (board[r][c] == disc) {
                return numFlips;
            }
            r = r + rowOffset;
            c = c + colOffset;
        }
        return 0;
    }
    
    /**
     * flips the discs given *one specific direction*
     */
    private void flip(int row, int col, int disc, int idx, int numFlips) {
        int rowOffset = ROW_OFFSETS[idx];
        int colOffset = COL_OFFSETS[idx];
        
        int r = row + rowOffset;
        int c = col + colOffset;
        while (r >= 0 && r < board.length && c >= 0 && c < board[r].length && numFlips != 0) {
            if (board[r][c] != disc && numFlips != 0) {
                board[r][c] = disc;
                numFlips--;
            }
            r = r + rowOffset;
            c = c + colOffset;
        }
    }
    
    /**
     * checks if there are any possible moves for both players 
     * returns true if yes, false if no more moves
     */
    private boolean anyPossibleMoves() {
        boolean any1s = false;
        boolean any2s = false;
        boolean full = true; 
        
        // if board only has 1 color disc or is full -> no more possible moves
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 0) {
                    full = false;
                } else if (board[row][col] == 1) {
                    any1s = true;
                } else if (board[row][col] == 2) {
                    any2s = true;
                }
            }
        }
        
        if (!any1s || !any2s || full) {
            return false;
        }
        
        // iterate through every square and see if placing something there will be possible 
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (possibleMove(row, col, 1) || possibleMove(row, col, 2)) {
                    return true;
                }
            }
        }        
        return false; 
    }

    /**
     * endGame checks whether the game has reached a win 
     * condition
     * 
     * @return 0 if game not over yet (i.e. valid moves still exist),
     * 1 if player 1 has won,
     * 2 if player 2 has won,
     * 3 if the game ties
     */
    public int endGame() {
        p1score = 0;
        p2score = 0;
        // no more possible moves for either player 
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == 1) {  
                    p1score++;
                } else if (board[row][col] == 2) {
                    p2score++;
                }
            }
        }
                
        if (!anyPossibleMoves()) {
            gameOver = true; 
            if (p1score > p2score) {
                return 1;
            } else if (p2score > p1score) {
                return 2;
            } else {
                return 3;
            }
        } else {
            gameOver = false;
            return 0;
        }       
    }
    
    /*************************/
    /***** UNDO FUNCTION *****/
    /*************************/
    
    
    public void addToList(int row, int col) {
        Coordinate c = new Coordinate(row, col);
        moves.add(c);
    }
    
    public void tryCell(int row, int col) {
        if (board[row][col] == 0) {            
            moves.add(new Coordinate(row, col));
        }
        
        if (player1 && board[row][col] == 0) {
            board[row][col] = 1;
        } else if (!player1 && board[row][col] == 0) {
            board[row][col] = 2;
        }
    }
    
    public void undo() {
        if (moves.isEmpty()) {
            return;
        }
        // most recent move at end of list
        Coordinate c = moves.removeLast();
        
        // update array
        int row = c.getRow();
        int col = c.getCol();      
        board[row][col] = 0;
    }
    
    /**
     * skips a player's turn
     */
    public void skip() {
        while (!moves.isEmpty()) {
            undo();
        }
        player1 = !player1;
        
    }
    
    /********************************/
    /***** SAVE & LOAD FUNCTION *****/
    /********************************/
    
    public boolean load() {
        File file = Paths.get("game.txt").toFile();
        BufferedReader br = null; 
        
        try {
            br = new BufferedReader(new FileReader(file));
            
            // load game board 
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    int val = br.read() - ASCII_OFFSET;
                    board[row][col] = val;
                } 
                // read new line character
                br.read();
            }            
            
            // load moves
            String coordinates = br.readLine();
            if (!coordinates.isEmpty()) {
                String[] coordinatesSplit = coordinates.split("/");
                
                for (int i = 0; i < coordinatesSplit.length; i += 2) {
                    int row = Integer.parseInt(coordinatesSplit[i]);
                    int col = Integer.parseInt(coordinatesSplit[i + 1]);
                    Coordinate c = new Coordinate(row, col);
                    moves.add(c);
                }
            }
            
            // load player
            String player = br.readLine();
            if (player.equals("true")) {
                player1 = true;
            } else if (player.equals("false")) {
                player1 = false;
            }          
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                return false;
            }
        }        
        return true;
    }
    
    
    public boolean save() {
        File file = Paths.get("game.txt").toFile();
        BufferedWriter bw = null;
        
        try {
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            
            // save current game board
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    bw.write("" + board[row][col]);
                }
                bw.newLine();
            }
            
            // save moves (for undoing purposes)
            for (Coordinate c : moves) {
                bw.write(c.toString());
                bw.write("/");
            }
            
            bw.newLine();
            
            // save who current player is 
            bw.write("" + player1);            
        } catch (IOException e) {
            return false;
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                return false;
            }
        }
        return true;       
    }
    

    /*****************************/
    /***** GETTERS & SETTERS *****/
    /*****************************/
    
    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numTurns + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 8) { 
                    System.out.print(" | "); 
                }
            }
            if (i < 8) {
                System.out.println("\n-------------------------------"); 
            }
        }
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    public int getNumTurns() {
        return numTurns;
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     * 
     * @return true if it's Player 1's turn,
     * false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }
    
    /**
     * getCell is a getter for the contents of the
     * cell specified by the method arguments.
     * 
     * @param row row to retrieve
     * @param col column to retrieve
     * @return an integer denoting the contents
     *         of the corresponding cell on the 
     *         game board.  0 = empty, 1 = Player 1 = black,
     *         2 = Player 2 = white
     */
    public int getCell(int row, int col) {
        return board[row][col];
    }
    
    
    /** 
     * sets game board to a specific game state
     * used for testing
     */
    public void setBoard(int[][] board) {
        this.board = board;
    }
    
    public boolean getGameOver() {
        return this.gameOver;
    }
    
    public int getP1Score() {
        return this.p1score;
    }
    
    public int getP2Score() {
        return this.p2score;
    }
    
    public LinkedList<Coordinate> getMoves() {
        return this.moves;
    }
}
