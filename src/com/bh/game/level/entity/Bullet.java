package com.bh.game.level.entity;

import com.bh.game.Game;
import com.bh.game.graphics.Screen;

public class Bullet extends Entity {
	
	public boolean canHarmPlayer = false;
	private int d;
	
	public Bullet(int x, int y, int d) {
		super(x, y, "/bullet.png");
		this.d = d;
		if (d == 1) {
			canHarmPlayer = true;
		}
	}
	
	public void tick() {
		if (d == 0) {
			y -= 3;
		}
		if (d == 1) {
			y += 3;
		}
		if (y < 0 || y >= Game.HEIGHT) {
			alive = false;
		}
	}
	
	public void preRender(Screen screen) {
		screen.addLightSource(x + 4, y + 4, 20, 200);
	}
}
