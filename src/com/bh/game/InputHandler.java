package com.bh.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {
	public class Key {
		public boolean pressed = false;
		public boolean clicked = false;
		public int presses = 0;
		public int absorbs = 0;
		
		public Key() {
			keys.add(this);
		}
		
		public void toggle(boolean press) {
			if (pressed != press) {
				pressed = press;
				if (press) {
					presses++;
				}
			}
		}
		
		public void tick() {
			if (absorbs < presses) {
				absorbs++;
				clicked = true;
			} else if (absorbs > presses) {
				absorbs = presses;
				clicked = false;
			} else {
				clicked = false;
			}
		}
	}
	
	public InputHandler(Game game) {
		game.addKeyListener(this);
	}

	public List<Key> keys = new ArrayList<Key>();
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key shoot = new Key();
	public Key speed = new Key();
	
	public void tick() {
		for (Key k : keys) {
			k.tick();
		}
	}
	
	public void toggle(int e, boolean press) {
		if (e == KeyEvent.VK_UP)
			up.toggle(press);
		if (e == KeyEvent.VK_DOWN)
			down.toggle(press);
		if (e == KeyEvent.VK_LEFT)
			left.toggle(press);
		if (e == KeyEvent.VK_RIGHT)
			right.toggle(press);
		if (e == KeyEvent.VK_SPACE)
			shoot.toggle(press);
		if (e == KeyEvent.VK_SHIFT)
			speed.toggle(press);
	}

	public void keyPressed(KeyEvent e) {
		toggle(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggle(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {
	}
}
