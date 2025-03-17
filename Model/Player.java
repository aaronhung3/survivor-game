package Model;

import java.awt.*;

public class Player {

    public int playerX;
    public int playerY;
    public int playerSize;

    public Player(int playerX, int playerY, int playerSize) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerSize = playerSize;
    }

    public void moveLeft() {

    }

    public void moveRight() {

    }

    public void moveUp() {

    }

    public void moveDown() {

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
}