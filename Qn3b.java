// A Game of Tetris
// Functionality:
// Queue: Use a queue to store the sequence of falling blocks.
// Stack: Use a stack to represent the current state of the game board.
// GUI:
// A game board with grid cells.
// A preview area to show the next block.
// Buttons for left, right, and rotate.
// Implementation:
// Initialization:
//  Create an empty queue to store the sequence of falling blocks.
//  Create an empty stack to represent the game board.
//  Initialize the game board with empty cells.
//  Generate a random block and enqueue it.
// Game Loop:
// While the game is not over:
//  Check for game over: If the top row of the game board is filled, the game is over.
//  Display the game state: Draw the current state of the game board and the next block in the
// preview area.
// Handle user input:
//  If the left or right button is clicked, move the current block horizontally if possible.
//  If the rotate button is clicked, rotate the current block if possible.
//  Move the block: If the current block can move down without colliding, move it down. Otherwise:
//  Push the current block onto the stack, representing its placement on the game board.
//  Check for completed rows: If a row is filled, pop it from the stack and add a new empty row at the
// top.
//  Generate a new random block and enqueue it.
// Game Over:
//  Display a game over message and the final score.
// Data Structures:
// Block: A class or struct to represent a Tetris block, including its shape, color, and current position.
// GameBoard: A 2D array or matrix to represent the game board, where each cell can be empty or filled
// with a block.
// Queue: A queue to store the sequence of falling blocks.
// Stack: A stack to represent the current state of the game board.
// Additional Considerations:
// Collision detection: Implement a function to check if a block can move or rotate without colliding with
// other blocks or the game board boundaries.
// Scoring: Implement a scoring system based on factors like completed rows, number of blocks placed, and
// other game-specific rules.
// Leveling: Increase the speed of the falling blocks as the player's score increases.
// Power-ups: Add power-ups like clearing lines, adding extra rows, or changing the shape of the current
// block.

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;

// Class to represent a Tetris block
class Block {
    int[][] shape; // 2D array representing the block's shape
    Color color; // Color of the block
    int x, y; // Position of the block on the game board

    // Constructor to initialize a block with its shape and color
    public Block(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        this.x = 0; // Start at the top-left corner
        this.y = 3; // Center horizontally
    }

    // Method to rotate the block 90 degrees clockwise
    public void rotate() {
        int[][] rotated = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                rotated[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        shape = rotated;
    }
}

// Main Tetris game class using Swing
public class Question3b extends JPanel implements ActionListener, KeyListener {
    private static final int BOARD_WIDTH = 10; // Width of the game board
    private static final int BOARD_HEIGHT = 20; // Height of the game board
    private static final int TILE_SIZE = 30; // Size of each tile in pixels
    private static final int FPS = 10; // Frames per second (reduced for slower speed)
    private static final int DELAY = 1000 / FPS; // Delay between frames in milliseconds

    private final int[][] gameBoard; // 2D array representing the game board
    private final Queue<Block> blockQueue; // Queue to store the sequence of falling blocks
    private Block currentBlock; // Current falling block
    private int score; // Player's score
    private final javax.swing.Timer timer; // Timer for game updates (explicitly use javax.swing.Timer)

    // Predefined colors for blocks
    private static final Color[] COLORS = {
        Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.CYAN, Color.MAGENTA
    };

