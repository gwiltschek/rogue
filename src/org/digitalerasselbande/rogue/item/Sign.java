package org.digitalerasselbande.rogue.item;

import java.util.Random;

public class Sign extends Item {

	private String message = "";
	private String messages[] = {"Foobar", "Where has the happy hippo gone?", "You are here."};
	
	public Sign() {
		this.setSymbol("T");
		this.message = messages[new Random().nextInt(messages.length)];
	}
	
	@Override
	public void onCollision() {
		System.out.println("The sign says: " + message);
	}
}
