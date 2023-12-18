package level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import utilz.LoadSave;

public class LevelManager {

	private Game game;
	private BufferedImage[] levelSprite;
	private Level levelOne;
	
	public LevelManager(Game game){
	this.game = game;

	importOutsideSprites();
	levelOne = new Level(LoadSave.GetLevelData());

	}
	
	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.SRC_MAP);
		levelSprite = new BufferedImage[204];
		for (int j = 0; j < 17; j++)
			for (int i = 0; i < 12; i++) {
				int index = j * 12 + i;
				levelSprite[index] = img.getSubimage(i * 48, j * 48, 48, 48);
			}
	}

	
	public void draw(Graphics g, int xLvlOffset) {
		for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
			for (int i = 0; i < levelOne.getLevelData()[0].length; i++) {
				int index = levelOne.getSpriteIndex(i, j);
//				System.out.println(index);
				if(index == -1) {
					index = 203;
				}
				g.drawImage(levelSprite[index], Game.TILES_SIZE * i - xLvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
	}
	
	public void update() {
		
	}
	
	public Level getCurrentLevel() {
		return levelOne;
	}

}
