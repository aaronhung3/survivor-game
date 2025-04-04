package View;
import Model.Player;
import Model.CollisionHandler;
import Model.Enemy;
import Model.Bullet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    // Calculate the tile size for each tile on the screen
    final int originalTileSize = 16;
    final int tileScalar = 3;
    final int tileSize = originalTileSize * tileScalar; // Original size * scalar

    // Setup the screen width and screen height 
    public final int SCREEN_WIDTH = 20 * tileSize; // 960px
    public final int SCREEN_HEIGHT = 20 * tileSize; // 720px

    // Values of the player object
    final int playerScalar = 5;
    final int playerX = SCREEN_WIDTH / 2;
    final int playerY = SCREEN_HEIGHT / 2;
    final int playerSize = 5 * playerScalar; // 5 x 5 size
    public int playerLives = 5;

    // Values of the enemy object
    final int enemyScalar = 3;
    final int enemySize = 5 * enemyScalar;

    // List of enemies
    private final int MAX_ENEMIES = 10;
    private ArrayList<Enemy> enemies = new ArrayList<>();

    // Array list for bullets
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private Thread shootingThread;
    private boolean shooting;
    private int targetX, targetY;

    // Player Movement
    public boolean moveLeft;
    public boolean moveRight;
    public boolean moveUp;
    public boolean moveDown;

    // Enemy Movement
    public int enemySpeed = 2;

    // ALlows the game to run in multiple threads
    Thread gameThread;

    // Timer for incremental enemy spawning
    private ScheduledExecutorService spawnTimer; // Timer to help spawn enemies incrementally
    private static final int SPAWN_INTERVAL = 2000;

    // Initiate an instance of the player class
    Player player = new Player(playerX, playerY, playerSize, playerLives, this);

    // Initiate an instance of the enemy class
    Enemy enemy = new Enemy(SCREEN_WIDTH, SCREEN_HEIGHT, enemySize);

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

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
        final int targetFPS = 120;
        final long optimalTime = 1000 / targetFPS;

        // Trigger the repaint 
        while (gameThread != null) {
            long startTime = System.nanoTime();

            player.playerMove();

            for (Enemy enemy : enemies) {
                enemy.attackPlayer(player, enemySpeed);
                if (!player.isInvincible() && CollisionHandler.isColliding(player, enemy)) {
                    player.loseLife();
                    System.out.println("Player lost a life! " + player.getLives());
                    player.triggerInvincible(3000);
                    System.out.println("Player is Invincible!");
                }
            }

            if (player.isInvincible() && System.currentTimeMillis() > player.invincibleTime) {
                player.isInvincible = false;  // Reset invincibility after time is up
            }
            if (player.getLives() == 0) {
                gameThread = null;
            }

            for (int i = 0; i < bullets.size(); i++) {
                Bullet bullet = bullets.get(i);
                bullet.moveBullet();

                boolean bulletRemoved = false;

                if (bullet.getX() < 0 || bullet.getX() > SCREEN_WIDTH || bullet.getY() < 0 || bullet.getY() > SCREEN_HEIGHT || CollisionHandler.bulletColliding(bullet, enemy)) {
                    bulletRemoved = true;
                }

                for (Enemy bulletEnemy : enemies) {
                    if (CollisionHandler.bulletColliding(bullet, bulletEnemy)) {
                        bulletRemoved = true;
                        enemies.remove(bulletEnemy);
                        System.out.println("Bullet collided with enemy!");
                        break; 
                    }
                }

                if (bulletRemoved) {
                    bullets.remove(i);
                    System.out.println("Bullet " + i + " has been removed");
                    i--; // Adjust the index after removal to avoid skipping
                }
            }

            repaint();

            long elapsedTime = (System.nanoTime() - startTime) / 1_000_000; // Convert to milliseconds
            long sleepTime = optimalTime - elapsedTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);  // Sleep to maintain stable FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }   
    }

    // Only used for painting
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!player.isInvincible() || (System.currentTimeMillis() / 200) % 2 == 0) {
            player.draw(g); // Only draw the player every alternate frame   
        }
        
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        player.getWeapon().draw(g);

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    // Movement for the player
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_A) {
            moveLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            moveRight = true;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            moveUp = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            moveDown = true;
        }

        player.setMovement(moveLeft, moveRight, moveUp, moveDown);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_A) {
            moveLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            moveRight = false;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            moveUp = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            moveDown = false;
        }
        player.setMovement(moveLeft, moveRight, moveUp, moveDown);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        shooting = true;
        targetX = e.getX();
        targetY = e.getY();

        if (shootingThread == null || !shootingThread.isAlive()) {
            shootingThread = new Thread(() -> {
                while (shooting) {
                    bullets.add(new Bullet(player.getX(), player.getY(), targetX, targetY));
                    try {
                        Thread.sleep(200); // Adjust fire rate
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            shootingThread.start();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        shooting = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        shooting = true;
        targetX = e.getX();
        targetY = e.getY();

        if (shootingThread == null || !shootingThread.isAlive()) {
            shootingThread = new Thread(() -> {
                while (shooting) {
                    bullets.add(new Bullet(player.getX(), player.getY(), targetX, targetY));
                    try {
                        Thread.sleep(200); // Adjust fire rate
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            shootingThread.start();
        }
    }
}
