package yongbi.client;

import javax.swing.JPanel;

public class Player extends Humanoid {
	private static final long serialVersionUID = 62413L;
	public String id, mapname;
	public int x, y;
	public boolean hit = false;
	public double hitTick = 0.0D;
	public Player() {
		super("탐험가", "user", null);
		
	}
	
}
