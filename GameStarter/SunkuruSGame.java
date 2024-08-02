import javax.swing.*;     // Import everything from swing package
import java.awt.*;		    // Import everything from awt package
import java.awt.event.*;  // for key listener
import java.io.*; 	 			// for file ops
import java.awt.geom.*;
import java.util.ArrayList;

public class SunkuruSGame extends JPanel implements KeyListener, Runnable  // Implementing KeyListener here allows you to react to key presses
{
	private static final int frameWidth = 1200;  // Set Dimension of full frame
	private static final int frameHeight = 800;
	private static final String[] levels ={"level1.txt", "level2.txt", "level3.txt"};
	private int currentLevel;
	private boolean win;
	private String msg;
	private char[][] board;
	private int flagX,flagY, size;
    private ArrayList<Block> walls;
    private ArrayList<Respawnable> Lava;
    private Openable BatDoor;
    private Block flag;
    private ArrayList<Paintable> paintedThings;
    private PlayerBall player;
    private Ball npc; //non-player that will bounce around the screen
    private Ball npctwo;
    private Ball npcthree;
    private int npcRespawnX, npcRespawnY, npctwoRespawnX, npctwoRespawnY, npcthreeRespawnX, npcthreeRespawnY;
    private int playerPrevX, playerPrevY;
	private JFrame frame;
	private Font f;
	private Thread t;
	private boolean gameLoaded, introRead;
	private long startTime;
	private long lastCollisionT;
	private long lastCollisionTime;
	private boolean collisionOccurred;
	private static final long COOLDOWN = 200;
	public int numRows , numCols, dx, dy, scoreReq;
	public Graphics2D g2d;

	public SunkuruSGame()
	{
		// Load and configure Java Graphics
		frame=new JFrame();
		frame.add(this);  //Add Object to Frame, this will invoke the paintComponent method
		frame.setSize(frameWidth,frameHeight);
		frame.setVisible(true);
		frame.addKeyListener(this); /***********  NEEDED TO USE KEYLISTENER ******/
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set some more basic game parameters
		win = false;
		gameLoaded = false;
		introRead = false;
		msg = "Level #1";  // Initial message can be overwritten
		f=new Font("TIMES NEW ROMAN",Font.BOLD,24);  // Set Font  https://docs.oracle.com/javase/7/docs/api/java/awt/Font.html
		t=new Thread(this);  // The thread
		t.start();

		// Call method to load properties from file
		// NOTE: FOR NOW SOME BASIC PROPERTIES ARE ADDED HERE
		currentLevel = 0;
		loadPropertiesFromFile(levels[currentLevel]);
	}

	/*********** ALL PAINTING INITIATED FROM THIS METHOD *************/
	public void paintComponent(Graphics g)

	{
		super.paintComponent(g);  // Need to call this first to initialize in parent class, do not change
	    g2d = (Graphics2D)g;  // Cast to Graphics2D which is a subclass of Graphics with additional properties

		/*  Fill background */
		g2d.setColor(new Color(137, 0 ,124));  // Color on RGB scale
		g2d.fillRect(0,0,frameWidth,frameHeight);

	//intro stuff
		if (!gameLoaded || !introRead){
			System.out.println("NOT LOADED");
			introScreen(g2d);
			return;
}
			if(win)
		    displayEndScreen(g2d);

		g2d.setColor(Color.WHITE);
		g2d.setFont(f);  // NOTE: Font initialized in the constructor
		g2d.drawString(msg,100,30);  // Write text from msg String
		for (Paintable p : paintedThings)
			p.paint(g2d);

			/*if(player.getScore() >= 10)
				flag = newBlock(flagX, flagY, size, size, "redflag.png");
				flag.paint(g2d);
				*/
}

