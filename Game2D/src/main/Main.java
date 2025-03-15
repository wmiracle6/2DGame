package main;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Game2D");
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameThread();
        
	}

}
