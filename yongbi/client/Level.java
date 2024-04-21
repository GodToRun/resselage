package yongbi.client;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import pw.common.AABB;
import pw.common.GameObject;
import pw.component.Physics;
import yongbi.ts.NPCInfo;
import yongbi.ts.Tile;

public class Level {
	public String bg, name;
	public ArrayList<Tile> tiles = new ArrayList<>();
	public ArrayList<NPCInfo> npcs = new ArrayList<>();
	
	public Level(String mapname, JPanel panel) {
		new LevelGenerator().generate(this, mapname, panel);
	}
}
