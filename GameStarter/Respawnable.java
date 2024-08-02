import java.awt.*;
import java.awt.geom.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.*;

public class Respawnable extends Block

{
	 private double respawnX;
    private double respawnY;

    public Respawnable(double x, double y, double h, double w, String fileName, double resX, double resY)
    {
        super(x, y, h, w, fileName);
        respawnX = resX;
        respawnY = resY;
    }

    public void respawn()
    {
		setX(respawnX);
		setY(respawnY);
	}
}