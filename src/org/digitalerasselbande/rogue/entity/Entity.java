package org.digitalerasselbande.rogue.entity;

import java.util.Random;

import org.digitalerasselbande.rogue.item.Item;

public class Entity {

	private int pos_x;
	private int pos_y;
	private String symbol = "";
	private String symbolString = "";
	private boolean isPushable = false;
	private int health = 100;
	private int attack = 10;
	private int exp = 0;
	private int earnsExp = 5;
	public boolean isDead = false;
	private Item drop;
	private EntityType type;
	
	
	public EntityType getType() {
		return type;
	}

	public void setType(EntityType npc) {
		this.type = npc;
	}

	public Item getDrop() {
		return drop;
	}

	public void  setDrop(Item i) {
		this.drop = i;
	}

	public boolean isDead() {
		return isDead;
	}
	
	public boolean isPushable() {
		return isPushable;
	}
	
	public void setIsPushable(boolean pushable) {
		this.isPushable = pushable;
	}
	
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
	
	public void setPos(int x, int y) {
		this.pos_x = x;
		this.pos_y = y;
	}
	
	public void randomizePosition(int w, int h) {
		Random r = new Random();
		
		pos_x = r.nextInt(w);
		pos_y = r.nextInt(h);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String s) {
		this.symbol = s;
	}

	public String getSymbolString() {
		return symbolString;
	}

	public void setSymbolString(String s) {
		this.symbolString = s;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
		if (health <= 0) {
			isDead = true;
		}
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}
	
	public void update() {
	}


	public int getExp() {
		return exp;
	}


	public void setExp(int exp) {
		this.exp = exp;
	}


	public int getEarnsExp() {
		return earnsExp;
	}


	public void setEarnsExp(int earnsExp) {
		this.earnsExp = earnsExp;
	}
}
