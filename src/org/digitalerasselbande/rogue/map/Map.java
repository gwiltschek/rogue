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
				if (r.nextInt(10) == 0) {
					map[x][y] = "O";					
				}
				else {
					map[x][y] = " ";
				}
			}			
		}
	}
	
	public void draw() {
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
		
		// draw output map
		for (y = 0; y < w; y++) {
			for (x = 0; x < h; x++) {
				System.out.print(output[x][y]);
			}			
			System.out.println();
		}
		for (int i = 0; i < Game.WORLD_WIDTH; i++) {
			System.out.print("-");
		}
		
		System.out.println();
		System.out.println("HP: " + p.getHealth());

	}

	public void addPlayer(Player p) {
		this.p = p;
	}
	
	public void addEntity(Entity e) {
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
		System.out.println(x + " " + y);
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
}
