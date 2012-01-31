package org.digitalerasselbande.rogue.entity;

import java.util.Random;

import org.digitalerasselbande.rogue.game.Game;
import org.digitalerasselbande.rogue.map.Map;

public class Monster extends Entity {

	private Map map;
	
	public Monster(Map map) {
		setSymbol("!");
		setHealth(10 + new Random().nextInt(40));
		this.map = map;
	}	
	
	// monsters move randomly one field up, down, left or right
	public void update() {
		int m = -1;
		int new_pos;
		
		if (new Random().nextBoolean()) {
			m = 1;
		}
		if (new Random().nextBoolean()) {
			new_pos = (this.getPos_x() + m);
			if (!(map.collidesPlayer(new_pos, this.getPos_y())) && (new_pos >= 0) && (new_pos < Game.WORLD_WIDTH) && (!map.collidesWall(new_pos, this.getPos_y()))) {
				this.setPos_x(new_pos);				
			}
		}
		else {
			new_pos = this.getPos_y() + m;
			if (!(map.collidesPlayer(this.getPos_x(), new_pos)) && (new_pos >= 0) && (new_pos < Game.WORLD_HEIGHT) && (!map.collidesWall(this.getPos_x(), new_pos))) {
				this.setPos_y(new_pos);
			}
		}
		
		
	}
	
}
