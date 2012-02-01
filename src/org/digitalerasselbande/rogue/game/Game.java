package org.digitalerasselbande.rogue.game;

import org.digitalerasselbande.rogue.entity.Monster;
import org.digitalerasselbande.rogue.entity.Pet;
import org.digitalerasselbande.rogue.entity.Player;
import org.digitalerasselbande.rogue.map.Map;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

	public Game() {
		super("rogue");
	}

	public static final int NUM_ROOMS = 4;
	public static final int WORLD_WIDTH = 32;
	public static final int WORLD_HEIGHT = 32;
	public static final int NUM_SIGNS = 4;
	public static final String WALL = "I";
	public static final String EMPTY_SPACE = " ";
	public static final int WINDOW_SIZE = 32;
	public static final int NUM_MONSTERS = 2;
	public static final int MIN_ROOM_WIDTH = 4;
	public static final int MIN_ROOM_HEIGHT = 4;
	public static final int MAX_ROOM_WIDTH = 6;
	public static final int MAX_ROOM_HEIGHT = 6;

	
	private static Map map;
	private static String[][] currentMap;
	private static Player p;
	private static Pet pet;
	private static int turns = 0;

	public String outputMap;

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new Game());
			app.setDisplayMode(16*WORLD_WIDTH, 16*WORLD_HEIGHT, false);
			app.setShowFPS(false);
			app.start();
			//app.setAlwaysRender(true);

		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		resetWorld();
	}
	
	private void resetWorld() {
		int i;
		map = new Map(WORLD_WIDTH, WORLD_HEIGHT);
		p = new Player();
		pet = new Pet(p, map);

		p.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
		while (map.collidesWall(p.getPos_x(), p.getPos_y())) {
			p.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
		}

		pet.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
		while (map.collidesWall(pet.getPos_x(), pet.getPos_y())) {
			pet.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
		}

		// map.addEntity(p);
		map.addPlayer(p);
		map.addEntity(pet);
		for (i = 0; i < NUM_MONSTERS; i++) {
			Monster m = new Monster(map);
			m.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
			while (map.collidesWall(m.getPos_x(), m.getPos_y())) {
				m.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
			}
			map.addEntity(m);
		}
		currentMap = drawWorld();
	}

	private static String[][] drawWorld() {
		int x1, x2, y1, y2;
		int window_size = WINDOW_SIZE;
		x1 = p.getPos_x() - window_size;
		x1 = x1 > 0 ? x1 : 0;
		x2 = p.getPos_x() + window_size;
		x2 = x2 < WORLD_WIDTH ? x2 : WORLD_WIDTH;
		y1 = p.getPos_y() - window_size;
		y1 = y1 > 0 ? y1 : 0;
		y2 = p.getPos_y() + window_size;
		y2 = y2 < WORLD_HEIGHT ? y2 : WORLD_HEIGHT;
		return map.draw(x1, x2, y1, y2);
	}

	private static void drawDeathMessage() {
		System.out.println("You survived " + turns
				+ " turns, but now you're dead!");
	}

	private static void drawVictoryMessage() {
		System.out.println("You killed all evil monsters in " + turns
				+ " turns, yay!");
	}

	private static void moveEntities() {
		map.moveEntities();
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		boolean buttonPressed = false;
		int new_x, new_y;
		new_x = p.getPos_x();
		new_y = p.getPos_y();
		
		if (container.getInput().isKeyPressed(Input.KEY_UP)) {
			new_y = (p.getPos_y() - 1);
			buttonPressed = true;
		}
		if (container.getInput().isKeyPressed(Input.KEY_DOWN)) {
			new_y = (p.getPos_y() + 1);
			buttonPressed = true;
		}
		if (container.getInput().isKeyPressed(Input.KEY_LEFT)) {
			new_x = (p.getPos_x() - 1);
			buttonPressed = true;
		}
		if (container.getInput().isKeyPressed(Input.KEY_RIGHT)) {
			new_x = (p.getPos_x() + 1);
			buttonPressed = true;
		}
		if (container.getInput().isKeyPressed(Input.KEY_DELETE)) {
			resetWorld();
			buttonPressed = false;
		}

		if (buttonPressed) {
			buttonPressed = false;
			turns++;
			
			moveEntities();
			
			if ((new_x >= 0) && (new_x < WORLD_WIDTH) && (new_y >= 0)
					&& (new_y < WORLD_HEIGHT)) {
				if (!map.collides(new_x, new_y)) {
					map.collidesItem(new_x, new_y);
					p.setPos(new_x, new_y);
				}
			}

			currentMap = drawWorld(); 
			
			if (map.allDead()) {
				drawVictoryMessage();
			}

			if (p.isDead()) {
				drawDeathMessage();
			}		
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		int x, y;
		
		if (currentMap != null) {
			for (y = 0; y < Game.WINDOW_SIZE; y++) {
				for (x = 0; x < Game.WINDOW_SIZE; x++) {
					if (currentMap[x][y] == Game.EMPTY_SPACE) {
						g.setColor(Color.white);	
					}
					else if (currentMap[x][y] == p.getSymbol()) {
						g.setColor(Color.green);
					}
					else if (currentMap[x][y] == pet.getSymbol()) {
						g.setColor(Color.pink);
					}
					else if (currentMap[x][y] == "!") {
						g.setColor(Color.red);
					}
					else if (currentMap[x][y] == "T") {
						g.setColor(Color.yellow);
					}
					else if (currentMap[x][y] == "d") {
						g.setColor(Color.orange);
					}
					else if (currentMap[x][y] == Game.WALL) {
						g.setColor(Color.gray);
					}
					else {
						g.setColor(Color.blue);
					}
					g.fillRect(x*16, y*16, 16, 16);
				}				
			}			
		}
	}

}