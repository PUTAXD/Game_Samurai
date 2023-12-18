package ui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class PauseOverlay {
	private Playing playing;
	private PauseOverlayButton[] buttons = new PauseOverlayButton[3];
	private BufferedImage backgroundImg;
	private int menuX, menuY, menuWidth, menuHeight;

	public PauseOverlay(Playing playing) {
		this.playing = playing;
		loadButtons();
		loadBackground();
		// TODO Auto-generated constructor stub
	}

	private void loadBackground() {
		// TODO Auto-generated method stub
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_OVERLAY_BG);
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (45 * Game.SCALE);
	}

	private void loadButtons() {
		// TODO Auto-generated method stub
		buttons[0] = new PauseOverlayButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, Gamestate.PLAYING);
		buttons[1] = new PauseOverlayButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, Gamestate.PLAYING);
		buttons[2] = new PauseOverlayButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, Gamestate.QUIT);
	}

	public void update() {
		// TODO Auto-generated method stub
		for (PauseOverlayButton mb : buttons)
			mb.update();
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

		for (PauseOverlayButton mb : buttons)
			mb.draw(g);
	}


	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		for (PauseOverlayButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMousePressed(true);
				break;
			}
		}		
			
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		for (PauseOverlayButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed())
					mb.applyGamestate();
				break;
			}
		}

		 if (isIn(e, buttons[0])) {
				if (buttons[0].isMousePressed())
					playing.unpauseGame();
		 }
		 
		 if (isIn(e, buttons[1])) {
			 if(buttons[1].isMousePressed()) {
					playing.unpauseGame();
					playing.resetAll();
			 	}
		 }
		 
		 resetButtons();
	}
	private void resetButtons() {
		for (PauseOverlayButton mb : buttons)
			mb.resetBools();

	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		for (PauseOverlayButton mb : buttons)
			mb.setMouseOver(false);

		for (PauseOverlayButton mb : buttons)
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
	}

	private boolean isIn(MouseEvent e, PauseOverlayButton b) {
		return b.getBounds().contains(e.getX(), e.getY());

	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			Gamestate.state = Gamestate.PLAYING;

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
