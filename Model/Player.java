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

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(playerX, playerY, playerSize, playerSize);
    }
}