package Model;

import java.awt.*;

import View.GamePanel;

public class Player {

    private GamePanel panel;

    public int playerX;
    public int playerY;
    public int playerSize;

    // Player movement
    public boolean moveLeft;
    public boolean moveRight;
    public boolean moveUp;
    public boolean moveDown;
    private int movementSpeed = 10;

    // Player stats
    public int playerLives;
    public boolean isInvincible = false;
    public long invincibleTime;

    public Player(int playerX, int playerY, int playerSize, int playerLives, GamePanel panel) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerSize = playerSize;
        this.playerLives = playerLives;
        this.panel = panel;
    }

    public void playerMove() {
        double normalizeX = 0;
        double normalizeY = 0;

        if (moveLeft) {
            normalizeX = -1;
        } else if (moveRight) {
            normalizeX = 1;
        }
        if (moveUp) {
            normalizeY = -1;
        } else if (moveDown) {
            normalizeY = 1;
        }

        if (normalizeX != 0 || normalizeY != 0) {
            double magnitude = Math.sqrt(normalizeX * normalizeX + normalizeY * normalizeY);
            normalizeX = (int) (normalizeX / magnitude * movementSpeed);
            normalizeY = (int) (normalizeY / magnitude * movementSpeed);
        }

        double nextX = playerX + normalizeX;
        double nextY = playerY + normalizeY;
        
        if (nextX >= 0 && nextX + playerSize <= panel.SCREEN_WIDTH) {
            playerX = (int) nextX;
        }
        if (nextY >= 0 && nextY + playerSize <= panel.SCREEN_HEIGHT) {
            playerY = (int) nextY;
        }
        
    }

    public void setMovement(boolean left, boolean right, boolean up, boolean down) {
        this.moveLeft = left;
        this.moveRight = right;
        this.moveUp = up;
        this.moveDown = down;
    }

    public void loseLife() {
        if (playerLives > 0) {
            playerLives--;
        }
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void triggerInvincible(int duration) {
        isInvincible = true;
        invincibleTime = System.currentTimeMillis() + duration;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(playerX, playerY, playerSize, playerSize);
    }

    public int getX() {
        return playerX;
    }

    public int getY() {
        return playerY;
    }

    public int getSize() {
        return playerSize;
    }

    public int getLives() {
        return playerLives;
    }

    public void setX(int x) {
        this.playerX = x;
    }

    public void setY(int y) {
        this.playerY = y;
    }

}