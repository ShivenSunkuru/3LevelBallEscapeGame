import java.awt.*;

public class PlayerBall extends Ball{

	private int score;
	private int cooldown;
	private double intX;
	private double intY;

 public PlayerBall(double x,double y, double s, Color c)
   {
		super(x,y,s,c);
		score = 0;
		cooldown = 0;
		intX = x;
		intY = y;

   }

  public PlayerBall(double x,double y, double s, String fileName)
   {
		super(x,y,s,fileName);
		score = 0;
		intX = x;
		intY = y;
   }


       public void setInitialPosition(double x, double y) {
           intX = x;
           intY = y;
    }

    public void respawn()
    {
	    setX(intX);
	    setY(intY);
}


	public int getScore()
	{
        return score;
	}

	public void addScore(int x)
	{
		score += x;
    }

    public void subtractScore(int x)
	{
			score -= x;
	}

   public void restartScore(int x)
    {
				score = x;
	}

	public void reduceCooldown()
	{
		if(cooldown > 0)
		cooldown--;
	}

	public boolean isCooldownA()
	{
		return cooldown > 0;
	}
	public void resetCooldown()
	{
		cooldown = 10;
	}


}

