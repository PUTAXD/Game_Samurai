package main;

import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static utilz.Constants.Direction.*;
import static utilz.Constants.PlayerConstant.*;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {
	
	private MouseInputs mouseInputs;

	
	private Game game;
	
	public GamePanel(Game game) {
		this.game = game;

		
		mouseInputs = new MouseInputs(this);
		addKeyListener(new KeyboardInputs(this));
		setPanelSize();
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}


	private void setPanelSize() {
		// TODO Auto-generated method stub
		Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
		System.out.println("GAME WIDTH : "+ GAME_WIDTH + ", GAME HEIGHT : "+ GAME_HEIGHT);
//		setMinimumSize(size);
		setPreferredSize(size);
//		setMaximumSize(size);
	}


	
	public void updateGame() {

	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}
	public Game getGame() {
		return game;
	}
	

}
	