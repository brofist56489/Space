package com.bh.game.sound;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
	public static final Sound shoot = new Sound("/shoot.wav");
	public static final Sound playerHurt = new Sound("/playerHurt.wav");
	public static final Sound enemyDie = new Sound("/enemyDie.wav");
	
	private AudioClip clip;
	
	public Sound(String str) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(str));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		stop();
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		clip.stop();
	}
}
