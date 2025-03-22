package Model;

import java.awt.*;

public class Bullet {
    
    private double currentX;
    private double currentY;
    private double targetX;
    private double targetY;
    private double velocityX;
    private double velocityY;
    final int bulletSize = 3;
    final int bulletSpeed = 10;

    public Bullet(double currentX, double currentY, double targetX, double targetY) {
        this.currentX = currentX;
        this.currentY = currentY;
        this.targetX = targetX;
        this.targetY = targetY;

        updateVelocity();
    }

    private void updateVelocity() {
        double dx = targetX - currentX;
        double dy = targetY - currentY;
        double mag = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        this.velocityX = (dx / mag) * bulletSpeed;
        this.velocityY = (dy / mag) * bulletSpeed;
    }

    public void moveBullet() {
        currentX += velocityX;
        currentY += velocityY;
    }

    public double getX() {
        return currentX;
    }

    public double getY() {
        return currentY;
    }

    public int getSize() {
        return bulletSize;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect((int) currentX, (int) currentY, bulletSize, bulletSize);
    }

}
