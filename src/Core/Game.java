package Core;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game implements Runnable
{

	int startingColorRange = 256;
	int colourChangeSpeed = 20;
// Speed (higher = faster)
	int speed = 20;
	boolean lockR = false;
	boolean lockB = true;
	boolean lockG = true;
	boolean randomUpdate = true;
	
	int sequentialUpdate = 5;
	
	int R = (int)(Math.random( ) * startingColorRange);
	int B = (int)(Math.random( ) * startingColorRange);
	int G = (int)(Math.random( ) * startingColorRange);
	

	
	// Game Thread Stuff
	Thread thread = new Thread(this);
	boolean running = false;
	
	// Window Stuff
	JFrame frame;
	public static String windowTitle = "T-Square Fractal";
	public static int width = 640;
	public static int height = width;
	public static Dimension gameDim = new Dimension(width, height); 
	
	// Square Stuff
	int squareColor = 5000;
	int squareSize = width / 2 - 10;
	
	// Rendering Stuff
	Canvas canvas;
	BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
	public void run()
	{
		while( running )
		{
			update();
			render();
			try { 
				Thread.sleep(1000/speed);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			
		}
		
	}
	
	public synchronized void start()
	{
		running = true;
		thread.start();
	}
	
	public synchronized void stop()
	{
		try{
			running = false;
			thread.join();
			System.exit( 0 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public Game()
	{
		init();
	}
	
	private void init()
	{
		
		// Create Frame
		frame = new JFrame(windowTitle);
		frame.setVisible(true);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create Canvas
		canvas = new Canvas();
		canvas.setSize(gameDim);
		canvas.setPreferredSize(gameDim);
		canvas.setMaximumSize(gameDim);
		canvas.setMinimumSize(gameDim);
		frame.add(canvas, BorderLayout.CENTER);
		
	}
	
	private void update()
	{

	}

	private Color updateColor()
	{
		
		if(randomUpdate == true)
		{
			
			if(lockR == false)
			{
				if (R > (256 - colourChangeSpeed))
					R = (int)(Math.random( )*colourChangeSpeed);
				else
					R += (int)(Math.random( )*colourChangeSpeed);
			}
			
			if(lockG == false)
			{
				if (G > (256 - colourChangeSpeed))
					G = (int)(Math.random( )*colourChangeSpeed);
				else
					G += (int)(Math.random( )*colourChangeSpeed);
			}
			
			if(lockB == false)
			{
				if (B > (256 - colourChangeSpeed))
					B = (int)(Math.random( )*colourChangeSpeed);
				else
					B += (int)(Math.random( )*colourChangeSpeed);
			}
		}
		else
		{
			if(lockR == false)
			{
				if(R >= (256 - sequentialUpdate))
					R = 0;
				else
					R += sequentialUpdate;
			}
			
			if(lockG == false)
			{
				if(G >= (256 - sequentialUpdate))
					G = 0;
				else
					G += sequentialUpdate;
			}
			
			if(lockB == false)
			{
				if(B >= (256 - sequentialUpdate))
					B = 0;
				else
					B += sequentialUpdate;
			}
			
		}
	//	int G = 121;
	//	int B = 250;
		Color newColor = new Color(R, G, B);
		return newColor;
	}
	
	private void render()
	{
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null)
		{
			canvas.createBufferStrategy( 3 );
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, null);
		
		drawSquare(height/2 - squareSize/2, width/2-squareSize/2, squareSize, updateColor(), g);
		
		bs.show();
		g.dispose();
	}
	
	private void drawSquare(int x, int y, int w, Color squareColor, Graphics g)
	{
		int orderRandomizer = (int)Math.ceil((Math.random ( ) * 4));
		
		if(w>0)
		{
			if(orderRandomizer == 1)
			// Draw top left square
			{
				drawSquare(x-w/4, y-w/4, w/2, updateColor(), g);
				drawSquare(x+ 3*w/4, y-w/4, w/2, updateColor(), g);
				drawSquare(x-w/4, y+3*w/4, w/2, updateColor(), g);
				drawSquare(x+3*w/4, y+3*w/4, w/2, updateColor(), g);
			}
			else if(orderRandomizer == 2)
			{
				// Draw top right square
				drawSquare(x+ 3*w/4, y-w/4, w/2, updateColor(), g);
				drawSquare(x-w/4, y+3*w/4, w/2, updateColor(), g);
				drawSquare(x+3*w/4, y+3*w/4, w/2, updateColor(), g);
				drawSquare(x-w/4, y-w/4, w/2, updateColor(), g);	
			
			}
				
			else if(orderRandomizer == 3)
			{
				// Draw bottom left square
				drawSquare(x-w/4, y+3*w/4, w/2, updateColor(), g);
				drawSquare(x+3*w/4, y+3*w/4, w/2, updateColor(), g);
				drawSquare(x-w/4, y-w/4, w/2, updateColor(), g);
				drawSquare(x+ 3*w/4, y-w/4, w/2, updateColor(), g);

			}
			else
			{
				// Draw bottom right square
				drawSquare(x+3*w/4, y+3*w/4, w/2, updateColor(), g);
				drawSquare(x-w/4, y-w/4, w/2, updateColor(), g);
				drawSquare(x+ 3*w/4, y-w/4, w/2, updateColor(), g);
				drawSquare(x-w/4, y+3*w/4, w/2, updateColor(), g);			
				
			}
		}
		g.setColor(updateColor());
		g.fillRect(x, y, w, w);
	}
	

}
