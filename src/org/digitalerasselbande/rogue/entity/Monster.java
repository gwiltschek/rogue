package org.digitalerasselbande.rogue.entity;

import java.util.Random;

import org.digitalerasselbande.rogue.game.Game;

public class Monster extends Entity {

	public Monster() {
		setSymbol("!");
		setHealth(70 + new Random().nextInt(20));
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
			if ((new_pos >= 0) && (new_pos < Game.WORLD_WIDTH)) {
				this.setPos_x(new_pos);				
			}
		}
		else {
			new_pos = this.getPos_y() + m;
			if ((new_pos >= 0) && (new_pos < Game.WORLD_HEIGHT)) {
				this.setPos_y(new_pos);
			}
		}
		
		
	}
	
}
