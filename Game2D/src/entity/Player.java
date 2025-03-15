package entity;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	
	long lastSprintEndTime = 0; // Время окончания последнего спринта
	final long sprintCooldown = 3000; // Кулдаун в миллисекундах (3 секунды)
	// Анимация
    int spriteCounter = 0;
    int spriteNum = 1;
	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		setDefaultValues(); 
		getPlayerImage();
	}
	public void setDefaultValues() {
		worldX = 500; worldY = 500;
		speed = 2;
		sprintspeed = 30;
		direction = "down";
		
	}
	
	public void getPlayerImage() {
		try {
			 up1 = ImageIO.read(getClass().getResourceAsStream("/player/up1_assets.png"));
			 up2 = ImageIO.read(getClass().getResourceAsStream("/player/up2_assets.png"));
			 up3 = ImageIO.read(getClass().getResourceAsStream("/player/up3_assets.png"));
			 down1 = ImageIO.read(getClass().getResourceAsStream("/player/down1_assets.png"));
			 down2 = ImageIO.read(getClass().getResourceAsStream("/player/down2_assets.png")); 
			 down3 = ImageIO.read(getClass().getResourceAsStream("/player/down3_assets.png")); 
			 left1 = ImageIO.read(getClass().getResourceAsStream("/player/left1_assets.png")); 
			 left2 = ImageIO.read(getClass().getResourceAsStream("/player/left2_assets.png"));
			 left3 = ImageIO.read(getClass().getResourceAsStream("/player/left3_assets.png"));
			 right1 = ImageIO.read(getClass().getResourceAsStream("/player/right1_assets.png"));
			 right2 = ImageIO.read(getClass().getResourceAsStream("/player/right2_assets.png"));
			 right3 = ImageIO.read(getClass().getResourceAsStream("/player/right3_assets.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		// Проверяем, нажаты ли одновременно противоположные клавиши
		boolean isMovingForwardAndBackward = keyH.upPressed && keyH.downPressed;
	    boolean isMovingLeftAndRight = keyH.leftPressed && keyH.rightPressed;
	    // Если нажаты одновременно вперёд и назад или влево и вправо, блокируем движение
	    boolean isContradictoryMovement = isMovingForwardAndBackward || isMovingLeftAndRight;
	    // Проверяем, прошёл ли кулдаун
	    boolean isCooldownOver = System.currentTimeMillis() - lastSprintEndTime >= sprintCooldown;
	    
	    
	    // Проверяем, нажата ли клавиша спринта (например, Shift) и нет ли противоречивых движений
	    if (keyH.sprintPressed && gp.stamina > 0 && !isContradictoryMovement && isCooldownOver) {
	    	gp.isSprinting = true;
	    } else {
	    	gp.isSprinting = false;
	    }
        // Управление движением с учётом спринта
        int currentSpeed = gp.isSprinting ? sprintspeed : speed;
        
        if (keyH.upPressed && !keyH.downPressed || 
        	    keyH.downPressed && !keyH.upPressed ||
        	    keyH.leftPressed && !keyH.rightPressed || 
        	    keyH.rightPressed && !keyH.leftPressed) 
        {
        	// Движение вперёд и назад
            if (keyH.upPressed && !keyH.downPressed) {
                direction = "up";
                worldY -= currentSpeed;
            }
            if (keyH.downPressed && !keyH.upPressed) {
            	direction = "down";
            	worldY += currentSpeed;
            }

            // Движение влево и вправо
            if (keyH.leftPressed && !keyH.rightPressed) {
            	direction = "left";
            	worldX -= currentSpeed;
            }
            if (keyH.rightPressed && !keyH.leftPressed) {
            	direction = "right";
            	worldX += currentSpeed;
            }
    		
         
            // Анимация
            updateAnimation();
        }
        boolean isMoving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;
     // Уменьшаем выносливость при спринте (если нет противоречивых движений)
        if (gp.isSprinting && !isContradictoryMovement && isMoving) {
        	gp.stamina -= gp.staminaDrainRate;
            if (gp.stamina <= 0) { // Если выносливость <= 0, отключаем спринт
            	gp.stamina = 0;
            	gp.isSprinting = false;
                lastSprintEndTime = System.currentTimeMillis(); // Записываем время окончания спринт
            }
        } else if (gp.stamina < 100) {
            // Восстанавливаем выносливость, если игрок не спринтует
        	gp.stamina += gp.staminaRegenRate;
            if (gp.stamina > 100) {
            	gp.stamina = 100;
            }
        }
        		
	}
	private void updateAnimation() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            spriteCounter++;
            if (spriteCounter > 10) { // Частота смены кадров
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }
	public void draw(Graphics2D g2) {

		BufferedImage image = null;
		boolean isMoving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;

	    switch (direction) {
	        case "up":
	            // Если игрок двигается, используем анимацию (up1 или up2), иначе статичное изображение (up3)
	            image = isMoving ? (spriteNum == 1 ? up1 : up2) : up3;
	            break;
	        case "down":
	            // Если игрок двигается, используем анимацию (down1 или down2), иначе статичное изображение (down3)
	            image = isMoving ? (spriteNum == 1 ? down1 : down2) : down3;
	            break;
	        case "left":
	            // Если игрок двигается, используем анимацию (left1 или left2), иначе статичное изображение (left3)
	            image = isMoving ? (spriteNum == 1 ? left1 : left2) : left3;
	            break;
	        case "right":
	            // Если игрок двигается, используем анимацию (right1 или right2), иначе статичное изображение (right3)
	            image = isMoving ? (spriteNum == 1 ? right1 : right2) : right3;
	            break;
	    }
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
	}
}
