package com.bh.game.level.entity;

import java.awt.Rectangle;

import com.bh.game.Game;
import com.bh.game.InputHandler;
import com.bh.game.graphics.Screen;
import com.bh.game.level.Level;
import com.bh.game.sound.Sound;

public class Player extends Entity {
	
	public final int maxHealth = 200;
	public int health = maxHealth;
	
	public int score = 0;

	public Player(int x, int y) {
		super(x, y, "/player.png");
	}

	public void tick() {
		super.tick();
		InputHandler input = Game.instance.input;
		
		int speed = ((input.speed.pressed) ? 5 : 2);
		
		if (input.up.pressed) {
			y -= speed;
		}
		if (input.down.pressed) {
			y += speed;
		}
		if (input.left.pressed) {
			x -= speed;
		}
		if (input.right.pressed) {
			x += speed;
		}		
		if (y < Game.HEIGHT - 48) y = Game.HEIGHT - 48;
		if (y + h >= Game.HEIGHT) y = Game.HEIGHT - h;
		if (x < 0) x = 0;
		if (x + w >= Game.WIDTH) x = Game.WIDTH - w;

		Level level = Game.instance.level;
		if (input.shoot.clicked) {
			Bullet b = new Bullet(x + 4, y - 9, 0);
			level.addEntity(b);
			Sound.shoot.play();
		}
		
		for (int i = 0; i < level.bullets.size(); i++) {
			if (new Rectangle(x, y, 16, 16).intersects(new Rectangle(level.bullets.get(i).x, level.bullets.get(i).y, 8, 8)) && level.bullets.get(i).canHarmPlayer) {
				level.removeEntity(level.bullets.get(i));
				health -= 10;
				Sound.playerHurt.play();
			}
		}
		
		if (health <= 0) {
			die();
		}
	}
	
	public void preRender(Screen screen) {
		screen.addLightSource(x + 8, y + 8, 40, 255);
	}

	private void die() {
		alive = false;
	}
}
