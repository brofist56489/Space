// Hello Adam

package com.bh.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.bh.game.graphics.Image;
import com.bh.game.graphics.Screen;
import com.bh.game.level.Level;
import com.bh.game.level.entity.Player;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 300;
	public static final int HEIGHT = WIDTH / 4 * 3;
	public static final int SCALE = 2;
	public static final String NAME = "Space Shooter ";
	public static Game instance;
	
	JFrame frame;
	Thread gameThread;
	public int tickCount = 0;
	boolean running = false;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	public Screen screen;
	public InputHandler input;
	
	public Level level;
	
	public Player player;
	
	private Image gameoverImage;
	private boolean gameover = false;
	
	public synchronized void start() {
		if (running)
			return;
		gameThread = new Thread(this, "MAIN_game");
		gameThread.start();
		running = true;
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public void init() {
		instance = this;
		input = new InputHandler(this);
		
		gameoverImage = new Image("/gameover.png");
		
		screen = new Screen(WIDTH, HEIGHT);
		level = new Level((WIDTH >> 4) + 1, (HEIGHT >> 4) + 1);
		player = new Player(WIDTH / 2 - 8, 0);
		level.setPlayer(player);
	}

	public void run() {
		
		init();
		
		long lt = System.nanoTime();
		double nsPt = 1000000000D/60D;
		double delta = 0.0;
		@SuppressWarnings("unused")
		int ticks = 0, frames = 0;
		long ltr = System.currentTimeMillis();
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lt) / nsPt;
			lt = now;
			
			while (delta >= 1) {
				tick();
				ticks++;
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - ltr >= 1000) {
				ltr += 1000; 
				ticks = frames = 0;
			}
		}
	}
	
	public void tick() {
		tickCount++;
		
		if (!player.alive) gameover = true;
		
		if (!gameover) { 
			input.tick();
			
			if (tickCount % 100 == 0) level.addEnemy();
			
			level.tick();
			frame.setTitle(NAME + " Score: " + player.score);
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		screen.clear(0);
		screen.clearLighting(0);
		
		level.preRender(screen);
		level.render(screen);
		
		int w = WIDTH * player.health / player.maxHealth;
		screen.renderRect(0, 0, w, 10, 0xFF0000);
		
		if (gameover) screen.render(gameoverImage.pixels, 17, (HEIGHT / 2) - 32, 256, 64, 0, false);
		
		pixels = screen.setPixels(pixels);
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.frame = new JFrame(NAME);
		game.frame.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.frame.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setResizable(true);
		game.frame.setVisible(true);
		game.frame.add(game);
		game.frame.pack();
		
		game.start();
	}
}
