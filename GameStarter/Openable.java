import java.awt.geom.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.*;

public class Openable extends Block
{
private boolean isOpen;

public Openable(double x,double y, double h, double w, String fileName){
	super(x,y,h,w,fileName);
	this.isOpen = false;
}

public boolean isOpen()
{
	return isOpen;
}

public void opened()
{
	isOpen = true;
	try {
		setImageAndFileName("OpenDoor.png");
	} catch (IOException ex)
	{
		System.out.println("Couldn't load");
	}
}

public void closed()
{
	isOpen = false;
	try {
			setImageAndFileName("BatDoor.png");
		} catch (IOException ex)
		{
			System.out.println("Couldn't load");
	}
}

public void setImageAndFileName(String fileName) throws IOException
{
	Image newImage = ImageIO.read(new File(fileName));
	setImage(newImage);
	setFileName(fileName);
}

}