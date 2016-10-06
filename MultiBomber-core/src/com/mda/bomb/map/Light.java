package com.mda.bomb.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Light {
	private Vector2 pos;

	private Vector3 shade;
	private int size;
	
	public Light(Vector2 pos) {
		this(pos, new Vector3(1, 1, 1));
	}
	
	public Light(Vector2 pos, Vector3 shade) {
		this(pos, shade, 5);
	}
	
	public Light(Vector2 pos, Vector3 shade, int size) {
		this.pos = pos;
		this.shade = shade;
		this.size = size;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public Vector3 getShade() {
		return shade;
	}

	public void setShade(Vector3 shade) {
		this.shade = shade;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
