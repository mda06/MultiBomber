package com.mda.bomb.map.tile;

public class Tile {
	private TileIDs id;
	
	public Tile(TileIDs id) {
		this.id = id;
	}
	
	public boolean isSolid() {
		return (id == TileIDs.TILE_SOLID) || (id == TileIDs.TILE_EXPLOSABLE);
	}
	
	public boolean isExplosable() {
		return id == TileIDs.TILE_EXPLOSABLE;
	}
	
	public TileIDs getTileID() {
		return id;
	}
}
