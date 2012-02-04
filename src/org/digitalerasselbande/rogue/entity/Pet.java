package org.digitalerasselbande.rogue.entity;

import java.util.Random;

import org.digitalerasselbande.rogue.map.Map;

public class Pet extends Entity {

	private Player owner;
	private Map map;
	private Random r = new Random();
	
	public Pet(Player owner, Map map) {
		this.setHealth(50);
		this.setAttack(15);
		this.setSymbol("*");
		this.setSymbolString("\033[34m" + this.getSymbol() + "\033[0m");
		this.setIsPushable(true);
		this.setType(EntityType.PET);
		this.owner = owner;
		this.map = map;
	}

	private void makeSound() {
		System.out.println("MIAU!");
	}
	
	@Override
	public void update() {
		int new_pos_x = this.getPos_x();
		int new_pos_y = this.getPos_y();
		
		// move towards player
		int dest_x = owner.getPos_x() + 1;
		int dest_y = owner.getPos_y() + 1;
		int dist_x = this.getPos_x() - dest_x;
		int dist_y = this.getPos_y() - dest_y;
		int dir_x = dist_x > 0 ? 1 : -1;
		int dir_y = dist_y > 0 ? 1 : -1;
		
		if (Math.abs(dist_x) >= Math.abs(dist_y)) {
			new_pos_x -= dir_x;
		}
		else {
			new_pos_y -= dir_y;
		}
		
		if (!map.collidesEntity(new_pos_x, new_pos_y)) {
			//if (!map.collidesPlayer(new_pos_x, new_pos_y)) {
				this.setPos(new_pos_x, new_pos_y);
			//}
		}
		
		if(r.nextInt(9) == 0) {
			if (map.inSameRoom(this, owner)) {
				makeSound();
			}
		}
	}
}
