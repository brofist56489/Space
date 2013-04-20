package com.bh.game.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	public BufferedImage image;
	public int[] pixels;
	
	public int w;
	public int h;
	
	public Image(String path) {
		try {
			image = ImageIO.read(Image.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.w = image.getWidth();
		this.h = image.getHeight();
		
		pixels = image.getRGB(0, 0, w, h, null, 0, w);
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] += 0x1000000;
		}
	}

}
