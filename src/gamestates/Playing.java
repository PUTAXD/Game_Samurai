package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.EnemyManager;
import entities.Player;
import level.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods{
	
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private boolean paused = false;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	
	//ui backkground
	private BufferedImage backgroundImg, bigBat;
	
	//level offset
	private int xLvlOffset;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
	private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
	private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
	
	private boolean gameOver;
	private boolean playerDying;
	private boolean lvlCompleted = false;
	
	private int currentEnemies;
	private int maxEnemies;
	
	public Playing (Game game) {
		super (game);
		initClasses();
		
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigBat = LoadSave.GetSpriteAtlas(LoadSave.BIG_BATS);
	}
	private void initClasses() {
		// TODO Auto-generated method stub
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
//		maxEnemies = enemyManager.getCountEnemies();
		player = new Player(200,100, (int) (128), (int) (128), this);
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		
	}

	@Override
	public void update() {
		
		
		if(paused) {
			pauseOverlay.update();
		}else if(lvlCompleted) {
			levelCompletedOverlay.update();
		}else if(gameOver) {
			gameOverOverlay.update();
		}else if(playerDying) {
			player.update();
		}
		else {
			checkSumEnemies();
			levelManager.update();
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(),player);
			checkCloseToBorder();
		}

	}
	
	public void checkSumEnemies() {
//		System.out.println("jumlah enemy : " + enemyManager.getCountEnemies() );
		if(enemyManager.getCountEnemies() == 0 ) {
			lvlCompleted = true;
			game.getAudioPlayer().lvlCompleted();
		}
			
	}
	

	private void checkCloseToBorder() {
		// TODO Auto-generated method stub
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;
	}
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		
//		drawBats(g);
		
		levelManager.draw(g,xLvlOffset);
		player.render(g,xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		if(paused) {
			g.setColor(new Color(0,0,0,100));
			g.fillRect(0, 0, Game.GAME_WIDTH, game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}else if(gameOver) {
			gameOverOverlay.draw(g);
		}else if (lvlCompleted) {
			levelCompletedOverlay.draw(g);
		}
	}

	
	private void drawBats(Graphics g) {
		g.drawImage(bigBat, 0, (int)(204 * Game.SCALE),BIG_BATS_WIDTH,BIG_BATS_HEIGHT, null);
	}
	
	
	public void resetAll() {
		gameOver = false;
		paused = false;
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
	}
	
	public void setGameOver(Boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!gameOver)
			if(e.getButton() == MouseEvent.BUTTON1) {
				player.setAttacking(true);
			}
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(gameOver)
			gameOverOverlay.keyPressed(e);
		else
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;
			case KeyEvent.VK_J:
				getPlayer().setAttacking(true);
				break;
			case KeyEvent.VK_BACK_SPACE:
				Gamestate.state = Gamestate.MENU;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(!gameOver)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			
			}

	}
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mousePressed(e);
			else if(lvlCompleted) {
				levelCompletedOverlay.mousePressed(e);	
			}
		} else
			gameOverOverlay.mousePressed(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseReleased(e);
			else if(lvlCompleted) {
				levelCompletedOverlay.mouseReleased(e);	
				lvlCompleted = false;
			}
		} else
			gameOverOverlay.mouseReleased(e);


	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseMoved(e);
			else if(lvlCompleted) {
				levelCompletedOverlay.mouseMoved(e);	
			}
		} else
			gameOverOverlay.mouseMoved(e);

	}

	public void unpauseGame() {
		paused = false;
	}
	
	public void windowFocusLost() {
		player.resetDirBooleans();
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayerDying(boolean playerDying) {
		// TODO Auto-generated method stub
		this.playerDying = playerDying;
	}
}
