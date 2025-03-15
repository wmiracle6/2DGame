package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JPanel;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel  implements Runnable{
	//Настрокай скрина
	//Персонаж
	final int originalTileSize = 16; //16x16
	//Скелинг на 3
	final int scale = 3;
	public final int tileSize = originalTileSize*scale;// 48x48
	//скелинг экрана 16 на 9
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 9;
	public int screenWidth = tileSize * maxScreenCol;
 	public int screenHeight = tileSize * maxScreenRow;
    
 // Переменные для спринта и выносливости
 	public boolean isSprinting = false;
 	public float stamina = 1000.0f; // Шкала выносливости (от 0 до 100)
 	public final float staminaDrainRate = 0.5f; // Скорость уменьшения выносливости при спринте
 	public final float staminaRegenRate = 0.2f; // Скорость восстановления выносливости
	
 	// параметры мира
 	public final int maxWorldCol = 50;
 	public final int maxWorldRow = 50;
 	public final int wolrdWidth = tileSize*maxWorldCol;
 	public final int wolrdHeight = tileSize*maxWorldRow;
 	
 	
	int FPS = 60;
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Thread Game2D;
	public Player player = new Player(this,keyH);


	// Переменные для подсчёта FPS
	long lastTime = System.nanoTime();
    long now;
    int frames = 0;
    int currentFPS = 0;
	
	//конструктор
	public GamePanel() {
        // Устанавливаем размеры окна в соответствии с разрешением экрана
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true); 
		
	}
	
	public void startGameThread() {
		Game2D = new Thread(this);
		Game2D.start(); 
	}

	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;
		double nextDrawTime = System.nanoTime() + drawInterval;
		while(Game2D != null) {
			update();
			repaint();
			// Подсчёт FPS
			frames++;
            now = System.nanoTime();
            if (now - lastTime >= 1000000000) { // Прошла 1 секунда
                currentFPS = frames;
                frames = 0;
                lastTime = now;
            }
			
			try {
				long remainingTime = (long) ((nextDrawTime - System.nanoTime()) / 1000000);
                if (remainingTime > 0) {
                    Thread.sleep(remainingTime);
                    }
				nextDrawTime += drawInterval;				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	public void update() {
		player.update();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//Отрисковка тайлов
		tileM.draw(g2);
		//Отрисковка игрока
		player.draw(g2);
		// Отрисовка шкалы выносливости
		g2.setColor(Color.green);
		g2.fillRect(screenWidth - 120, 10, (int) stamina, 20); // Зелёная полоса выносливости
		g2.setColor(Color.white);
        g2.drawRect(screenWidth - 120, 10, 100, 20); // Рамка для шкалы
		// Отрисовка FPS
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("FPS: " + currentFPS, 10, 25); // Позиция в левом верхнем углу
	}
	
}
