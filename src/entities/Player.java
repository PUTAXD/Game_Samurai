package entities;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.PlayerConstant.*;
import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity {

	private BufferedImage[][] animation;	
	private int aniTick, aniIndex, aniSpeed = 20;
	private int playerAction = IDLE;
	private int playerDir = -1;
	private boolean moving = false, attacking = false;
	private boolean right, left, up, down, jump;
	private float playerSpeed = 2.0f;
	private int [][] lvlData;
	private float xDrawOffset = 22 * Game.SCALE;
	private float yDrawOffset = 59 * Game.SCALE;
	
	//gravity
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -3.0f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;
	
	// StatusBarUI
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	
	private int maxHealth = 100;
	private int currentHealth = 15;
	private int healthWidth = healthBarWidth;


	//attack
	private Rectangle2D.Float attackBox;

	private int flipX = 0;
	private int flipW = 1;

	
	private boolean attackChecked;
	private Playing playing;
	
		
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		loadAnimation();
//		initHitbox(x,y, 25 * Game.SCALE, 44 * Game.SCALE);
		initHitbox(x,y, 25 * Game.SCALE, 69 * Game.SCALE);
		initAttackBox();
	}
	
	
	private void initAttackBox() {
		// TODO Auto-generated method stub
		attackBox = new Rectangle2D.Float(x, y, (int) (40 * Game.SCALE), (int) (60 * Game.SCALE));
		
	}

	public void update() {
		updateHealthBar();
		if(currentHealth <= 0||hitbox.y >= Game.GAME_HEIGHT) {
			if (playerAction != DEAD) {
				playerAction = DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
			} else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= playerSpeed - 1) {
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			} else
				updateAnimationTick();

			return;
		}
		
		updateAttackBox();
		
		updatePos();
		if(attacking)
			checkAttack();
		updateAnimationTick();	
		setAnimation();
	}

	private void checkAttack() {
		// TODO Auto-generated method stub
		if(attackChecked || aniIndex != 4) {
			return;
		}
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.ATTACK_ONE);
	}


	private void updateAttackBox() {
		if (right) {
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
		}
		else if (left) {
			attackBox.x = hitbox.x - 40 - (int) (Game.SCALE * 10);			
		}

		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}


	private void updateHealthBar() {
		// TODO Auto-generated method stub
		healthWidth = (int)((currentHealth/(float)maxHealth)* healthBarWidth);
	}

	public void render(Graphics g, int xLvlOffset) {
//		System.out.println("PLayar Action : "+ playerAction);
		g.drawImage(animation[playerAction][aniIndex], 
				(int) (hitbox.x - xDrawOffset) - xLvlOffset + flipX, 
				(int) (hitbox.y - yDrawOffset), 
				width * flipW, height, null);
//		g.drawImage(animation[playerAction][aniIndex], 
//				(int) (hitbox.x - xDrawOffset) - xLvlOffset , 
//				(int) -200, 
//				width, height, null);
		
		drawHitbox(g, xLvlOffset);
		drawAttackBox(g, xLvlOffset);
		drawUI(g);
	}
	
	private void drawAttackBox(Graphics g, int lvlOffsetX) {
		g.setColor(Color.red);
		g.drawRect((int) attackBox.x - lvlOffsetX, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);

	}
	
	private void drawUI(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}

	private void updateAnimationTick() {
		// TODO Auto-generated method stub
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
			}
		}
	}
	
	public void setAnimation() {
		int startAni = playerAction;
		
		if(moving) {
			playerAction = RUN;
		}else{
//			System.out.println("print playeraction : " + moving);
			playerAction = IDLE;
		}
		

		if (inAir) {
			if (airSpeed < 0)
				playerAction = JUMP;
			else
				playerAction = JUMP;
		}

		
		if(attacking) {
			playerAction = ATTACK_2;
			if(startAni != ATTACK_2) {
				aniIndex = 2;
				aniTick = 0;
				return;
			}
		}
		
		if(startAni != playerAction) {
			resetAniTick();
		}
	

	}
	
	
	private void resetAniTick() {
		// TODO Auto-generated method stub
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;

		if (jump)
			jump();
		
//		if (!left && !right && !inAir)
//			return;

		if(!inAir)
			if((!left && !right) || (right && left))
				return;
		
		float xSpeed = 0;

		if (left) {
			xSpeed -= playerSpeed;
			flipX = 67;
			flipW = -1;
		}
		if (right) {
			xSpeed += playerSpeed;
			flipX = 0;
			flipW = 1;
		}

		if (!inAir)
			if (!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}

		} else
			updateXPos(xSpeed);
		moving = true;
	}
	
	
	private void jump() {
		if (inAir)
			return;
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;

	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;

	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}

	}


	public void changeHealth(int value) {
		currentHealth += value;

		if (currentHealth <= 0) {
			currentHealth = 0;
			//gameover
		}else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}
	
	private void loadAnimation() {
		// TODO Auto-generated method stub
		
		BufferedImage[] img = LoadSave.GetSpriteSamurai();

				animation = new BufferedImage[7][9];
				for(int j = 0; j <animation.length;j++) {
					for(int i = 0; i<GetSpriteAmount(j) ; i++) {
						animation[j][i] = img[j].getSubimage(i*128, 0, 128, 128);
					}
				}
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
		
	}
	
	public void loadLevelData(int[][] lvlData) {
		this.lvlData = lvlData;
//		if (!IsEntityOnFloor(hitbox, lvlData))
//			inAir = true;

	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	
	
	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setJump(boolean jump) {
		// TODO Auto-generated method stub
		this.jump = jump;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}


	public void resetAll() {
		// TODO Auto-generated method stub
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		playerAction = IDLE;
		currentHealth = maxHealth;
		
		hitbox.x = x;
		hitbox.y = y;
		
		if(!IsEntityOnFloor(hitbox,lvlData)) {
			inAir = true;
		}
	}
	

	
//	public boolean getMoving() {
//		return moving;
//	}
//	public int getPlayerAction() {
//		return playerAction;
//	}
}
