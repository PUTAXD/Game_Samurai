package entities;

import static utilz.Constants.EnemyConstant.*;
import static utilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Direction.*;

import main.Game;

public abstract class Enemy extends Entity {

	protected int aniIndex, enemyState, enemyType;
	protected int aniTick, aniSpeed = 25;
	protected boolean firstUpdate = true;
	protected boolean inAir = false;
	protected float fallSpeed; 
	protected float gravity  = 0.04f * Game.SCALE;
	protected float walkSpeed = 0.35f * Game.SCALE;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected int maxHealth;
	protected int currentHealth;
	protected boolean active = true;
	protected boolean attackChecked;
	
	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x, y, width, height);
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
	}
	
	protected void firstUpdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}
	
	protected void updateInAir(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += fallSpeed;
			fallSpeed += gravity;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
//			tileY = (int) (hitbox.y / Game.TILES_SIZE) + 1;
			tileY = (int) (hitbox.y / Game.TILES_SIZE) ;
		}
	}
	
	protected float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE) ;
		if (airSpeed > 0) {
			// Falling - touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else
			// Jumping
			return currentTile * Game.TILES_SIZE;

	}
	
	protected void move(int[][] lvlData) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		changeWalkDir();
	}
	
	protected void turnTowardsPlayer(Player player) {
		if (player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}
	
	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE)+1;
//		System.out.println("PLayer- y : "+ playerTileY);
//		System.out.println("Enemy -y : " + tileY);
		if (playerTileY == tileY) {
//			System.out.println("Tiles sama");
			if (isPlayerInRange(player)) {
//				System.out.println("player didalam range");
				if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
			}
		}

		return false;
	}
	
	protected boolean isPlayerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
	}
	
	protected boolean isPlayerCloseForAttack(Player player) {
		int temp = 0;
		if(walkDir == LEFT) {
			temp = 21;
		}else {
			temp = 0;
		}
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x - temp);
		return absValue <= attackDistance;
		
	}
	
	protected void newState(int enemyState) {
		this.enemyState = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}
	
	public void hurt(int amount) {
		currentHealth -= amount;
		if(currentHealth <= 0 ) {
			newState(DEAD);
		}else {
			newState(HIT);
		}
	}
	
	protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
		// TODO Auto-generated method stub
		if(attackBox.intersects(player.hitbox))
			player.changeHealth(-GetEnemyDmg(enemyType));
		attackChecked = true;
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;
				
				
				switch(enemyState) {
				case ATTACK_3 -> enemyState = IDLE;
				case DEAD -> active = false;
				}
			}
		}
	}
	
	protected void changeWalkDir() {
		// TODO Auto-generated method stub
		if(walkDir == LEFT) {
			walkDir = RIGHT;
		}else {
			walkDir = LEFT;
		}
	}

	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		fallSpeed = 0;
	}
	public int getAniIndex() {
		return aniIndex;
	}
	public int getEnemyState() {
		return this.enemyState;
	}
	
	public boolean isActive() {
		return this.active;
	}
}
