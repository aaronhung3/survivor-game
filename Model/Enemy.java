package Model;

import java.awt.*;
import java.util.*;

public class Enemy {

    public int enemyX;
    public int enemyY;
    public int enemySize;

    public Enemy(int enemyX, int enemyY, int enemySize) {
        this.enemySize = enemySize;
        spawnEnemy(enemyX, enemyY);
    }

    private void spawnEnemy(int screenWidth, int screenHeight) {
        System.out.println("Enemy created!");
        Random random = new Random();
        int border = random.nextInt(4) + 1; // Randomly choose a number between 1 - 4

        switch (border) {
            case 1:
                this.enemyX = random.nextInt(screenWidth);
                this.enemyY = 0;
                break;
            
            case 2:
                this.enemyX = random.nextInt(screenWidth);
                this.enemyY = screenHeight - enemySize;
                break;

            case 3:
                this.enemyX = 0;
                this.enemyY = random.nextInt(screenHeight);
                break;

            case 4:
                this.enemyX = screenWidth - enemySize;
                this.enemyY = random.nextInt(screenHeight);
                break;
        }
    }

    public void attackPlayer(Player player, int speed) {
        int distanceX = player.getX() - this.enemyX;
        int distanceY = player.getY() - this.enemyY;
        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

        if (distance > 0) {
            int moveX = (int) (distanceX / distance * speed);
            int moveY = (int) (distanceY / distance * speed);

            this.enemyX += moveX;
            this.enemyY += moveY;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(enemyX, enemyY, enemySize, enemySize);
    }

    public int getX() {
        return enemyX;
    }

    public int getY() {
        return enemyY;
    }

    public int getSize() {
        return enemySize;
    }
}