	public void introScreen(Graphics2D g2d){
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("TIMES NEW ROMAN",Font.BOLD,48));  // NOTE: Font initialized in the constructor
		g2d.drawString("Welcome To the Sunkuru Ball Game!",80,100);
		g2d.setFont(new Font("TIMES NEW ROMAN",Font.BOLD,36));
		g2d.setColor(Color.WHITE);
	g2d.fillRect(80,120,1040,370);
		g2d.setColor(new Color(84,37,43));
		g2d.drawString("You are The White Ball with the Black center",100,160);
		g2d.drawString("Score is based on how many times you touch the other npc balls",100,205);
		g2d.drawString("One ball adds, one removes, (a point) and one restarts score to 0",100,250);
		g2d.drawString("Objective is to Capture The Red Flag", 100, 295);
		g2d.drawString("You must get a score of 3 and get the flag to move on",100,340);
		g2d.drawString("You may skip levels by pressing n but don't do that until you win!",100,385);
		g2d.drawString("Use the Arrow Keys", 100,430);
		g2d.drawString("Press Enter to Begin",100,475);


	}

	public void displayEndScreen(Graphics2D g2d)
	{
			    g2d.setColor(Color.BLACK);
		    g2d.fillRect(0, 0, frameWidth, frameHeight);
		   	 g2d.setColor(Color.WHITE);
		    g2d.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 36));
		 	   g2d.drawString("Good Job! You Won!", 355, 150);

	}




	/******** THIS METHOD EXECUTES GAME LOGIC **************/
	public void run(){
		delay(1500); // Initial Delay to allow load of data
		while(!introRead)
		{
			delay(100);
        }
		repaint();
		startTime = System.currentTimeMillis();
		lastCollisionT = System.currentTimeMillis();
		while(!win)  // Keep going until win or window is closed
		{
			// Move things once you have moveable items

						player.move();
			            String touching = touchingWall(player);
			            if (touching.length()>0){
			                //If a wall is hit, back up a bit and stop
			                if (touching.contains("L") || touching.contains("R")){
			                    player.setDX(-player.getDX());
			                    player.move();
			                }
			                else if (touching.contains("B") || touching.contains("T")){
			                    player.setDY(-player.getDY());
			                    player.move();
			                }
			                else{
			                    player.setDX(-player.getDX());
			                    player.setDY(-player.getDY());
			                    player.move();
			                }
			                player.setDX(0);
			                player.setDY(0);
            }

			 touching = touchingWall(npc);
			if (touching.length()>0){
				if (touching.contains("L") || touching.contains("R"))
					npc.setDX(-npc.getDX());
				else if (touching.contains("B") || touching.contains("T"))
					npc.setDY(-npc.getDY());
				else {
					npc.setDX(-npc.getDX());
					npc.setDY(-npc.getDY());
				}
				npc.move();
			}

			npc.move();

			String touchingtwo = touchingWall(npctwo);
			if (touchingtwo.length()>0){
				if (touchingtwo.contains("L") || touchingtwo.contains("R"))
					npctwo.setDX(-npctwo.getDX());
				else if (touchingtwo.contains("B") || touchingtwo.contains("T"))
					npctwo.setDY(-npctwo.getDY());
				else {
					npctwo.setDX(-npctwo.getDX());
					npctwo.setDY(-npctwo.getDY());
				}
				npctwo.move();
			}

			npctwo.move();

			String touchingthree = touchingWall(npcthree);
			if (touchingthree.length()>0){
				if (touchingthree.contains("L") || touchingthree.contains("R"))
					npcthree.setDX(-npcthree.getDX());
				else if (touchingthree.contains("B") || touchingthree.contains("T"))
					npcthree.setDY(-npcthree.getDY());
				else {
					npcthree.setDX(-npcthree.getDX());
					npcthree.setDY(-npcthree.getDY());
				}
				npcthree.move();
			}

			npcthree.move();

				handleCollisionsWithWallsAndLava(npc);
				  handleCollisionsWithWallsAndLava(npctwo);
				   handleCollisionsWithWallsAndLava(npcthree);

	  if (touchingLava(player))
	  {
	            player.respawn();
	        }

			//player.move() //move here keeps going
			// handle all intersections

			long currentT = System.currentTimeMillis();

			if(currentT - lastCollisionT > 1) {
  				  if (player.intersects(npc.getBounds2D())) {
        			if (npc.getX() != npcRespawnX || npc.getY() != npcRespawnY) {
            			if (!collisionOccurred && currentT - lastCollisionTime >= COOLDOWN) {
              		  if (playerPrevX != player.getX() || playerPrevY != player.getY()) {
                 		   player.addScore(1);
                  		 npc.setX(npcRespawnX);
                   			 npc.setY(npcRespawnY);
                   			 npc.setDX(Math.random() * 4 - 2);
                   			npc.setDY(Math.random() * 4 - 2);
                   	 lastCollisionTime = currentT;
                    		collisionOccurred = true;
              			  }
           				 }
        				}
    			}
    			else
    			{
        collisionOccurred = false;
   				 }
			}

						if(currentT - lastCollisionT > 1) {
			  				  if (player.intersects(npctwo.getBounds2D())) {
			        			if (npctwo.getX() != npctwoRespawnX || npctwo.getY() != npctwoRespawnY) {
			            			if (!collisionOccurred && currentT - lastCollisionTime >= COOLDOWN) {
			              		  if (playerPrevX != player.getX() || playerPrevY != player.getY()) {
			                 		   player.restartScore(0);
			                  		 npctwo.setX(npctwoRespawnX);
			                   			 npctwo.setY(npctwoRespawnY);
			                   			 npctwo.setDX(Math.random() * 4 - 2);
			                   			npctwo.setDY(Math.random() * 4 - 2);
			                   	 lastCollisionTime = currentT;
			                    		collisionOccurred = true;
			              			  }
			           				 }
			        				}
			    			}
			    			else
			    			{
			        collisionOccurred = false;
			   				 }
			}

						if(currentT - lastCollisionT > 1) {
			  				  if (player.intersects(npcthree.getBounds2D())) {
			        			if (npcthree.getX() != npcthreeRespawnX || npcthree.getY() != npcthreeRespawnY) {
			            			if (!collisionOccurred && currentT - lastCollisionTime >= COOLDOWN) {
			              		  if (playerPrevX != player.getX() || playerPrevY != player.getY()) {
			                 		   player.subtractScore(1);
			                  		 npcthree.setX(npcthreeRespawnX);
			                   			 npcthree.setY(npcthreeRespawnY);
			                   			 npcthree.setDX(Math.random() * 4 - 2);
			                   			npcthree.setDY(Math.random() * 4 - 2);
			                   	 lastCollisionTime = currentT;
			                    		collisionOccurred = true;
			              			  }
			           				 }
			        				}
			    			}
			    			else
			    			{
			        collisionOccurred = false;
			   				 }
			}

			  if (player.getScore() >= scoreReq && !BatDoor.isOpen())
			  {
			            BatDoor.opened();
			            paintedThings.add(BatDoor);
			        }
			        else
			        if (player.getScore() < scoreReq && BatDoor.isOpen())
			        {
			            BatDoor.closed();
			            paintedThings.add(BatDoor);
                   }

			msg = "Level #"+(currentLevel+1)+"   Time = "+getTime()+"   Score = "+player.getScore();

			if(player.getScore() >= scoreReq /*10*/ && player.intersects(flag.getBounds2D()))
			{
				NextLevel();
			}

			delay(5);  //play with this number frame refresh delay in ms
			repaint();

		}
		// Set Win Message
		//msg = "********  YOU WIN   ********";

	}

	private void NextLevel()
	{
		currentLevel++;
		if(currentLevel >= levels.length)
		{
			win = true;
		}
		else
		{
			loadPropertiesFromFile(levels[currentLevel]);
			msg = "Level #"+(currentLevel+1)+"   Time = "+getTime()+"   Score = "+player.getScore();
			player.restartScore(0);
		}
	}




