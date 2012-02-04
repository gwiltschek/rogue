package org.digitalerasselbande.rogue.quest;

import java.util.LinkedList;

import org.digitalerasselbande.rogue.entity.Entity;
import org.digitalerasselbande.rogue.entity.Player;
import org.digitalerasselbande.rogue.item.Item;

public class Quest {

	private Entity employer;
	private Player player;
	private QuestType type;
	private boolean active = false;
	private boolean finished = false;
	private int rewardXP;
	private int rewardGold;
	private LinkedList<Item> rewardItems;

	private String description;
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isFinished() {
		return finished;
	}

	
}
