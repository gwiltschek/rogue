package org.digitalerasselbande.rogue.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.digitalerasselbande.rogue.entity.Monster;
import org.digitalerasselbande.rogue.entity.Pet;
import org.digitalerasselbande.rogue.entity.Player;
import org.digitalerasselbande.rogue.map.Map;

public class Game {

	public static final int NUM_ROOMS = 3;
	public static final int WORLD_WIDTH = 48;
	public static final int WORLD_HEIGHT = 48;
	private static final int WINDOW_SIZE = 24;
	private static final int NUM_MONSTERS = 2;
	public static final int NUM_SIGNS = 4;
	public static final String WALL = "I";
	
	private static boolean isRunning = true;
	private static Map map = new Map(WORLD_WIDTH, WORLD_HEIGHT);
	private static Player p;
	private static Pet pet;
	private static int turns = 0;
	
	public static void main(String [ ] args) {
		int i;
		p = new Player();
		pet = new Pet(p, map);
		
		p.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
		while (map.collidesWall(p.getPos_x(), p.getPos_y())) {
			p.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);			
		}
		
		pet.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
		while (map.collidesWall(pet.getPos_x(), pet.getPos_y())) {
			pet.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);			
		}

		//map.addEntity(p);
		map.addPlayer(p);
		map.addEntity(pet);
		for (i = 0; i < NUM_MONSTERS; i++) {
			Monster m = new Monster(map);
			m.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
			while (map.collidesWall(m.getPos_x(), m.getPos_y())) {
				m.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);			
			}
			map.addEntity(m);
		}
		
		while (isRunning && !p.isDead() && !map.allDead()) {
			moveEntities();
			drawWorld();
			handleInput(readInput());
			turns++;
		}
		
		if (map.allDead()) {
			drawVictoryMessage();
		}
		
		if (p.isDead()) {
			drawDeathMessage();
		}
		
		System.exit(0);
	}
	
	private static int readInput() {
		Scanner kb = new Scanner(System.in);
		String entered = "";
				
		while(true)	{
			entered = kb.next();
			if((entered.equals("w")) || (entered.equals("a")) || (entered.equals("s")) || (entered.equals("d"))) {
				return Integer.valueOf(entered.charAt(0));				
			}
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
				break;
		}
		
		if ((new_x >= 0) && (new_x < WORLD_WIDTH) && (new_y >= 0) && (new_y < WORLD_HEIGHT)) {
			if (!map.collides(new_x, new_y)) {
				map.collidesItem(new_x, new_y);
				p.setPos(new_x, new_y);
			}
		}
	}
	
	private static void drawWorld() {
		int x1, x2, y1, y2;
		int window_size = WINDOW_SIZE;
		x1 = p.getPos_x() - window_size;
		x1 = x1 > 0 ? x1 : 0;
		x2 = p.getPos_x() + window_size;
		x2 = x2 < WORLD_WIDTH ? x2 : WORLD_WIDTH;
		y1 = p.getPos_y() - window_size;
		y1 = y1 > 0 ? y1 : 0;
		y2 = p.getPos_y() + window_size;
		y2 = y2 < WORLD_HEIGHT ? y2 : WORLD_HEIGHT;
		map.draw(x1,x2,y1,y2);
	}
	
	private static void drawDeathMessage() {
		System.out.println("You survived " + turns + " turns, but now you're dead!");
	}

	private static void drawVictoryMessage() {
		System.out.println("You killed all evil monsters in " + turns + " turns, yay!");
	}
	
	private static void moveEntities() {
		map.moveEntities();
	}
}