    // Constructor to initialize the game
    public Question3b() {
        setPreferredSize(new Dimension(BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        gameBoard = new int[BOARD_HEIGHT][BOARD_WIDTH]; // Initialize empty game board
        blockQueue = new LinkedList<>(); // Initialize empty block queue
        score = 0; // Initialize score
        generateNewBlock(); // Generate the first block

        timer = new javax.swing.Timer(DELAY, this); // Use javax.swing.Timer
        timer.start();
    }

    // Method to generate a new random block and enqueue it
    private void generateNewBlock() {
        Random random = new Random();
        int[][][] shapes = {
            {{1, 1, 1, 1}}, // I-block
            {{1, 1}, {1, 1}}, // O-block
            {{1, 1, 1}, {0, 1, 0}}, // T-block
            {{1, 1, 0}, {0, 1, 1}}, // Z-block
            {{0, 1, 1}, {1, 1, 0}}, // S-block
            {{1, 0, 0}, {1, 1, 1}}, // L-block
            {{0, 0, 1}, {1, 1, 1}} // J-block
        };
        int[][] shape = shapes[random.nextInt(shapes.length)];
        Color color = COLORS[random.nextInt(COLORS.length)]; // Use predefined colors
        Block block = new Block(shape, color);
        blockQueue.add(block);
        if (currentBlock == null) {
            currentBlock = blockQueue.poll(); // Set the current block if it's not initialized yet
        }
    }

    // Method to check if a block can move to a new position
    private boolean canMove(Block block, int newX, int newY) {
        for (int i = 0; i < block.shape.length; i++) {
            for (int j = 0; j < block.shape[0].length; j++) {
                if (block.shape[i][j] != 0) {
                    int x = newX + i;
                    int y = newY + j;
                    if (x < 0 || x >= BOARD_HEIGHT || y < 0 || y >= BOARD_WIDTH || gameBoard[x][y] != 0) {
                        return false; // Collision detected
                    }
                }
            }
        }
        return true; // No collision
    }

    // Method to move the current block left
    public void moveLeft() {
        if (canMove(currentBlock, currentBlock.x, currentBlock.y - 1)) {
            currentBlock.y--; // Move the block left
        }
    }

    // Method to move the current block right
    public void moveRight() {
        if (canMove(currentBlock, currentBlock.x, currentBlock.y + 1)) {
            currentBlock.y++; // Move the block right
        }
    }

    // Method to rotate the current block
    public void rotateBlock() {
        Block rotatedBlock = new Block(currentBlock.shape, currentBlock.color);
        rotatedBlock.rotate();
        if (canMove(rotatedBlock, currentBlock.x, currentBlock.y)) {
            currentBlock.rotate(); // Apply the rotation if it's valid
        }
    }

    // Method to move the current block down
    public boolean moveDown() {
        if (canMove(currentBlock, currentBlock.x + 1, currentBlock.y)) {
            currentBlock.x++; // Move the block down
            return true; // Move successful
        } else {
            placeBlock(); // Place the block on the game board
            return false; // Cannot move down, block has landed
        }
    }

    // Method to place the current block on the game board
    private void placeBlock() {
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[0].length; j++) {
                if (currentBlock.shape[i][j] != 0) {
                    gameBoard[currentBlock.x + i][currentBlock.y + j] = 1; // Mark as filled
                }
            }
        }
        checkCompletedRows(); // Check for completed rows
        currentBlock = blockQueue.poll(); // Get the next block
        generateNewBlock(); // Generate a new block
    }

    // Method to check for completed rows and update the score
    private void checkCompletedRows() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            boolean isComplete = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (gameBoard[i][j] == 0) {
                    isComplete = false;
                    break; // Row is not complete
                }
            }
            if (isComplete) {
                removeRow(i); // Remove the completed row
                score += 100; // Increase the score
            }
        }
    }

    // Method to remove a completed row and shift rows down
    private void removeRow(int row) {
        for (int i = row; i > 0; i--) {
            gameBoard[i] = Arrays.copyOf(gameBoard[i - 1], BOARD_WIDTH); // Shift down
        }
        Arrays.fill(gameBoard[0], 0); // Clear the top row
    }

    // Method to check if the game is over
    public boolean isGameOver() {
        for (int j = 0; j < BOARD_WIDTH; j++) {
            if (gameBoard[0][j] != 0) {
                return true; // Game over if there's any filled block in the top row
            }
        }
        return false; // Game is still going
    }

    // Method to paint the game board and blocks
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the game board
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (gameBoard[i][j] != 0) {
                    g.setColor(Color.GRAY); // Set color for filled cells
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // Draw the current block
        if (currentBlock != null) {
            g.setColor(currentBlock.color); // Set color for the current block
            for (int i = 0; i < currentBlock.shape.length; i++) {
                for (int j = 0; j < currentBlock.shape[0].length; j++) {
                    if (currentBlock.shape[i][j] != 0) {
                        g.fillRect((currentBlock.y + j) * TILE_SIZE, (currentBlock.x + i) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        // Draw the score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
    }

    // Method to handle game updates
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver()) {
            if (!moveDown()) {
                if (isGameOver()) {
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + score);
                }
            }
            repaint(); // Repaint after each update
        }
    }

    // Method to handle key presses
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                moveLeft(); // Move left when left arrow key is pressed
                break;
            case KeyEvent.VK_RIGHT:
                moveRight(); // Move right when right arrow key is pressed
                break;
            case KeyEvent.VK_UP:
                rotateBlock(); // Rotate the block when up arrow key is pressed
                break;
            case KeyEvent.VK_DOWN:
                moveDown(); // Move down when down arrow key is pressed
                break;
        }
        repaint(); // Repaint after key action
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    // Main method to run the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Game");
        Question3b game = new Question3b();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); // Display the game window
    }
}
