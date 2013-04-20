package com.bh.game.level.entity;

import com.bh.game.graphics.Image;
import com.bh.game.graphics.Screen;
import com.bh.game.level.Level;

public class Entity {
	
	public int x;
	public int y;
	
	public int w;
	public int h;
	
	public Image image;
	
	protected Level level;
	
	public boolean alive = true;
	
	public Entity(int x, int y, String path) {
		this.x = x;
		this.y = y;
		image = new Image(path);
		this.w = image.w;
		this.h = image.h;
	}
	
	public void setLevel(Level l) {
		this.level = l;
	}
	
	public void tick() {
		
	}
	
	public void preRender(Screen screen ) {
	}
	
	public void render(Screen screen) {
		screen.render(image.pixels, x, y, w, h, 0, true);
	}
}
