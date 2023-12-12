import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    private static final int TILE_SIZE = 20;
    private static final int GRID_SIZE = 20;
    private static final int GAME_SPEED = 100;

    private Timer timer;
    private int[][] grid;
    private int snakeLength;
    private int[] snakeX, snakeY;
    private int foodX, foodY;
    private boolean isGameOver;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(TILE_SIZE * GRID_SIZE, TILE_SIZE * GRID_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        grid = new int[GRID_SIZE][GRID_SIZE];
        snakeX = new int[GRID_SIZE * GRID_SIZE];
        snakeY = new int[GRID_SIZE * GRID_SIZE];
        snakeLength = 1;

        isGameOver = false;

        timer = new Timer(GAME_SPEED, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);

        initializeGame();
    }

    private void initializeGame() {
        snakeLength = 1;
        snakeX[0] = GRID_SIZE / 2;
        snakeY[0] = GRID_SIZE / 2;

        spawnFood();
    }

    private void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(GRID_SIZE);
        foodY = random.nextInt(GRID_SIZE);

        // Make sure the food doesn't spawn on the snake
        while (grid[foodX][foodY] != 0) {
            foodX = random.nextInt(GRID_SIZE);
            foodY = random.nextInt(GRID_SIZE);
        }
    }

    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        if (grid[snakeX[0]][snakeY[0]] == 2) {
            isGameOver = true;
            return;
        }

        switch (grid[snakeX[0]][snakeY[0]]) {
            case 1: // Food
                snakeLength++;
                spawnFood();
                break;
        }

        grid[snakeX[0]][snakeY[0]] = 2;

        for (int i = 1; i < snakeLength; i++) {
            grid[snakeX[i]][snakeY[i]] = 1;
        }
    }

    private void checkCollision() {
        if (snakeX[0] < 0 || snakeX[0] >= GRID_SIZE || snakeY[0] < 0 || snakeY[0] >= GRID_SIZE) {
            isGameOver = true;
        }
    }

    private void checkGameOver() {
        if (isGameOver) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        checkCollision();
        checkGameOver();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                if (snakeLength <= 1 || snakeY[0] != snakeY[1] + 1) {
                    snakeX[0] = snakeX[0];
                    snakeY[0] = snakeY[0] - 1;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (snakeLength <= 1 || snakeY[0] != snakeY[1] - 1) {
                    snakeX[0] = snakeX[0];
                    snakeY[0] = snakeY[0] + 1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (snakeLength <= 1 || snakeX[0] != snakeX[1] + 1) {
                    snakeX[0] = snakeX[0] - 1;
                    snakeY[0] = snakeY[0];
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (snakeLength <= 1 || snakeX[0] != snakeX[1] - 1) {
                    snakeX[0] = snakeX[0] + 1;
                    snakeY[0] = snakeY[0];
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == 0) {
                    g.setColor(Color.WHITE);
                } else if (grid[i][j] == 1) {
                    g.setColor(Color.GREEN);
                } else if (grid[i][j] == 2) {
                    g.setColor(Color.RED);
                }
                g.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame snakeGame = new SnakeGame();
            snakeGame.setVisible(true);
        });
    }
}
