package org.digitalerasselbande.rogue.map;

import java.util.LinkedList;
import java.util.Random;

import org.digitalerasselbande.rogue.entity.Entity;
import org.digitalerasselbande.rogue.entity.Player;
import org.digitalerasselbande.rogue.game.Game;

public class Map {

	// dimensions
	private int w;
	private int h;
	private LinkedList<Entity> entites = new LinkedList<Entity>();
	private Player p;
	private boolean allDead = true;
	
	// the map
	private String[][] map;
	
	// constructor
	public Map(int w, int h) {
		int x, y;
		Random r = new Random();
		map = new String[w][h];
		this.w = w;
		this.h = h;
		
		for (y = 0; y < w; y++) {
			for (x = 0; x < h; x++) {
				map[x][y] = Game.WALL;	
			}			
		}

		generateRoom();
		generateRoom();
		
		generateRoom();

		//generateDoors();
		

	}

	private void generateDoors() {
		Random r = new Random();
		int x, y;
		
		for (y = 0; y < w; y++) {
			for (x = 0; x < h; x++) {
				if (map[x][y].equals(Game.WALL)) {
					if (r.nextInt(10) == 0) {
						map[x][y] = " ";
					}
				}
			}
		}
	}
	
	private void generateRoom() {
		Random r = new Random();
		int min_room_w = 3;
		int min_room_h = 3;
		int max_room_w = 10;
		int max_room_h = 10;
		
		// generate room dimensions
		int room_w = min_room_w + r.nextInt(max_room_w);
		int room_h = min_room_h + r.nextInt(max_room_h);
		
		// find a place to start
		int room_x = 0 + r.nextInt(Game.WORLD_WIDTH) - room_w;
		int room_y = 0 + r.nextInt(Game.WORLD_WIDTH) - room_h;
		
		if (room_x < 0) room_x = 0;
		if (room_y < 0) room_y = 0;

		// draw room into map
		for (int y = room_y; y < room_y + room_h; y++) {
			for (int x = room_x; x < room_x + room_w; x++) {
				map[x][y] = " ";
				
			}
		}
	}
	
	public void draw(int x1, int x2, int y1, int y2) {
		int x, y;
		String[][] output = new String[w][h];

		// build output map
		for (y = 0; y < w; y++) {
			for (x = 0; x < h; x++) {
				output[x][y] = map[x][y];
			}			
		}
		
		// add entities
		for(Entity e : entites) {
			output[e.getPos_x()][e.getPos_y()] = "\033[31m" + e.getSymbol() + "\033[0m";
		}
		output[p.getPos_x()][p.getPos_y()] = "\033[32m" + p.getSymbol() + "\033[0m";

		// player pos
		System.out.println(p.getPos_x() + " " + p.getPos_y());
		
		// draw output map window
		for (y = y1; y < y2; y++) {
			for (x = x1; x < x2; x++) {
				System.out.print(output[x][y]);
			}			
			System.out.println();
		}
		for (int i = 0; i < 15; i++) {
			System.out.print("-");
		}
		
		System.out.println();
		System.out.println("HP: " + p.getHealth() + " | XP: " + p.getExp());

	}

	public void addPlayer(Player p) {
		this.p = p;
	}
	
	public void addEntity(Entity e) {
		allDead = false;
		entites.add(e);
	}
	
	// CLEAN ME TODO
	public boolean collides(int x, int y) {
		for (Entity e: entites) {
			if ((e != (Entity)p) && (e.getPos_x() == x) && (e.getPos_y() == y)) {
				p.setHealth(p.getHealth()-e.getAttack());
				e.setHealth(e.getHealth()-p.getAttack());
				System.out.println("ATK, ENEMY HP " + e.getHealth());
				if (e.isDead) {
					entites.remove(e);
					p.setExp(p.getExp() + e.getEarnsExp());
					if (entites.size() == 0) {
						allDead = true;
					}
				}
				return true;
			}
		}

		return collidesWall(x, y);
	}

	public boolean collidesPlayer(int x, int y) {
		if ((p.getPos_x() == x) && (p.getPos_y() == y)) {
			return true;
		}
		return false;
	}
	
	public boolean collidesWall(int x, int y) {
		if (map[x][y] != " ") {
			return true;
		}
		return false;
	}
	
	public void moveEntities() {
		for (Entity e : entites) {
			e.update();
		}
	}
	
	public boolean allDead() {
		return allDead;
	}
}
