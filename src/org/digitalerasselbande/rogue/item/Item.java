package org.digitalerasselbande.rogue.item;

import java.util.Random;

import org.digitalerasselbande.rogue.entity.Player;

public class Item {

	private boolean pickable;
	private String symbol;
	private String symbolString;
	private int pos_x;
	private int pos_y;
	
	public String getSymbolString() {
		return symbolString;
	}

	public void setSymbolString(String symbolString) {
		this.symbolString = symbolString;
	}
	
	public int getPos_x() {
		return pos_x;
	}

	public void setPos(int x, int y) {
		this.pos_x = x;
		this.pos_y = y;
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

	public Item() {
	}

	public boolean isPickable() {
		return pickable;
	}

	public void setPickable(boolean pickable) {
		this.pickable = pickable;
	}
	
	public void onCollision(Player p) {
		
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
