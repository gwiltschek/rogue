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
	private static final int VIEW_RANGE = 3;
	private static int VIEW_RANGE_CURRENT = VIEW_RANGE;
	private static Map map;
	private static String[][] currentMap;
	private static Player p;
	private static Pet pet;
	private static int turns = 0;
	
	private boolean showMiniMap = false;
    private static boolean showMessage = false;
	private static String message = "";
	
	public String outputMap;

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new Game());
			app.setDisplayMode(16*WINDOW_SIZE, 16*WINDOW_SIZE, false);
			app.setShowFPS(false);
			app.start();		
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
		showMiniMap = false;
		showMessage = false;
		message = "";
		turns = 0;
		
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

	private static void drawDeathMessage(Graphics g) {
		drawMessage("You survived " + turns + " turns, but now you're dead!", g);
	}

	private static void drawVictoryMessage(Graphics g) {
		drawMessage("You killed all evil monsters in " + turns + " turns, yay!", g);
	}

	private static void drawMessage(String msg, Graphics g) {
		showMessage = true;
		message = msg;
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
		if (container.getInput().isKeyPressed(Input.KEY_M)) {
			showMiniMap = !showMiniMap;
			buttonPressed = false;
		}
		if (container.getInput().isKeyPressed(Input.KEY_V)) {
			if (VIEW_RANGE_CURRENT == VIEW_RANGE) {
				VIEW_RANGE_CURRENT = WINDOW_SIZE;
			}
			else {
				VIEW_RANGE_CURRENT = VIEW_RANGE;
			}
			buttonPressed = false;
		}

		if (container.getInput().isKeyPressed(Input.KEY_Q)) {
			drawDeathMessage(container.getGraphics());
			buttonPressed = false;
		}
		if (container.getInput().isKeyPressed(Input.KEY_W)) {
			drawVictoryMessage(container.getGraphics());
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
				drawVictoryMessage(container.getGraphics());
			}

			if (p.isDead()) {
				drawDeathMessage(container.getGraphics());
			}		
		}				
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		renderMap(container, g, 0, 0, 16, WINDOW_SIZE);
		
		// minimap
		if (showMiniMap) {
			renderMap(container, g, 0, 0, 2, WORLD_HEIGHT);			
		}		
	}
	
	public void renderMap(GameContainer container, Graphics g, int x_start, int y_start, int tileSize, int size) {
		int x, y;	
		if (currentMap != null) {
			for (y = 0; y < size; y++) {
				for (x = 0; x < size; x++) {
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
					else if (currentMap[x][y] == Game.WALL) {
						g.setColor(Color.gray);
					}
					else {
						g.setColor(Color.blue);
					}
					g.fillRect((x_start+x)*tileSize, (y_start+y)*tileSize, tileSize, tileSize);
				}				
			}
			int p_x = p.getPos_x();
			int p_y = p.getPos_y();
			
			for (y = 0; y < size; y++) {
				for (x = 0; x < size; x++) {
					g.setColor(Color.black);
					if (((y < p_y - VIEW_RANGE_CURRENT) || (y > p_y + VIEW_RANGE_CURRENT)) ||
						((x < p_x - VIEW_RANGE_CURRENT) || (x > p_x + VIEW_RANGE_CURRENT))) {
						g.fillRect((x_start+x)*tileSize, (y_start+y)*tileSize, tileSize, tileSize);							
					}
				}
			}
		}
		if (showMessage == true) {
			x = 10;
			y = WORLD_HEIGHT*16/2;
			
			g.setColor(Color.red);
			g.fillRect(x, y-16, (WORLD_WIDTH*16)-2*x, 64);
			g.setColor(Color.white);
			g.drawString(message, x*2, y);
		}
	}
}