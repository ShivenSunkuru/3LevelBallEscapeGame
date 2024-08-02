import javax.swing.*;     // Import everything from swing package
import java.awt.*;		    // Import everything from awt package
import java.awt.event.*;  // for key listener
import java.io.*; 				// for file ops
import java.util.ArrayList;


public class GameStarter extends JPanel implements KeyListener, Runnable  // Implementing KeyListener here allows you to react to key presses
{
	private static final int frameWidth = 1200;  // Set Dimension of full frame
	private static final int frameHeight = 800;
	private static final String[] levels ={"level1.txt"};
	private boolean win;
	private String msg;
	private char[][] board;
  private ArrayList<Block> walls;
  private ArrayList<Paintable> paintedThings;
	private JFrame frame;
	private Font f;
	private Thread t;
	private boolean gameLoaded;

	public GameStarter()
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
		msg = "MESSAGE DISPLAYED AT TOP OF SCREEN";  // Initial message can be overwritten
		f=new Font("TIMES NEW ROMAN",Font.BOLD,24);  // Set Font  https://docs.oracle.com/javase/7/docs/api/java/awt/Font.html
		t=new Thread(this);  // The thread
		t.start();

		// Call method to load properties from file
		// NOTE: FOR NOW SOME BASIC PROPERTIES ARE ADDED HERE
		loadPropertiesFromFile(levels[0]);
	}

	/*********** ALL PAINTING INITIATED FROM THIS METHOD *************/
	public void paintComponent(Graphics g)

	{
		super.paintComponent(g);  // Need to call this first to initialize in parent class, do not change
		Graphics2D g2d = (Graphics2D)g;  // Cast to Graphics2D which is a subclass of Graphics with additional properties

		/*  Fill background */
		g2d.setColor(new Color(137, 247,124));  // Color on RGB scale
		g2d.fillRect(0,0,frameWidth,frameHeight);

		if (!gameLoaded){
			introScreen(g2d);
			return;
	  }

		g2d.setColor(Color.BLUE);
		g2d.setFont(f);  // NOTE: Font initialized in the constructor
		g2d.drawString(msg,100,30);  // Write text from msg String
		for (Paintable p : paintedThings)
			p.paint(g2d);
	}

	public void introScreen(Graphics2D g2d){
		g2d.setColor(Color.BLUE);
		g2d.setFont(new Font("TIMES NEW ROMAN",Font.BOLD,128));  // NOTE: Font initialized in the constructor
		g2d.drawString("LOADING...",200,300);

	}

	/******** THIS METHOD EXECUTES GAME LOGIC **************/
	public void run(){
		delay(2000); // Initial Delay to allow load of data
		repaint();
		while(!win)  // Keep going until win or window is closed
		{
			// Move things once you have moveable items

			// handle all intersections
			delay(5);  //play with this number frame refresh delay in ms
			repaint();
		}
		// Set Win Message
		msg = "********  YOU WIN   ********";
	}

	/*  Key Listener Methods Below
	https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyListener.html
	*/
	public void keyPressed(KeyEvent ke)
	{
		// Print key codes to know how to trigger events
		System.out.println(ke.getKeyCode());
	}

	public void keyReleased(KeyEvent ke){}

	public void keyTyped(KeyEvent ke){}

	public void loadPropertiesFromFile(String fileName){
			// Hardcoded for now.  Will adapt to load from file
			int size = 60;
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
			walls = new ArrayList<Block>();
			for(int r=0;r<board.length;r++){
				for(int c=0;c<board[0].length;c++){
					if (board[r][c]=='#')
							walls.add(new Block(c*size+size,r*size+size,size,size,Color.MAGENTA));
					if (board[r][c]=='@')
							walls.add(new Block(c*size+size,r*size+size,size,size,Color.RED));

					}
			}
			paintedThings = new ArrayList<Paintable>();

			paintedThings.addAll(walls);
			gameLoaded = true;
	}

	public void delay(int ms){
		try{
			t.sleep(ms);  // time in milliseconds (0.001 seconds) to delay
		} catch(InterruptedException e) {}

	}

	public static void main(String args[])
	{
		GameStarter app=new GameStarter();
	}
}