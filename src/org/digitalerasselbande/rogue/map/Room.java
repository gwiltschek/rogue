package org.digitalerasselbande.rogue.map;

import java.util.LinkedList;
import java.util.Random;

import org.digitalerasselbande.rogue.game.Game;
import org.digitalerasselbande.rogue.item.Sign;

public class Room {
	private int room_x;
	private int room_y;
	private int room_w;
	private int room_h;
	private Map map;
	private LinkedList<Room> connectedWith = new LinkedList<Room>();
	
	public Room(Map map) {
		this.map = map;
		generateRoom();
	}
	
	public void addConnectedRoom(Room r) {
		connectedWith.add(r);
	}
	
	private void generateRoom() {
		Random r = new Random();
		int min_room_w = Game.MIN_ROOM_WIDTH;
		int min_room_h = Game.MIN_ROOM_HEIGHT;
		int max_room_w = Game.MAX_ROOM_WIDTH;
		int max_room_h = Game.MAX_ROOM_HEIGHT;
		
		// generate room dimensions
		room_w = min_room_w + r.nextInt(max_room_w);
		room_h = min_room_h + r.nextInt(max_room_h);
		
		// find a place to start
		room_x = 0 + r.nextInt(Game.WORLD_WIDTH) - room_w;
		room_y = 0 + r.nextInt(Game.WORLD_WIDTH) - room_h;
		
		if (room_x < 0) room_x = 0;
		if (room_y < 0) room_y = 0;

		// draw room into map
		for (int y = room_y; y < room_y + room_h; y++) {
			for (int x = room_x; x < room_x + room_w; x++) {
				map.getMap()[x][y] = Game.EMPTY_SPACE;
			}
		}
	
		// mighty random multiplier
		if (r.nextBoolean() || r.nextBoolean()) {
			Sign sign = new Sign();
			sign.randomizePosition(Game.WORLD_WIDTH, Game.WORLD_HEIGHT);
			while (!inRoom(sign.getPos_x(), sign.getPos_y(), 1)) {
				sign.randomizePosition(Game.WORLD_WIDTH, Game.WORLD_HEIGHT);
			}
			map.addItem(sign);				
		}
	}

	// check if coordinates are inside the room, with offset from wall, if needed 
	public boolean inRoom(int x, int y, int offset) {
		if ((x > room_x + offset) &&	(x < room_x + room_w - offset) && (y > room_y + offset) &&	(y < room_y + room_h - offset)) {
			return true;
		}
		return false;
	}
	
	public int getRoom_x() {
		return room_x;
	}

	public int getRoom_y() {
		return room_y;
	}

	public int getRoom_w() {
		return room_w;
	}

	public int getRoom_h() {
		return room_h;
	}
}