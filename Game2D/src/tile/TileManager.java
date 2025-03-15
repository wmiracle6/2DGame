package tile;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;
import java.io.BufferedReader;

public class TileManager {
	GamePanel gp;
	Tile[] tile;
	int mapTileNum[][];
	
	
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		tile = new Tile[10];
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		getTileImage();
		loadMap("/maps/map02.txt");
	}
	
	public void getTileImage() {
		try {
			tile[0] = createTile("/tiles/pool.png");
            tile[1] = createTile("/tiles/green_blood1.png");
            tile[2] = createTile("/tiles/wall.png");
            tile[3] = createTile("/tiles/wall_left.png");
            tile[4] = createTile("/tiles/wall_right.png");
            tile[5] = createTile("/tiles/wall_up.png");
            tile[6] = createTile("/tiles/wall_down.png");
            tile[7] = createTile("/tiles/lava.png");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	private Tile createTile(String path) throws IOException {
        Tile newTile = new Tile();
        newTile.image = ImageIO.read(getClass().getResourceAsStream(path));
        if (newTile.image == null) {
            System.err.println("Ошибка: Изображение '" + path + "' не найдено.");
        }
        return newTile;
    }
	
	public void loadMap(String filePath) {
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			int col = 0;
			int row = 0;
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				String line = br.readLine();
				while(col < gp.maxWorldCol) {
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col++;
				}
				if (col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();
		
		
		}catch(Exception e) {
				
			}
	}
	
	
	public void draw(Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0;
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			int tileNum = mapTileNum[worldCol][worldRow];
			int worldX  = worldCol * gp.tileSize;
			int worldY  = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
					worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
					worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
					worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			}
			
			worldCol++;

			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;

			}
		}
		
}
}