public void checkdoor()
{
    if (player.getScore() >= scoreReq)
    {
        BatDoor.opened();
    }
	}

	/*  Key Listener Methods Below
	https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyListener.html
	*/

	private String direction(Ball ball, Block w){
	     String s = "";
	     Point2D ballTop = new Point2D.Double(ball.getCenterX(),ball.getY());
	     Point2D ballRight = new Point2D.Double(ball.getMaxX(),ball.getCenterY());
	     Point2D ballBottom = new Point2D.Double(ball.getCenterX(),ball.getMaxY());
	     Point2D ballLeft = new Point2D.Double(ball.getX(),ball.getCenterY());

		if (w.contains(ballTop))   	// Ball on Bottom of Block
			s+="B";
		if (w.contains(ballRight))	// Ball Left of Block
			s+="L";
		if (w.contains(ballBottom)) // Ball on Top of Block
			s+="T";
		if (w.contains(ballLeft))		// Ball on Right of Block
			s+="R";

		return s;
	}

	public String touchingWall(Ball b){
		String s = "";

		for (Block w : walls){
			if (b.intersects(w))
				s += "I->"+direction(b,w);
		}

            if(b.intersects(BatDoor))

		{
			if(!BatDoor.isOpen())
			s += "I->"+direction(b, BatDoor);
		}

		return s;
	}

	public boolean touchingLava(Ball b)
	{
	    for (Respawnable l : Lava)
	    {
	        if (b.intersects(l))
	        {
	            return true;
	        }
	  	  }
	    return false;
	}







				private void handleCollisionsWithWallsAndLava(Ball b)
				{
				    String touching = touchingWall(b);
				    if (touching.length() > 0)
				    {

				        if (touching.contains("L") || touching.contains("R"))

				        {
				            b.setDX(-b.getDX());
				        }
				        else if (touching.contains("B") || touching.contains("T"))
				        {
				            b.setDY(-b.getDY());
				        } else
				        {
				            b.setDX(-b.getDX());
				            b.setDY(-b.getDY());
				        }
				        b.move();
				    }
				    if (touchingLava(b))
				    {

				        if (b.getDX() != 0)
				        {
				            b.setDX(-b.getDX());
				        }
				        if (b.getDY() != 0)
				        {
				            b.setDY(-b.getDY());
				        }
				        b.move();
				    }
}













	public double getTime() // Time elapse in 10th of a second
	{

		long elapsed = System.currentTimeMillis()-startTime;

		double d = (long)(elapsed/100.0)/10.0;
		return d;

    }

	public void keyPressed(KeyEvent ke)
	{
		// Print key codes to know how to trigger events
		System.out.println(ke.getKeyCode());

playerPrevX = (int) player.getX();
playerPrevY = (int) player.getY();

		/*
		if(ke.getKeyCode() == 32)
		{
			currentLevel++;
			if(currentLevel >= levels.length)
				currentLevel = 0;
			loadPropertiesFromFile(levels[currentLevel]);
			msg = "Level #"+(currentLevel+1);
			delay(500);
	    }
        */

        if(ke.getKeyCode() == 10)
        {
			introRead = true;
	    }

	    if(ke.getKeyCode() == 78 && introRead && currentLevel < 3) //skip levels by clicking n!
	    {
			NextLevel();
     	}

		player.setDX(0);
		player.setDY(0);

		if(ke.getKeyCode() == 37) //left arrow
		{
			if(!touchingWall(player).contains("R"))
			{
			//player.setDX(-5);
			if(dx > 0)
			player.setDX(dx * -1);
			if(dx < 0)
			player.setDX(dx);
			}
		else
			player.setX(player.getX()+5);
		}
		if(ke.getKeyCode() == 39) //right arrow
		{
			if(!touchingWall(player).contains("L"))
			{
			//player.setDX(5);
			if(dx < 0)
			player.setDX(dx * -1);
			if(dx > 0)
			player.setDX(dx);
		    }
		else
			player.setX(player.getX()-5);
		}
		if(ke.getKeyCode() == 38) //up arrow
		{
			if(!touchingWall(player).contains("B"))
			{
			//player.setDY(-5);
			if(dy < 0)
			  player.setDY(dy);
			if(dy > 0)
			  player.setDY(dy * -1);
		    }
		else
			player.setY(player.getY()+5);
		}
		if(ke.getKeyCode() == 40) //down arrow
		{
			if(!touchingWall(player).contains("T"))
			{
			//player.setDY(5);
			if(dy < 0)
			player.setDY(dy * -1);
			if(dy > 0)
			player.setDY(dy);
		    }
		else
			player.setY(player.getY()-5);
		}

		//player.move(); //move while key is pressed

	}

	public void keyReleased(KeyEvent ke){

				player.setDX(0);
				player.setDY(0);
		}


	public void keyTyped(KeyEvent ke){}

	public void loadPropertiesFromFile(String fileName){

			// Hardcoded for now.  Will adapt to load from file
			char[][] temp = {{'@','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
											 {'#',' ',' ',' ',' ',' ',' ','#',' ','#',' ','#',' ','#','#','#'},
											 {'#',' ',' ',' ',' ','@',' ','#',' ','#',' ','#',' ','#','#','#'},
											 {'#',' ','#',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#','#'},
											 {'#',' ','#','#','#',' ',' ',' ','#',' ',' ','#',' ','#','#','#'},
											 {'#',' ',' ',' ','#',' ',' ',' ',' ','#',' ','#',' ','#','@','#'},
											 {'#',' ','@',' ','#','#','#','#',' ','#',' ',' ',' ',' ',' ','#'},
											 {'#',' ',' ',' ',' ',' ',' ','#',' ','#',' ',' ','#','#','#','#'},
											 {'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}};

		  board = temp;
		  int size=60;
		  numRows=4;
		  numCols=6;
		  dx = 0;
		  dy = 0;
		  scoreReq = 0;

				try
					{   //This object does the reading
						BufferedReader input = new BufferedReader(new FileReader(fileName)); //create file reader

			      String[] propertyNames = {"size","numRows","numCols","dx", "dy", "scoreReq"}; // you need to add to this
			      String[] valuesAsStrings = new String[propertyNames.length]; //hold values assosciated with fields
			      for (int i = 0; i < propertyNames.length; i++){
			  			String line = input.readLine(); // get a single line
			        String[] tokens = line.split("="); // tokens[0] == prop. name, tokens[1] == prop. value as String
			        if (tokens[0].trim().equals(propertyNames[i])) // Note trim is used to remove unwanted spaces
			            valuesAsStrings[i] = tokens[1].trim();
			        else
         	 System.out.println("Property Name "+propertyNames[i]+" not found");
					}

				  size = Integer.parseInt(valuesAsStrings[0]);
				  numRows = Integer.parseInt(valuesAsStrings[1]);  //convert to int to store field
				  numCols = Integer.parseInt(valuesAsStrings[2]);
				  dx = Integer.parseInt(valuesAsStrings[3]);
				  dy = Integer.parseInt(valuesAsStrings[4]);
				  scoreReq = Integer.parseInt(valuesAsStrings[5]);

					//Once you have the properties. Read in gameboard one line at a time
					//line = input.readLine();
					//read the line, and put characters into char array board
					//a helpful method is line.charAt(col);

					board = new char[numRows][numCols];
					for(int r = 0; r<numRows; r++){
						String line = input.readLine();
						System.out.println(line+"  --> "+line.length()+","+numCols);
						for(int c = 0; c < numCols; c++)
							board[r][c] = line.charAt(c);
					}

					}
					catch (IOException io) // catch errors
							{
								System.err.println("Exception =>"+io.getMessage());
		                    }

			walls = new ArrayList<Block>();
			Lava = new ArrayList<Respawnable>();
			for(int r=0;r<board.length;r++){
				for(int c=0;c<board[0].length;c++){
					if (board[r][c]=='#')
							walls.add(new Block(c*size+size,r*size+size,size,size, "rose.jfif" /*"name.png"*/));

					if (board[r][c]=='@')
							Lava.add(new Respawnable(c*size+size, r*size+size, size, size, "lava.jpg", (double)size, (double)size));

					if(board[r][c]=='*')
						BatDoor = new Openable(c*size+size,r*size+size,size,size,"BatDoor.png");

					if(board[r][c]=='F') {


					       flag = new Block(c*size+size,r*size+size,size,size,"redflag.png");
					       /*
					       flagX = c * size +size;
					       flagY = c * size+size;
					       */

						}
					if (board[r][c]=='J')
					{
						/*
						int npcSize = size/2;
						npcRespawnX = (int)(Math.random() * (frameWidth - size)) + npcSize;
						npcRespawnY = (int)(Math.random() * (frameHeight - size)) + npcSize;
						*/
						npcRespawnX = c * size + size;
						npcRespawnY = r * size + size;
						npc = new Ball(c*size+size,r*size+size,size/2,"JohnAlt.png"); //npc ball
					}
					if (board[r][c]=='S') {
						/*
							int npctwoSize = (int)(size/2.3);
							npctwoRespawnX = (int)(Math.random() * (frameWidth - size)) + npctwoSize;
							npctwoRespawnY = (int)(Math.random() * (frameHeight - size)) + npctwoSize;
						*/
							npctwoRespawnX = c * size + size;
							npctwoRespawnY = r * size + size;
							npctwo = new Ball(c*size+size,r*size+size,size/2.3,"SpawnAlt.png"); //npc ball
						}
					if (board[r][c]=='E'){
						/*
							int npcthreeSize = (int)(size/2.4);
							npcthreeRespawnX = (int)(Math.random() * (frameWidth - size)) + npcthreeSize;
							npcthreeRespawnY = (int)(Math.random() * (frameHeight - size)) + npcthreeSize;
						*/
							npcthreeRespawnX = c * size + size;
							npcthreeRespawnY = r * size + size;
							npcthree = new Ball(c*size+size,r*size+size,size/2.4,"Wolf.png"); //npc ball
						}

					if(board[r][c]=='P')
						    player = new PlayerBall(c*size+size,r*size+size,size,"skeleballp.png");
					}
			}

			npc.setDX(2);
			npc.setDY(3);

			npctwo.setDX(2);
			npctwo.setDY(3);

			npcthree.setDX(2);
			npcthree.setDY(3);

			paintedThings = new ArrayList<Paintable>();
			paintedThings.addAll(Lava);
			paintedThings.addAll(walls); //<-- All Walls will be painted

			paintedThings.add(BatDoor);

			paintedThings.add(player); //player wil get painted if added here
			paintedThings.add(npc); //npc wil get painted if added here
			paintedThings.add(npctwo);
			paintedThings.add(npcthree);
			paintedThings.add(flag);
			gameLoaded = true;
	}


	public void delay(int ms){

		try{
			t.sleep(ms);  // time in milliseconds (0.001 seconds) to delay
		} catch(InterruptedException e) {}

	}

	public static void main(String args[])
	{
		SunkuruSGame app=new SunkuruSGame();
	}
}