package com.bh.game.level.entity;

import java.awt.Rectangle;
import java.util.Random;

import com.bh.game.Game;
import com.bh.game.graphics.Screen;
import com.bh.game.level.Level;
import com.bh.game.sound.Sound;

public class Enemy extends Entity {
	
	public Enemy(int x, int y) {
		super(x, y, "/enemy.png");
	}
	
	public void tick() {
		y += 1;
		Level level = Game.instance.level;
		
		for (int i = 0; i < level.bullets.size(); i++) {
			if (new Rectangle(x, y, 16, 16).intersects(new Rectangle(level.bullets.get(i).x, level.bullets.get(i).y, 8, 8)) && !level.bullets.get(i).canHarmPlayer) {
				level.removeEntity(level.bullets.get(i));
				die(level);
			}
		}
		
		if (y >= (level.h << 4) + 16) dieOffscreen(level);
		
		Random r = new Random();
		if (r.nextInt(100) == 0) {
			fire();
		}
	}
	
	private void fire() {
		Bullet b = new Bullet(x + 4, y + 16, 1);
		Level level = Game.instance.level;
		level.addEntity(b);
	}
	
	private void die(Level level) {
		alive = false;
		level.player.score += 10;
		Sound.enemyDie.play();
	}
	
	private void dieOffscreen(Level level) {
		level.player.health -= 50;
		alive = false;
		Sound.enemyDie.play();
	}
	
	public void preRender(Screen screen) {
		screen.addLightSource(x + 8, y + 8, 40, 128);
	}
}
