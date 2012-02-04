package org.digitalerasselbande.rogue.quest;

import java.util.LinkedList;

import org.digitalerasselbande.rogue.entity.Entity;
import org.digitalerasselbande.rogue.item.Item;

public class Quest {

	private Entity employer;
	private QuestType type;
	private boolean finished = false;
	private int rewardXP;
	private int rewardGold;
	private LinkedList<Item> rewardItems;

	private String description;
}
