package com.mda.bomb.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mda.bomb.ecs.components.BombAIComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.map.tile.Tile;
import com.mda.bomb.map.tile.TileFactory;
import com.mda.bomb.map.tile.TileIDs;

public class Map {
	// x,y = tile
	private HashMap<Integer, HashMap<Integer, Tile>> tiles;
	private TileFactory tileFactory;

	public Map() {
		tiles = new HashMap<Integer, HashMap<Integer, Tile>>();
		tileFactory = new TileFactory();
	}

	public void initMap(String map) {
		FileHandle file = Gdx.files.internal(map);
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
		int ts = getTileSize();
		for (int x = 0; x < getMapWidth(); x++) {
			for (int y = 0; y < getMapHeight(); y++) {
				batch.draw(tileFactory.getTileTexture(getTile(x, y).getTileID()), x * ts, y * ts);
			}
		}
	}
	
	public List<Vector2> getBackgroundTiles() {
		List<Vector2> lst = new ArrayList<Vector2>();
		for (int x = 0; x < getMapWidth(); x++) {
			for (int y = 0; y < getMapHeight(); y++) {
				if(!getTile(x, y).isSolid())
					lst.add(new Vector2(x, y));
			}
		}
		return lst;
	}

	public void explode(Entity e) {
		BombAIComponent ai = e.getAs(BombAIComponent.class);
		PositionComponent pc = e.getAs(PositionComponent.class);
		if(ai == null || pc == null) return;
		
		Vector2 tilePos = getTilePositionWithAbsolutePosition(pc.x, pc.y);
		int tx = (int)tilePos.x, ty = (int)tilePos.y;
		for(int x = tx - ai.explodeSize +1; x < tx + ai.explodeSize; x++) {
			Tile t = getTile(x, ty);
			if(t != null && t.isExplosable()) {
				tiles.get(x).put(ty, tileFactory.getTileWithID(TileIDs.TILE_GROUND));
			}
		}
		for(int y = ty - ai.explodeSize +1; y < ty + ai.explodeSize; y++) {
			Tile t = getTile(tx, y);
			if(t != null && t.isExplosable()) {
				tiles.get(tx).put(y, tileFactory.getTileWithID(TileIDs.TILE_GROUND));
			}
		}
	}
	
	public boolean canWalk(float absx, float absy) {
		Tile t = getTileWithAbsolutePosition(absx, absy);
		if(t == null) return false;
		return !t.isSolid();
	}

	public Tile getTile(int x, int y) {
		if (x >= 0 && x < getMapWidth() && y >= 0 && y < getMapHeight())
			return tiles.get(x).get(y);
		else
			return null;
	}
	
	public Vector2 getTilePositionWithAbsolutePosition(float absx, float absy) {
		int ts = getTileSize();
		return new Vector2((int)(absx / ts), (int)(absy / ts));
	}
	
	public Tile getTileWithAbsolutePosition(float absx, float absy) {
		Vector2 tp = getTilePositionWithAbsolutePosition(absx, absy);
		return getTile((int)tp.x, (int)tp.y);
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
	
	public int getAbsoluteWidth() {
		return getMapWidth() * getTileSize();
	}

	public int getAbsoluteHeight() {
		return getMapHeight() * getTileSize();
	}

	public void dispose() {
		tileFactory.dispose();
	}
}
