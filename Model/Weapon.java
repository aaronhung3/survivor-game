package Model;

import java.awt.*;

public class Weapon {

    private Player player;

    private int weaponSize = 10;
    private int weaponX;
    private int weaponY;
    private int offsetX;
    private int offsetY;

    public Weapon(int offsetX, int offsetY, Player player) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.player = player;
    }

    public void updatePosition() {
        this.weaponX = player.getX() + offsetX;
        this.weaponY = player.getY() + offsetY;
    }

    public int getX() {
        return weaponX;
    }

    public int getY() {
        return weaponY;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(getX(), getY(), weaponSize, weaponSize);
    }

}
