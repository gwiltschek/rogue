package org.digitalerasselbande.rogue.game;

import org.digitalerasselbande.rogue.entity.Monster;
import org.digitalerasselbande.rogue.entity.Pet;
import org.digitalerasselbande.rogue.entity.Player;
import org.digitalerasselbande.rogue.map.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
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

	public static final int NUM_ROOMS = 5;
	public static final int WORLD_WIDTH = 32;
	public static final int WORLD_HEIGHT = 32;
	public static final int NUM_SIGNS = 6;
	public static final String WALL = "I";
	public static final String EMPTY_SPACE = " ";
	public static final int WINDOW_SIZE = 32;
	public static final int NUM_MONSTERS = 10;
	public static final int MONSTERS_MIN_HEALTH = 35;
	public static final int MONSTERS_MAX_HEALTH = 105;
	public static final int MIN_ROOM_WIDTH = 4;
	public static final int MIN_ROOM_HEIGHT = 4;
	public static final int MAX_ROOM_WIDTH = 6;
	public static final int MAX_ROOM_HEIGHT = 6;
	public static final int MIN_ROOM_DISTANCE = 2;
	public static final boolean CONNECT_ROOMS = true;
	public static boolean PLAYER_CAN_DIG = false;
	public static final boolean ALLOW_INTERSECTING_ROOMS = false;
	private static final int VIEW_RANGE = 3;
	private static int VIEW_RANGE_CURRENT = VIEW_RANGE;
	private static Map map;
	private static String[][] currentMap;
	private static Player p;
	private static Pet pet;
	private static int turns = 0;
	private static AppGameContainer app;
	
	private boolean showMiniMap = false;
    private static boolean showMessage = false;
	private static String message = "";
	
	public String outputMap;

	public static void main(String[] args) {
		try {
			app = new AppGameContainer(new Game());
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

     	// init OpenGL     	
     	GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
//    	GL11.glMatrixMode(GL11.GL_PROJECTION);
//    	GL11.glLoadIdentity();
//    	GL11.glOrtho(0, WINDOW_SIZE * 16, 0, WINDOW_SIZE * 16, 1, -1);
//    	GL11.glMatrixMode(GL11.GL_MODELVIEW);
//     
	}
	
	private void resetWorld() {
		int i;
		showMiniMap = false;
		showMessage = false;
		message = "";
		turns = 0;
		
		map = new Map(WORLD_WIDTH, WORLD_HEIGHT);
		p = new Player(map);
		pet = new Pet(p, map);

		p.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
		while (map.collidesWall(p.getPos_x(), p.getPos_y())) {
			p.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
		}

		pet.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
		int tries = 0;
		while (map.collidesWall(pet.getPos_x(), pet.getPos_y())) {
			while (!map.inSameRoom(p, pet) && (tries < 50)) {
				tries++;
				pet.randomizePosition(WORLD_WIDTH, WORLD_HEIGHT);
			}
			tries = 0;
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
		
		if (container.getInput().isKeyPressed(Input.KEY_UP)) {
			p.handleInput(Input.KEY_UP);
			buttonPressed = true;
		}
		if (container.getInput().isKeyPressed(Input.KEY_DOWN)) {
			p.handleInput(Input.KEY_DOWN);
			buttonPressed = true;
		}
		if (container.getInput().isKeyPressed(Input.KEY_LEFT)) {
			p.handleInput(Input.KEY_LEFT);
			buttonPressed = true;
		}
		if (container.getInput().isKeyPressed(Input.KEY_RIGHT)) {
			p.handleInput(Input.KEY_RIGHT);
			buttonPressed = true;
		}		
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			p.handleInput(Input.KEY_SPACE);
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
		if (container.getInput().isKeyPressed(Input.KEY_D)) {
			PLAYER_CAN_DIG = !PLAYER_CAN_DIG;
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
		
		if (buttonPressed) {
			buttonPressed = false;
			turns++;
			p.update();
			moveEntities();
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
		renderMapGL(container, g, 0, 0, 16, WINDOW_SIZE);
		// minimap
		if (showMiniMap) {
			renderMapGL(container, g, 0, 0, 2, WORLD_HEIGHT);			
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

	public void renderMapGL(GameContainer container, Graphics g, int x_start, int y_start, int tileSize, int size) {
		int x, y;	
		float fr, fg, fb;
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		if (currentMap != null) {
			for (y = 0; y < size; y++) {
				for (x = 0; x < size; x++) {
					if (currentMap[x][y] == Game.EMPTY_SPACE) {
						fr = 1.0f; fg = 1.0f; fb = 1.0f;
					}
					else if (currentMap[x][y] == Game.WALL) {
						fr = 0.5f; fg = 0.5f; fb = 0.5f;
					}
					else if (currentMap[x][y] == p.getSymbol()) {
						fr = 0.0f; fg = 1.0f; fb = 0.0f;
					}
					else if (currentMap[x][y] == pet.getSymbol()) {
						fr = 1.0f; fg = 0.75f; fb = 0.6f;
					}
					else if (currentMap[x][y] == "!") {
						fr = 1.0f; fg = 0.0f; fb = 0.0f;
					}
					else if (currentMap[x][y] == "T") {
						fr = 1.0f; fg = 1.0f; fb = 0.0f;
					}
					else {
						fr = 0.0f; fg = 0.0f; fb = 1.0f;
					}
							
					// create quads for each tile 
					GL11.glBegin(GL11.GL_QUADS);
					
						GL11.glColor3f(fr, fg, fb);
						GL11.glVertex2i((x_start+x)*tileSize, (y_start+y)*tileSize);
						GL11.glVertex2i((x_start+x)*tileSize, tileSize + (y_start+y)*tileSize);
						GL11.glVertex2i(tileSize +  (x_start+x)*tileSize, tileSize + (y_start+y)*tileSize);
						GL11.glVertex2i(tileSize + (x_start+x)*tileSize, (y_start+y)*tileSize);						

					GL11.glEnd();
				}				
			}
		}	
		Display.update();
	}
}