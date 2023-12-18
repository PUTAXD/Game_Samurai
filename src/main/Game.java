package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import entities.Player;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import level.LevelManager;

public class Game implements Runnable {
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	
	private Playing playing;
	private Menu menu;
	private AudioPlayer audioPlayer;
	
	public final static int TILES_DEFAULT_SIZE = 48;
	public final static float SCALE = 1.0f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	public Game() {
		initClasses();
		gamePanel  = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
		startGameLoop();
	}
	
	private void initClasses() {
		// TODO Auto-generated method stub
		menu = new Menu(this);
		playing = new Playing(this);
		audioPlayer = new AudioPlayer();
	}

	public void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	
	public void update () {
		switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case RESTART:
			playing.update();
			break;
		case QUIT:
			System.exit(0);
			break;
		default:
			break;
		}
	}
	
	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		default:
			break;
		}
	}
	

	
	
	@Override
	public void run() {
		double timePerFrame = 1000000000.0/FPS_SET;
		double timePerUpdate = 1000000000.0/UPS_SET;
		
		long previousTime = System.nanoTime();
		
		int frame = 0;
		int updates = 0;
		long lastcheck = System.currentTimeMillis();
		
		double deltaU = 0;
		double deltaF = 0;
		
		//fps counter
		while(true) {
			long currentTime = System.nanoTime();
			
			deltaU += (currentTime  - previousTime)/ timePerUpdate;
			deltaF += (currentTime -previousTime) / timePerFrame;
			previousTime = currentTime;
			
			if(deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}
			
			if(deltaF>=1) {
				gamePanel.repaint();
				frame++;
				deltaF--;
			}
			
//			if(now - lastFrame >= timePerFrame) {
//				gamePanel.repaint();
//				lastFrame = now;
//				frame++;
//			}
				
			if(System.currentTimeMillis() - lastcheck >= 1000) {
				lastcheck = System.currentTimeMillis();
				System.out.println("FPS : " + frame +"|| UPS : "+ updates);
				frame = 0;
				updates = 0;
			}
		}
	}
	
	public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING) 
			playing.getPlayer().resetDirBooleans();
	}
	public Menu getMenu() {
		return menu;
	}
	public Playing getPlaying() {
		return playing;
	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
}
