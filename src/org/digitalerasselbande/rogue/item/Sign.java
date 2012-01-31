package org.digitalerasselbande.rogue.item;

import java.util.Random;

import org.digitalerasselbande.rogue.entity.Player;

public class Sign extends Item {

	private String message = "";
	private String messages[] = {
			"Foobar",
			"Where has the happy hippo gone?",
			"You are here.",
			"Nothing to see here, move along.",
			"This sign left intentionally blank."
			};
	
	public Sign() {
		this.setSymbol("T");
		this.setSymbolString(this.getSymbol());
		this.setPickable(false);
		this.message = messages[new Random().nextInt(messages.length)];
	}
	
	@Override
	public void onCollision(Player p) {
		// TODO push the message somehow to the map.draw() method
		System.out.println("The sign says: " + message);
	}
}
