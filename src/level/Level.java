package level;

import java.util.ArrayList;

import entities.GreenSlime;
import utilz.HelpMethods;

public class Level {

	private int[][] lvlData;

	public Level(int[][] lvlData) {
		this.lvlData = lvlData;
	}


	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public void printLevel() {
		 for (int[] row : lvlData) {
	            for (int cell : row) {
	                System.out.print(cell + "\t");
	            }
	            System.out.println();
	        }
	}
	
	public int[][] getLevelData(){
		return this.lvlData;
	}
	
}