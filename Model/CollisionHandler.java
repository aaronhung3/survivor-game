package Model;

import java.awt.*;

public class CollisionHandler {
    
    public static boolean isColliding(Player player, Enemy enemy) {
        Rectangle playerPos = new Rectangle(player.getX(), player.getY(), player.getSize(), player.getSize());
        Rectangle enemyPos = new Rectangle(enemy.getX(), enemy.getY(), enemy.getSize(), enemy.getSize());
        
        return playerPos.intersects(enemyPos);
    }

    public static boolean bulletColliding(Bullet bullet, Enemy enemy) {
        Rectangle bulletPos = new Rectangle((int) bullet.getX(), (int) bullet.getY(), bullet.getSize(), bullet.getSize());
        Rectangle enemyPos = new Rectangle(enemy.getX(), enemy.getY(), enemy.getSize(), enemy.getSize());

        return bulletPos.intersects(enemyPos);
    }

}
