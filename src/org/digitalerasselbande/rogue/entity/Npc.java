package org.digitalerasselbande.rogue.entity;

import java.util.LinkedList;
import java.util.Random;

import org.digitalerasselbande.rogue.map.Map;
import org.digitalerasselbande.rogue.quest.Quest;

public class Npc extends Entity {

	private Map map;
	private LinkedList<Quest> quests;
	
	public Npc(Map map) {
		this.setHealth(Integer.MAX_VALUE); // really hard to kill
		this.setSymbol("N");
		this.setSymbolString(this.getSymbol());
		this.setIsPushable(false);
		this.setType(EntityType.NPC);
		this.map = map;
		generateQuests();
	}

	private void generateQuests() {
		
	}
	
	public LinkedList<Quest> getCurrentQuests() {
		LinkedList<Quest> cq = null;
		
		for (Quest q : quests) {
			if (q.isActive() && !q.isFinished()) {
				cq.add(q);				
			}
		}
		return cq;
	}
	
	public Quest getNewQuest() {
		if (quests.size() > 0) {
			return quests.getFirst();
		} else {
			generateQuests();
			return getNewQuest();
		}
	}
	
	public boolean checkQuestFinished(Quest q) {
		return q.isFinished();
	}
}
