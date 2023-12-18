package entities;

import static utilz.Constants.Direction.*;
import static utilz.Constants.EnemyConstant.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static utilz.HelpMethods.IsEntityOnFloor;
import static utilz.HelpMethods.IsFloor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;
public class GreenSlime extends Enemy {
	
	private Rectangle2D.Float attackBox;
	private int attackBoxOffsetX;
	private static final int RIGHT = 0;

	public GreenSlime(float x, float y) {
		super(x, y, GREENSLIME_WIDTH, GREENSLIME_HEIGHT, GREENSLIME);
		// TODO Auto-generated constructor stub
		initHitbox(x,y,40,27);
		initAttackBox();
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (70 * Game.SCALE), (int) (19 * Game.SCALE));
		attackBoxOffsetX = (int) (Game.SCALE * 11);
	}
	

	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData,player);
		updateAnimationTick();
		updateAttackBox();
	}
	
	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;

	}
	private void updateBehavior(int[][] lvlData, Player player) {
		if (firstUpdate) 
			firstUpdateCheck(lvlData);

		if (inAir) {
			updateInAir(lvlData);
		}else {
			switch (enemyState) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, player)) {
//					System.out.println("Slime liat Player");
					turnTowardsPlayer(player);
					if (isPlayerCloseForAttack(player)) {
						newState(ATTACK_3);
					}
				}
				
				
				move(lvlData);
				break;
			case ATTACK_3:
				if(aniIndex == 0)
					attackChecked = false;
				
				if(aniIndex == 3 && !attackChecked)
					checkEnemyHit(attackBox,player);
				break;
			case HIT :
				
				break;
			}
		}

	}
	


	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}
	
	public int flipX() {
		if(walkDir == RIGHT) {
			return width-5;
		}else {
			return 0;
		}
	}
	
	public int flipW() {
		if(walkDir == RIGHT) {
			return -1;
		}else
			return 1;
	}
	

}
