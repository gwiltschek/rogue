package org.digitalerasselbande.rogue.item;

import java.util.Random;

public class Item {

	private boolean pickable = false;
	private String symbol;
	public int getPos_x() {
		return pos_x;
	}

	public void setPos_x(int pos_x) {
		this.pos_x = pos_x;
	}

	public int getPos_y() {
		return pos_y;
	}

	public void setPos_y(int pos_y) {
		this.pos_y = pos_y;
	}

	private int pos_x;
	private int pos_y;
	
	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isPickable() {
		return pickable;
	}

	public void setPickable(boolean pickable) {
		this.pickable = pickable;
	}
	
	public void onCollision() {
		
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public void randomizePosition(int w, int h) {
		Random r = new Random();
		
		pos_x = r.nextInt(w);
		pos_y = r.nextInt(h);
	}
}
