package com.mda.bomb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mda.bomb.MultiBomberMain;
import com.mda.bomb.network.MyServer;
import com.mda.bomb.network.ServerMessages;
import com.mda.bomb.util.Constants;
import com.mda.bomb.util.IPUtils;

public class ServerScreen implements Screen {

	@SuppressWarnings("unused")
	private MultiBomberMain main;
	
	private SpriteBatch batch;
	private MyServer server;
	private BitmapFont font;
	
	public ServerScreen(MultiBomberMain m) {
		main = m;
		batch = new SpriteBatch();
		font = new BitmapFont();
		server = new MyServer();
	}
	
	public void initEngine() {
		server.initEngine();
	}
	
	private void update(float dt) {
		server.updateEngine(dt);
	}
	
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.drawMultiLine(batch, "Info server\n   IP : " + IPUtils.getMyIP() + "\n   Port TCP : " + Constants.PORT_TCP + "\n   Port UDP : " + Constants.PORT_UDP, 0, 450);
		int y = 350;
		font.drawMultiLine(batch, "-------------------------------------\nServer activity", 0, y);
		y -= 2*font.getLineHeight();
		draw(ServerMessages.serverInfo, y, 80);
		y -= 80;
		font.drawMultiLine(batch, "-------------------------------------\nClient activity", 0, y);
		draw(ServerMessages.serverIncomming, y - 2*(int)font.getLineHeight(), 200 - 2*(int)font.getLineHeight());
		y -= 210;
		font.drawMultiLine(batch, "-------------------------------------", 0, y);
		batch.end();
	}
	
	private void draw(Array<String> msg, int y, int size) {
		int n = (int)(size / font.getLineHeight()) + 1;
		for(int i = 0; i < n; i++) {
			if(i * font.getLineHeight() > size) break;
			int index = msg.size - 1 - i;
			if(index < 0) break;
			font.draw(batch, msg.get(index), 15, y - i * font.getLineHeight());
		}
	}
	
	public void dispose() {
		batch.dispose();
		font.dispose();
		server.dispose();
	}

	public void resize(int width, int height) {}
	public void show() {}
	public void hide() {}
	public void pause() {}
	public void resume() {}
}
