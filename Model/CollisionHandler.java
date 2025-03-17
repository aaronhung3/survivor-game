package Model;

import java.awt.*;

public class CollisionHandler {
    
    public static boolean isColliding(Player player, Enemy enemy) {
        Rectangle playerPos = new Rectangle(player.getX(), player.getY(), player.getSize(), player.getSize());
        Rectangle enemyPos = new Rectangle(enemy.getX(), enemy.getY(), enemy.getSize(), enemy.getSize());
        
        return playerPos.intersects(enemyPos);
    }

}
