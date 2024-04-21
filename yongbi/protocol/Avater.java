package yongbi.protocol;

import java.awt.Graphics;
import java.io.Serializable;

import yongbi.client.Game;

public abstract class Avater implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 305L;
	public int x = 0, y = 0;
	protected int actNum;
	public abstract void action(int actNum);
	public abstract void render(Graphics g);
	public abstract void update(Game game, double delta);
}
