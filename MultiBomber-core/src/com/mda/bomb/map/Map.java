package com.mda.bomb.map;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mda.bomb.map.tile.Tile;
import com.mda.bomb.map.tile.TileFactory;
import com.mda.bomb.map.tile.TileIDs;

public class Map {
	// x / y / tile
	private HashMap<Integer, HashMap<Integer, Tile>> tiles;
	private TileFactory tileFactory;

	public Map() {
		tiles = new HashMap<Integer, HashMap<Integer, Tile>>();
		tileFactory = new TileFactory();
	}

	public void initMap() {
		FileHandle file = Gdx.files.internal("maps/map1.txt");
		String textFile = file.readString();
		String textFileSplit[] = textFile.split("\n");
		boolean firstLine = true;
		int y = 0;
		int mapHeight = 0;
		for(String line : textFileSplit) {
			String[] split = line.split(",");
			// First need to initialize the hashmap
			if (firstLine) {
				int w = Integer.parseInt(split[0]);
				mapHeight = Integer.parseInt(split[1]);

				for (int x = 0; x < w; x++)
					tiles.put(x, new HashMap<Integer, Tile>());

				firstLine = false;
			} else {
				for (int x = 0; x < split.length; x++) {
					int nb = Integer.parseInt(split[x]);
					tiles.get(x).put(mapHeight - y - 1, tileFactory.getTileWithID(TileIDs.values()[nb]));
				}
				y++;
			}
		}
	}

	public void render(SpriteBatch batch) {
		for (int x = 0; x < getMapWidth(); x++) {
			for (int y = 0; y < getMapHeight(); y++) {
				int ts = getTileSize();
				batch.draw(tileFactory.getTileTexture(getTile(x, y).getTileID()), x * ts, y * ts);
			}
		}
	}

	public boolean canWalk(int x, int y) {
		if (x >= 0 && x < getMapWidth() && y >= 0 && y < getMapHeight())
			return !tiles.get(x).get(y).isSolid();
		else
			return false;
	}

	public Tile getTile(int x, int y) {
		if (x >= 0 && x < getMapWidth() && y >= 0 && y < getMapHeight())
			return tiles.get(x).get(y);
		else
			return null;
	}

	public int getTileSize() {
		return tileFactory.getTileTexture(TileIDs.TILE_GROUND).getWidth();
	}

	public HashMap<Integer, HashMap<Integer, Tile>> getTiles() {
		return tiles;
	}

	public int getMapWidth() {
		return tiles.size();
	}

	public int getMapHeight() {
		return tiles.get(0).size();
	}

	public void dispose() {
		tileFactory.dispose();
	}
}
