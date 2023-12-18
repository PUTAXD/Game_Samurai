package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.GreenSlime;
import main.Game;




public class LoadSave {
	public static final String SRC_MAP = "map_src.png";
	public static final String LEVEL_SAMURAI = "Map_basic.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
	public static final String LEVEL_ONE_DATA = "level_one_data.png";
	public static final String MENU_BUTTONS = "button_menu.png";
	public static final String MENU_BACKGROUND = "Background_menu_Fix.png";
	public static final String MAP_2 = "D:\\INDRA\\PBO\\Basic\\Samurai\\res\\map2.txt";
	public static final String GREENSLIME_SPRITE = "monster/Green_Slime/slime_sprite.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String PLAYING_BG_IMG = "Background_fix.png";
	public static final String BIG_BATS = "bigbat.png";
	public static final String TRAP_ATLAS = "tile39.png";
	public static final String DEATH_SCREEN = "death_screen_fix.png";
	public static final String URM_BUTTONS = "urm_button.png";
	public static final String PAUSE_BUTTON_OVERLAY = "pause_overlay.png";
	public static final String PAUSE_OVERLAY_BG = "pause_menu_fix.png";
	public static final String START_BG = "menu_background.png";
	public static final String COMPLETED_IMG = "complete_screen.png";
	

	public static BufferedImage[] GetSpriteSamurai() {
		BufferedImage[] img = null;
		InputStream is1 = LoadSave.class.getResourceAsStream("/Samurai/Idle.png");
		InputStream is2 = LoadSave.class.getResourceAsStream("/Samurai/Walk.png");
		InputStream is3 = LoadSave.class.getResourceAsStream("/Samurai/Run.png");
		InputStream is4 = LoadSave.class.getResourceAsStream("/Samurai/Jump.png");
		InputStream is5 = LoadSave.class.getResourceAsStream("/Samurai/Hurt.png");
		InputStream is6 = LoadSave.class.getResourceAsStream("/Samurai/Dead.png");
		InputStream is7 = LoadSave.class.getResourceAsStream("/Samurai/Attack_2.png");
		
		InputStream[] is = {is1,is2,is3,is4,is5,is6,is7} ;
		
		
			try {
				img = new BufferedImage[7];
				for(int i = 0;i<img.length;i++) {
					img[i] = ImageIO.read(is[i]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return img;
	}
	public static BufferedImage[] GetSpriteGreenSlime() {
		BufferedImage[] img = null;
		InputStream is1 = LoadSave.class.getResourceAsStream("/monster.Green_Slime/Idle.png");
		InputStream is2 = LoadSave.class.getResourceAsStream("/Samurai/Walk.png");
		InputStream is3 = LoadSave.class.getResourceAsStream("/Samurai/Run.png");
		InputStream is4 = LoadSave.class.getResourceAsStream("/Samurai/Jump.png");
		InputStream is5 = LoadSave.class.getResourceAsStream("/Samurai/Hurt.png");
		InputStream is6 = LoadSave.class.getResourceAsStream("/Samurai/Dead.png");
		InputStream is7 = LoadSave.class.getResourceAsStream("/Samurai/Attack_2.png");
		
		InputStream[] is = {is1,is2,is3,is4,is5,is6,is7} ;
		
		
			try {
				img = new BufferedImage[7];
				for(int i = 0;i<img.length;i++) {
					img[i] = ImageIO.read(is[i]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return img;
	}
	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static int[][] GetLevelData() {
		String txt = MAP_2;
		
		int[][] lvlData = readTextFile(txt);
		return lvlData;

	}
	
	public static ArrayList<GreenSlime> GetGreenSlime() {
		ArrayList<GreenSlime> list = new ArrayList<>();
		int lvlData[][] = GetLevelData();
		for (int j = 0; j < lvlData.length; j++)
			for (int i = 0; i < lvlData[j].length; i++) {
				int value = lvlData[j][i];
				if (value == 202)
					list.add(new GreenSlime(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;

	}

	

    private static int[][] readTextFile(String fileName) {
        int[][] data = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(fileName));

            // Membaca baris pertama untuk mendapatkan jumlah kolom
            String firstLine = br.readLine();
            int numCols = firstLine.split(",").length;

            // Menghitung jumlah baris
            long numRows = 1; // Menginisialisasi dengan 1 karena baris pertama sudah dibaca
            while (br.readLine() != null) {
                numRows++;
            }
            br.close(); // Menutup file setelah menghitung baris

            // Inisialisasi array
            data = new int[(int) numRows][numCols];

            // Membaca data dan menyimpan dalam array
            br = new BufferedReader(new FileReader(fileName));
            int row = 0;
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Memeriksa apakah jumlah kolom pada setiap baris konsisten
                if (values.length == numCols) {
                    for (int col = 0; col < numCols; col++) {
                        // Mengganti nilai -1 dengan 203
                        data[row][col] = Integer.parseInt(values[col].equals("-1") ? "203" : values[col]);
                    }
                    row++;
                } else {
                    System.out.println("Warning: Baris dengan jumlah kolom yang tidak konsisten dilewati.");
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }
}
