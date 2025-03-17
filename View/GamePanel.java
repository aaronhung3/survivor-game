package View;
import Model.Player;
import Model.Enemy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // Calculate the tile size for each tile on the screen
    final int originalTileSize = 16;
    final int tileScalar = 3;
    final int tileSize = originalTileSize * tileScalar; // Original size * scalar

    // Setup the screen width and screen height 
    final int SCREEN_WIDTH = 20 * tileSize; // 960px
    final int SCREEN_HEIGHT = 20 * tileSize; // 720px

    // Values of the player object
    final int playerScalar = 5;
    final int playerX = SCREEN_WIDTH / 2;
    final int playerY = SCREEN_HEIGHT / 2;
    final int playerSize = 5 * playerScalar; // 5 x 5 size

    // Values of the enemy object
    final int enemyScalar = 3;
    final int enemySize = 5 * enemyScalar;

    // List of enemies
    private final int MAX_ENEMIES = 10;
    private ArrayList<Enemy> enemies = new ArrayList<>();

    // Player Movement
    public boolean moveLeft;
    public boolean moveRight;
    public boolean moveUp;
    public boolean moveDown;
    public int movementSpeed = 10;

    // Enemy Movement
    public int enemySpeed = 5;


    // ALlows the game to run in multiple threads
    Thread gameThread;

    // Timer for incremental enemy spawning
    private ScheduledExecutorService spawnTimer; // Timer to help spawn enemies incrementally
    private static final int SPAWN_INTERVAL = 2000;

    // Initiate an instance of the player class
    Player player = new Player(playerX, playerY, playerSize);

    // Initiate an instance of the enemy class
    Enemy enemy = new Enemy(SCREEN_WIDTH, SCREEN_HEIGHT, enemySize);

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(this);

        startSpawningEnemies();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    ///////////////////////Spawning the enemies incrementally/////////////////////////
    
    private void startSpawningEnemies() {
        spawnTimer = Executors.newScheduledThreadPool(1);  // Single-threaded executor
        spawnTimer.scheduleAtFixedRate(this::spawnEnemy, 0, SPAWN_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void spawnEnemy() {
        if (enemies.size() < MAX_ENEMIES) {
            enemies.add(new Enemy(SCREEN_WIDTH, SCREEN_HEIGHT, enemySize)); // Create new enemy
        }
    }
     
    //////////////////////////////////////////////////////////////////////////////////

    // Handle all game logic
    @Override
    public void run() {
        // Trigger the repaint 
        while (gameThread != null) {
            if (moveLeft) {
                player.playerX -= movementSpeed;
            } else if (moveRight) {
                player.playerX += movementSpeed;
            }
            if (moveUp) {
                player.playerY -= movementSpeed;
            } else if (moveDown) {
                player.playerY += movementSpeed;
            }

            for (Enemy enemy : enemies) {
                enemy.attackPlayer(player, enemySpeed);
            }
            repaint();

            try {
                Thread.sleep(16); // Smooth movement (~60 FPS)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }   
    }

    // Only used for painting
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    }

    // Movement for the player
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight = true;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            moveUp = true;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            moveDown = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight = false;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            moveUp = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            moveDown = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
