package org.digitalerasselbande.rogue.entity;

import java.util.Random;

import org.digitalerasselbande.rogue.map.Map;

public class Npc extends Entity {

	private Map map;
	
	public Npc(Map map) {
		this.setHealth(9999999); // really hard to kill
		this.setSymbol("N");
		this.setSymbolString(this.getSymbol());
		this.setIsPushable(false);
		this.setType(EntityType.NPC);
		this.map = map;
	}

}
