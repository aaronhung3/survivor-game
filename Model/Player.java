package Model;
import java.awt.*;

public class Player {

    public int xPos;
    public int yPos;
    public int size;

    public Player(int xPos, int yPos, int size) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(xPos, yPos, size, size);
    }
}