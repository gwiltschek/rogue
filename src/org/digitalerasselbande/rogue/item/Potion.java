package org.digitalerasselbande.rogue.item;

import java.util.Random;

import org.digitalerasselbande.rogue.entity.Player;

public class Potion extends Item {

	private int health = 25;

	public Potion() {
		this.setPickable(true);
		this.setSymbol("d");
		this.setSymbolString(this.getSymbol());
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	@Override
	public void onCollision(Player p) {
		Random r = new Random();
		p.setHealth(p.getHealth() + this.health);
		
		System.out.println("You found a potion and drank it.");
		
		if (r.nextInt(10) == 0) {
			System.out.println("Sadly it was poison. You die. Sorry.");
			p.setHealth(-1);
			return;
		}
		
		System.out.println("You gain " + health + " health!");
	}
}
