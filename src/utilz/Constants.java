package utilz;

import main.Game;

public class Constants {
	public static final int ANI_SPEED = 25;
	
	public static class ObjectConstants {
		public static final int SPIKE_WIDTH_DEFAULT = 48;
		public static final int SPIKE_HEIGHT_DEFAULT = 48;
		public static final int SPIKE_WIDTH = (int) (Game.SCALE * SPIKE_WIDTH_DEFAULT);
		public static final int SPIKE_HEIGHT = (int) (Game.SCALE * SPIKE_HEIGHT_DEFAULT);

	}
	
	public static class Environment{
		public static final int BIG_BATS_WIDTH_DEFAULT = 448;
		public static final int BIG_BATS_HEIGHT_DEFAULT = 101;
		
		public static final int BIG_BATS_WIDTH = (int)(BIG_BATS_WIDTH_DEFAULT * Game.SCALE);
		public static final int BIG_BATS_HEIGHT = (int)(BIG_BATS_HEIGHT_DEFAULT * Game.SCALE);
	}
	
	
	public static class EnemyConstant{
		public static final int GREENSLIME = 0;
		
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK_3 = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;
		
		public static final int GREENSLIME_WIDTH_DEFAULT = 108;
		public static final int GREENSLIME_HEIGHT_DEFAULT = 48;
		
		public static final int GREENSLIME_WIDTH = (int)( GREENSLIME_WIDTH_DEFAULT * Game.SCALE);
		public static final int GREENSLIME_HEIGHT = (int)( GREENSLIME_HEIGHT_DEFAULT * Game.SCALE);
		
		//Gambar offset dari slime
//		public static final int GREENSLIME_DRAWOFFSET_X = (int) (39 * Game.SCALE);
//		public static final int GREENSLIME_DRAWOFFSET_Y = (int) (96 * Game.SCALE);
		
		public static final int GREENSLIME_DRAWOFFSET_X = (int) (26 * Game.SCALE);
		public static final int GREENSLIME_DRAWOFFSET_Y = (int) (22* Game.SCALE);
		
		public static int GetSpriteAmount(int enemy_type, int enemy_state) {
			switch(enemy_type) {
			case GREENSLIME:
				switch (enemy_state) {
				case IDLE:
					return 8;
				case RUNNING:
					return 7;
				case ATTACK_3:
					return 5;
				case HIT:
					return 6;
				case DEAD:
					return 3;
				}
			}
			return 0;
		}
		

		public static int GetMaxHealth(int enemy_type) {
			switch (enemy_type) {
			case GREENSLIME:
				return 10;
			default:
				return 1;
			}
		}
		

		public static int GetEnemyDmg(int enemy_type) {
			switch (enemy_type) {
			case GREENSLIME:
				return 15;
			default:
				return 0;
			}

		}
		
		
	}
	
	public static class UI {
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
		}

		public static class PauseButtons {
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
		}

		public static class URMButtons {
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);

		}

		public static class VolumeButtons {
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;

			public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
		}
	}
	
	public static class Direction{
		public static final int LEFT = 0;
		public static final int RIGHT= 1;
		public static final int UP = 2;
		public static final int DOWN = 3;
	}
	
	public static class PlayerConstant{
		public static final int IDLE = 0;
		public static final int WALK = 1;
		public static final int RUN = 2;
		public static final int JUMP = 3;
		public static final int HURT = 4;
		public static final int DEAD = 5;
		public static final int ATTACK_2 = 6;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case IDLE:
			case DEAD:
				return 6;
			case WALK:
			case JUMP:
				return 9;
			case RUN :
				return 8;
			case HURT :
				return 3;
			case ATTACK_2:
				return 5;
			default:
				return 1;
			}
		}
	}
}
