package com.bh.game.graphics;

public class Screen {
	
	private final int BIT_MIRROR_X = 0x01;
	private final int BIT_MIRROR_Y = 0x02;
	
	public int width;
	public int height;
	public int[] pixels;
	public int[] pixelBrightness;
	
	public int xOff;
	public int yOff;
	
	private boolean lightingEnabled = true;
	
	public Screen(int w, int h) {
		width = w;
		height = h;
		pixels = new int[w * h];
		pixelBrightness = new int[w * h];
	}
	
	public void clear(int c) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = c;
		}
	}
	
	public void clearLighting(int b) {
		for (int i = 0; i < pixelBrightness.length; i++) {
			pixelBrightness[i] = b;
		}
	}
	
	public void applyLightingToAllPixels() {
		if (!lightingEnabled) return;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = applyLighting(pixels[x + y * width], x, y);				
			}
		}
	}
	
	public void render(int[] pixs, int xp, int yp, int w, int h, int flip, boolean useLight) {
		xp -= xOff;
		yp -= yOff;
		
		boolean flipx = ((flip & BIT_MIRROR_X) == BIT_MIRROR_X);
		boolean flipy = ((flip & BIT_MIRROR_Y) == BIT_MIRROR_Y);
		
		for (int y = 0; y < h; y++) {
			if ((y + yp) < 0 || (y + yp) >= height) continue;
			int ys = y;
			if (flipy) ys = (h - 1) - y;
			for (int x = 0; x < w; x++) {
				if ((x + xp) < 0 || (x + xp) >= width) continue;
				int xs = x;
				if (flipx) xs = (w - 1) - x;
				
				int c = pixs[xs + ys * w];
				
				if (c % 0x1000000 == 0x7f007f) {
					continue;
				}
				
				if (lightingEnabled && useLight)
					c = applyLighting(c, (x + xp), (y + yp));
				
				pixels[(x + xp) + (y + yp) * width] = c;
			}
		}
	}
	
	public void renderRect(int x, int y, int w, int h, int c) {
		for (int yy = 0; yy < h; yy++) {
			if ((yy + y) < 0 || (yy + y) >= height) continue;
			for (int xx = 0; xx < w; xx++) {
				if ((xx + x) < 0 || (xx + x) >= width) continue;
				pixels[(xx + x) + (yy + y) * width] = c;
			}
		}
	}
	
	
	private int applyLighting(int c, int x, int y) {
		int br = pixelBrightness[x + y * width];
		return applyLighting(c, br);
	}
	
	private int applyLighting(int c, int br) {
		int r = (c >> 16) & 0xFF;
		int g = (c >> 8) & 0xFF;
		int b = c & 0xFF;
		
		r = r * br / 255;
		g = g * br / 255;
		b = b * br / 255;
		
		c = r << 16 | g << 8 | b;
		return c;
	}
	
	public void addLightSource(int x, int y, int r, int br) {
		if (!lightingEnabled) return; 
		
		x -= xOff;
		y -= yOff;
		
		for (int yy = -r; yy < r; yy++) {
			if (yy + y < 0 || yy + y >= height) continue;
			for (int xx = -r; xx < r; xx++) {
				if (xx + x < 0 || xx + x >= width) continue;
				
				int d = xx*xx + yy*yy;
				if (d > r*r) continue;
				
				pixelBrightness[(xx + x) + (yy + y) * width] += br - d * br / (r * r);
				if (pixelBrightness[(xx + x) + (yy + y) * width] > 255) {
					pixelBrightness[(xx + x) + (yy + y) * width] = 255;
				}
			}
		}
	}

	public void setOffset(int x, int y) {
		this.xOff = x;
		this.yOff = y;
	}
	
	public void applyOffest(float x, float y) {
		this.xOff = (int) (x - (width / 2));
		this.yOff = (int) (y - (height / 2));
	}
	
	public int[] setPixels(int[] pixs) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixs[x + y * width] = pixels[x + y * width];
			}
		}
		return pixs;
	}
}