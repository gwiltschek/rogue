package org.digitalerasselbande.rogue.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.digitalerasselbande.rogue.entity.Monster;
import org.digitalerasselbande.rogue.entity.Player;
import org.digitalerasselbande.rogue.map.Map;

public class Game {

	public static final int WORLD_WIDTH = 16;
	public static final int WORLD_HEIGHT = 16;
	private static final int NUM_MONSTERS = 3;
	
	private static boolean isRunning = true;
	private static Map map = new Map(WORLD_WIDTH, WORLD_HEIGHT);
	private static Player p;
	
	public static void main(String [ ] args) {
		int i;
		p = new Player();
		p.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
		map.addEntity(p);
		map.addPlayer(p);
		
		for (i = 0; i < NUM_MONSTERS; i++) {
			Monster m = new Monster(map);
			m.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
			map.addEntity(m);
		}
		
		while (isRunning && !p.isDead()) {
			moveEntities();
			drawWorld();
			handleInput(readInput());
		}
		
		if (p.isDead()) {
			drawDeathMessage();
		}
		
		System.exit(0);
	}
	
	private static int readInput() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			return reader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	private static void handleInput(int key) {
		int new_x, new_y;
		new_x = p.getPos_x();
		new_y = p.getPos_y();
		
		switch (key) {
			case 119: //w up
				new_y = (p.getPos_y()-1);
				break;
			case 115: // s down
				new_y = (p.getPos_y()+1);
				break;
			case 97: // a left
				new_x = (p.getPos_x()-1);
				break;
			case 100: // d right
				new_x = (p.getPos_x()+1);
				break;
			default:
		}
		
		if (!map.collides(new_x, new_y)) {
			p.setPos(new_x, new_y);
		}
		
	}
	
	private static void drawWorld() {
		map.draw();
	}
	
	private static void drawDeathMessage() {
		System.out.println("You're dead");
	}
	
	private static void moveEntities() {
		map.moveEntities();
	}
	
}
