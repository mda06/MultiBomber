package com.mda.bomb.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mda.bomb.ecs.components.BombAIComponent;
import com.mda.bomb.ecs.components.PositionComponent;
import com.mda.bomb.ecs.core.Entity;
import com.mda.bomb.map.tile.Tile;
import com.mda.bomb.map.tile.TileFactory;
import com.mda.bomb.map.tile.TileIDs;
import com.mda.bomb.util.MathUtil;

public class Map {
	// x,y = tile
	private HashMap<Integer, HashMap<Integer, Tile>> tiles;
	private TileFactory tileFactory;
	private HashMap<Integer, HashMap<Integer, Float>> lightShading;

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
		for (String line : textFileSplit) {
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

		initLights();
	}

	private void initLights() {
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector2(6, 3)));
		lights.add(new Light(new Vector2(1, 1)));

		initLightShadings(lights);
	}

	public void render(SpriteBatch batch) {
		int ts = getTileSize();
		Color oldColor = batch.getColor();
		for (int x = 0; x < getMapWidth(); x++) {
			for (int y = 0; y < getMapHeight(); y++) {
				//float shading = lightShading.get(x).get(y);
				float shading = 1;
				
				batch.setColor(oldColor.r * shading, oldColor.g * shading, oldColor.b * shading, oldColor.a);
				batch.draw(tileFactory.getTileTexture(getTile(x, y).getTileID()), x * ts, y * ts);
			}
		}
		batch.setColor(oldColor);
	}

	public void initLightShadings(List<Light> lights) {
		lightShading = new HashMap<Integer, HashMap<Integer, Float>>();
		for (int x = 0; x < getMapWidth(); x++)
			lightShading.put(x, new HashMap<Integer, Float>());

		float baseValue = .3f;
		for (int x = 0; x < getMapWidth(); x++) {
			for (int y = 0; y < getMapHeight(); y++) {
				lightShading.get(x).put(y, 0f);
			}
		}

		for (Light light : lights) {
			Vector2 pos = light.getPos();
			int size = light.getSize();
			int mod = size % 2;
			for (int x = (int) pos.x - size / 2 - mod; x < pos.x + size / 2 + mod + 1; x++) {
				if (!(x >= 0 && x < getMapWidth())) continue;
				for (int y = (int) pos.y - size / 2 - mod; y < pos.y + size / 2 + mod + 1; y++) {
					if (!(y >= 0 && y < getMapHeight())) continue;

					int distX = MathUtil.absMin((int) pos.x, x);
					int distY = MathUtil.absMin((int) pos.y, y);
					int dist = (int) MathUtils.clamp(distX + distY, 0, light.getSize());

					float shading = MathUtil.map(dist, 0, light.getSize(), 1, 0);
					float extShade = lightShading.get(x).get(y);
					if(extShade != 0) {
						shading += extShade;
					}
					
					if (shading > 1) shading = 1;

					lightShading.get(x).put(y, shading);
				}
			}
		}

		for (int x = 0; x < getMapWidth(); x++) {
			for (int y = 0; y < getMapHeight(); y++) {
				//if (lightShading.get(x).get(y) == 0) 
					lightShading.get(x).put(y, MathUtils.clamp(lightShading.get(x).get(y) + baseValue, 0, 1));
			}
		}
	}

	public List<Vector2> getBackgroundTiles() {
		List<Vector2> lst = new ArrayList<Vector2>();
		for (int x = 0; x < getMapWidth(); x++) {
			for (int y = 0; y < getMapHeight(); y++) {
				if (!getTile(x, y).isSolid()) lst.add(new Vector2(x, y));
			}
		}
		return lst;
	}

	public void explode(Entity e) {
		BombAIComponent ai = e.getAs(BombAIComponent.class);
		PositionComponent pc = e.getAs(PositionComponent.class);
		if (ai == null || pc == null) return;

		Vector2 tilePos = getTilePositionWithAbsolutePosition(pc.x, pc.y);
		int tx = (int) tilePos.x, ty = (int) tilePos.y;
		for (int x = tx - ai.explodeSize + 1; x < tx + ai.explodeSize; x++) {
			Tile t = getTile(x, ty);
			if (t != null && t.isExplosable()) {
				tiles.get(x).put(ty, tileFactory.getTileWithID(TileIDs.TILE_GROUND));
			}
		}
		for (int y = ty - ai.explodeSize + 1; y < ty + ai.explodeSize; y++) {
			Tile t = getTile(tx, y);
			if (t != null && t.isExplosable()) {
				tiles.get(tx).put(y, tileFactory.getTileWithID(TileIDs.TILE_GROUND));
			}
		}
	}

	public boolean canWalk(float absx, float absy) {
		Tile t = getTileWithAbsolutePosition(absx, absy);
		if (t == null) return false;
		return !t.isSolid();
	}

	public Tile getTile(int x, int y) {
		if (x >= 0 && x < getMapWidth() && y >= 0 && y < getMapHeight()) return tiles.get(x).get(y);
		else return null;
	}

	public Vector2 getTilePositionWithAbsolutePosition(float absx, float absy) {
		int ts = getTileSize();
		return new Vector2((int) (absx / ts), (int) (absy / ts));
	}

	public Tile getTileWithAbsolutePosition(float absx, float absy) {
		Vector2 tp = getTilePositionWithAbsolutePosition(absx, absy);
		return getTile((int) tp.x, (int) tp.y);
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
