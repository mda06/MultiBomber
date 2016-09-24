package com.mda.bomb.map.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TileFactory {
	private Texture textGround, textSolid, textExplosable;
	private Tile groundTile, solidTile, explosableTile;
	
	public TileFactory() {
		groundTile = new Tile(TileIDs.TILE_GROUND);
		solidTile = new Tile(TileIDs.TILE_SOLID);
		explosableTile = new Tile(TileIDs.TILE_EXPLOSABLE);
	}

	public Texture getTileTexture(TileIDs tile) {
		switch (tile) {
		case TILE_GROUND:
			if(textGround == null)
				textGround = new Texture(Gdx.files.internal("Sprites/Blocks/BackgroundTile.png"));
			return textGround;
		case TILE_SOLID:
			if(textSolid == null)
				textSolid = new Texture(Gdx.files.internal("Sprites/Blocks/SolidBlock.png"));
			return textSolid;
		case TILE_EXPLOSABLE:
			if(textExplosable == null)
				textExplosable = new Texture(Gdx.files.internal("Sprites/Blocks/ExplodableBlock.png"));
			return textExplosable;
		default:
			return null;
		}
	}
	
	public Tile getTileWithID(TileIDs id) {
		switch(id) {
		case TILE_GROUND:
			return groundTile;
		case TILE_SOLID:
			return solidTile;
		case TILE_EXPLOSABLE:
			return explosableTile;
		default:
			return null;
		}
	}
	
	public void dispose() {
		if(textGround != null) textGround.dispose();
		if(textSolid != null) textSolid.dispose();
		if(textExplosable != null) textExplosable.dispose();
	}
}
