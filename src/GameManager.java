import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GameManager extends JPanel implements ActionListener {

	private Timer timer;
	private Player player;
	private ArrayList<Asteroid> ufos;
	private boolean isGameOver;
	private final int PLAYER_X = 250;
	private final int PLAYER_Y = 150;
	private final int B_WIDTH = 400;
	private final int B_HEIGHT = 300;
	private final int DELAY = 15;

	// private final int[][] pos = {
	// {2380, 29}, {2500, 59}, {1380, 89},
	// {780, 109}, {580, 139}, {680, 239},
	// {790, 259}, {760, 50}, {790, 150},
	// {980, 209}, {560, 45}, {510, 70},
	// {930, 159}, {590, 80}, {530, 60},
	// {940, 59}, {990, 30}, {920, 200},
	// {900, 259}, {660, 50}, {540, 90},
	// {810, 220}, {860, 20}, {740, 180},
	// {820, 128}, {490, 170}, {700, 30}
	// };
	private final int[][] pos = new int[10][2];

	public GameManager() {

		initBoard();
	}

	private void initBoard() {

		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		isGameOver = true;

		// setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

		player = new Player(PLAYER_X, PLAYER_Y);

		initUFOs();

		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void initUFOs() {
		ufos = new ArrayList<>();
		Random rand = new Random();

		for (int[] p : pos) {
			p[0] = rand.nextInt(400);
			p[1] = rand.nextInt(300);
		}
		for (int[] p : pos) {
			ufos.add(new Asteroid(p[0], p[1], rand.nextInt(3) - 2, rand
					.nextInt(3) - 2));
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (isGameOver) {

			drawObjects(g);

		} else {

			drawGameOver(g);
		}

		Toolkit.getDefaultToolkit().sync();
	}

	private void drawObjects(Graphics g) {

		ArrayList<Bullet> ms = player.getBullets();

		for (Bullet m : ms) {
			if (m.isVisible()) {
				g.drawImage(m.getImage(), m.getX(), m.getY(), this);
			}
		}

		for (Asteroid a : ufos) {
			if (a.isVisible()) {
				g.drawImage(a.getImage(), a.getX(), a.getY(), this);
			}
		}

		g.setColor(Color.WHITE);
		g.drawString("Aliens left: " + ufos.size(), 5, 15);

		if (player.isVisible()) {
//			Image img = null;
//			Image rot = null;
//
//			int buffer[] = new int[32 * 32];
//			int rotate[] = new int[32 * 32];
//			try {
//
//				PixelGrabber grabber = new PixelGrabber(player.getImage(), 0, 0, 32, 32, buffer, 0, 32);
//				try {
//					grabber.grabPixels();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				for (int y = 0; y < 32; y++) {
//					for (int x = 0; x < 32; x++) {
//						rotate[((32 - x - 1) * 32) + y] = buffer[(y * 32) + x];
//					}
//				}
//				rot = createImage(new MemoryImageSource(32, 32, rotate, 0, 32));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
			
			
			
			
			
//			  Graphics2D g2d = (Graphics2D)g;
//			     AffineTransform origXform = g2d.getTransform();
//			     AffineTransform newXform = (AffineTransform)(origXform.clone());
//			     //center of rotation is center of the panel
//			     int xRot = this.getWidth()/2;
//			     int yRot = this.getHeight()/2;
//			     newXform.rotate(Math.toRadians(45), xRot, yRot);
//			     g2d.setTransform(newXform);
//			     //draw image centered in panel
////			     int x = (getWidth() - player.getImage().getWidth(this))/2;
////			     int y = (getHeight() - player.getImage().getHeight(this))/2;
//			     g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);
//			     g2d.setTransform(origXform);
			
		}

	}

	private void drawGameOver(Graphics g) {

		String msg = "Game Over";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics fm = getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2, B_HEIGHT / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		inGame();

		updatePlayer();
		updateBullets();
		updateUFOs();

		checkCollisions();

		repaint();
	}

	private void inGame() {

		if (!isGameOver) {
			timer.stop();
		}
	}

	private void updatePlayer() {

		if (player.isVisible()) {
			player.move();
		}
	}

	private void updateBullets() {

		ArrayList<Bullet> ms = player.getBullets();

		for (int i = 0; i < ms.size(); i++) {

			Bullet m = ms.get(i);

			if (m.isVisible()) {
				m.move();
			} else {
				ms.remove(i);
			}
		}
	}

	private void updateUFOs() {

		if (ufos.isEmpty()) {

			isGameOver = false;
			return;
		}

		for (int i = 0; i < ufos.size(); i++) {

			Asteroid a = ufos.get(i);
			if (a.isVisible()) {
				a.move();
			} else {
				ufos.remove(i);
			}
		}
	}

	public void checkCollisions() {

		Rectangle r3 = player.getBounds();

		for (Asteroid alien : ufos) {
			Rectangle r2 = alien.getBounds();

			if (r3.intersects(r2)) {
				player.setVisible(false);
				alien.setVisible(false);
				isGameOver = false;
			}
		}

		ArrayList<Bullet> ms = player.getBullets();

		for (Bullet m : ms) {

			Rectangle r1 = m.getBounds();

			for (Asteroid alien : ufos) {

				Rectangle r2 = alien.getBounds();

				if (r1.intersects(r2)) {
					m.setVisible(false);
					alien.setVisible(false);
				}
			}
		}
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e);
		}
	}
}