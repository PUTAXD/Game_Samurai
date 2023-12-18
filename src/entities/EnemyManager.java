package entities;

import static utilz.Constants.PlayerConstant.GetSpriteAmount;
import static utilz.Constants.EnemyConstant.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import gamestates.Playing;
import utilz.LoadSave;

public class EnemyManager {
	private Playing playing;
	private BufferedImage[][] greenSlimeArr;
	private ArrayList<GreenSlime> greenSlime = new ArrayList<>();
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
		addEnemies();

	}
	
	private void addEnemies() {
		// TODO Auto-generated method stub
		greenSlime = LoadSave.GetGreenSlime();
		System.out.println("Size of Slime : " +  greenSlime.size());
	}

	public void update(int[][] lvlData, Player player) {
		for(GreenSlime gs :  greenSlime) {
			if(gs.isActive())
			gs.update(lvlData,player);
		}
	}
	public void draw(Graphics g, int xLvlOffset) {
		
		drawGreenSlime(g, xLvlOffset);
	}

	private void drawGreenSlime(Graphics g, int xLvlOffset) {
		// TODO Auto-generated method stub
		for(GreenSlime gs :  greenSlime) {
			if(gs.isActive()) {
				g.drawImage(greenSlimeArr[gs.getEnemyState()][gs.getAniIndex()], 
						(int) gs.getHitbox().x - xLvlOffset - GREENSLIME_DRAWOFFSET_X + gs.flipX(), 
						(int) gs.getHitbox().y - GREENSLIME_DRAWOFFSET_Y, 
						GREENSLIME_WIDTH * gs.flipW(),
						GREENSLIME_HEIGHT, null);
				gs.drawHitbox(g, xLvlOffset);
				gs.drawAttackBox(g,xLvlOffset);
			}
			
		}
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (GreenSlime gs : greenSlime)
			if(gs.isActive()) {
				if (attackBox.intersects(gs.getHitbox())) {
					gs.hurt(10);
					return;
				}				
			}
	}

	private void loadEnemyImgs() {
		greenSlimeArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.GREENSLIME_SPRITE);
		for (int j = 0; j < greenSlimeArr.length; j++)
			for (int i = 0; i < greenSlimeArr[j].length; i++)
				greenSlimeArr[j][i] = temp.getSubimage(i * GREENSLIME_WIDTH_DEFAULT, j * GREENSLIME_HEIGHT_DEFAULT, GREENSLIME_WIDTH_DEFAULT, GREENSLIME_HEIGHT_DEFAULT);
	}
	
	public void resetAllEnemies() {
		for(GreenSlime gs : greenSlime) {
			gs.resetEnemy();
		}
	}
	public int getCountEnemies() {
		int sum = 0;
		for(GreenSlime gs :  greenSlime) {
			if(gs.isActive()) {
				sum++;
			}
		}
		return sum;
	}
	
}
