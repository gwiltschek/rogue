package org.digitalerasselbande.rogue.map;

import java.util.LinkedList;
import java.util.Random;

import org.digitalerasselbande.rogue.entity.Entity;
import org.digitalerasselbande.rogue.entity.EntityType;
import org.digitalerasselbande.rogue.entity.Player;
import org.digitalerasselbande.rogue.game.Game;
import org.digitalerasselbande.rogue.item.Item;

public class Map {

	// dimensions
	private int w;
	private int h;
	private LinkedList<Entity> entites = new LinkedList<Entity>();
	private LinkedList<Item> items = new LinkedList<Item>();
	private LinkedList<Room> rooms = new LinkedList<Room>();
	private Player p;
	private int killedMonsters = 0;
	private boolean allDead = true;
	
	// the map layout
	private String[][] map;
	
	// the map including all entities, items, etc
	private String[][] currentMap;
	
	// constructor
	public Map(int w, int h) {
		int x, y, i;
		map = new String[w][h];
		this.w = w;
		this.h = h;
		
		// fill map with walls
		for (y = 0; y < w; y++) {
			for (x = 0; x < h; x++) {
				map[x][y] = Game.WALL;	
			}			
		}

		// generate rooms
		boolean intersects = true;
		Room a = new Room(this);
		Room b = null;
		a.digRoom();
		rooms.add(a);
		for (i = 0; i < Game.NUM_ROOMS - 1; i++) {
			b = new Room(this);
			if (!Game.ALLOW_INTERSECTING_ROOMS) {
				while (intersects) {
					intersects = false;
					b = new Room(this);

					for (Room r : rooms) {
						if (r.intersects(b)) {
							intersects = true;
						}
					}
				}
			}
			b.digRoom();
			rooms.add(b);
			a = b;
			intersects = true;				
		}
		
		if (Game.CONNECT_ROOMS) {
			a = null;
			for (Room r : rooms) {
				if (a != null) {
					connect(r, a);
				}
					
				a = r;
			}
		}
	}
	
	public boolean inSameRoom(Entity a, Entity b) {
		for (Room r : rooms) {
			if ((r.inRoom(a.getPos_x(), a.getPos_y(), 0)) && (r.inRoom(b.getPos_x(), b.getPos_y(), 0))) {
				return true;
			}
		}
		return false;
	}

	public String[][] getMap() {
		return this.map;
	}
	
	private void connect(Room a, Room b) {
		Random r = new Random();
		int x, y;
		
		int x_a = a.getRoom_x() + r.nextInt(a.getRoom_w());
		int y_a = a.getRoom_y() + r.nextInt(a.getRoom_h());

		int x_b = b.getRoom_x() + r.nextInt(b.getRoom_w());
		int y_b = b.getRoom_y() + r.nextInt(b.getRoom_h());
		
		int x_dir = -1;
		// a left of b
		if (x_a <= x_b) {
			x_dir = 1;
		}

		int y_dir = -1;
		// a above of b
		if (y_a <= y_b) {
			y_dir = 1;
		}
		
		System.out.println(x_dir);
		
		for (x = x_a; x != x_b; x+=x_dir) {
			map[x][y_a] = Game.EMPTY_SPACE;
		}
		
		// x should be be on line with room b now
		for (y = y_a; y != y_b; y+=y_dir) {
			map[x][y] = Game.EMPTY_SPACE;
		}
		
		a.addConnectedRoom(b);
		b.addConnectedRoom(a);
	}
	
	public String[][] draw(int x1, int x2, int y1, int y2) {
		int x, y;
		String[][] output = new String[w][h];

		// build output map
		for (y = 0; y < w; y++) {
			for (x = 0; x < h; x++) {
				output[x][y] = map[x][y];
			}			
		}
		
		// add items
		for (Item item : items) {
			output[item.getPos_x()][item.getPos_y()] = item.getSymbol();
		}
		
		// add entities
		for(Entity e : entites) {
			output[e.getPos_x()][e.getPos_y()] = e.getSymbol();
		}
		
		// add player
		output[p.getPos_x()][p.getPos_y()] = p.getSymbol();
		
		// save state
		currentMap = output;
		
		String outputString = "HP: " + p.getHealth() + " | XP: " + p.getExp();
		System.out.println(outputString);
		
		return currentMap;
	}
	
	public void addPlayer(Player p) {
		this.p = p;
	}
	
	public void addEntity(Entity e) {
		allDead = false;
		entites.add(e);
	}

	public void addItem(Item i) {
		items.add(i);
	}

	// CLEAN ME TODO
	public boolean collidesEntity(int x, int y) {
		for (Entity e: entites) {
			if ((e != (Entity)p) && (e.getPos_x() == x) && (e.getPos_y() == y) && (!e.isPushable())) {
				p.setHealth(p.getHealth()-e.getAttack());
				e.setHealth(e.getHealth()-p.getAttack());
				System.out.println("ATK, ENEMY HP " + e.getHealth());
				if (e.isDead) {
					if (e.getType() == EntityType.MONSTER) {
						killedMonsters++;
					}
					Item drop = e.getDrop();
					drop.setPos(x, y);
					addItem(drop);
					entites.remove(e);
					p.setExp(p.getExp() + e.getEarnsExp());
					if (killedMonsters == Game.NUM_MONSTERS) {
						allDead = true;
					}
				}
				return true;
			}
		}

		return collidesWall(x, y);
	}

	// check if something collides with the player
	public boolean collidesPlayer(int x, int y) {
		if ((p.getPos_x() == x) && (p.getPos_y() == y)) {
			return true;
		}
		return false;
	}
	
	// check if there's an item at x/y
	public void collidesItem(int x, int y) {
		Item itemToRemove = null;
		for (Item item : items) {
			if ((item.getPos_x() == x) && (item.getPos_y() == y)) {
				item.onCollision(p);
				itemToRemove = item;
			}
		}
		
		if (itemToRemove != null) {
			if (itemToRemove.isPickable()) {
				items.remove(itemToRemove);			
			}
		}
	}
	
	// check for a wall at x, y
	public boolean collidesWall(int x, int y) {
		if (map[x][y] != Game.EMPTY_SPACE) {
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
	
	public void dig(int x, int y) {
		map[x][y] = Game.EMPTY_SPACE;
	}

	public void fill(int x, int y) {
		map[x][y] = Game.WALL;
	}
}
