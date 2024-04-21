package yongbi.server;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import pw.common.AABB;
import pw.common.GameObject;
import pw.component.Physics;
import yongbi.protocol.MobInfo;
import yongbi.server.SkillStruct;
import yongbi.ts.NPCInfo;
import yongbi.ts.PortalInfo;
import yongbi.ts.Tile;

public class Level {
	/*public BufferedImage merge;
	public int mergeX, mergeY;
	public ArrayList<Entity> entities = new ArrayList<>();*/
	public ArrayList<GameObject> footholds = new ArrayList<>();
	public ArrayList<SkillStruct> skills = new ArrayList<>();
	public ArrayList<PortalInfo> portals = new ArrayList<>();
	public ArrayList<MobInfo> monsters = new ArrayList<>();
	public ArrayList<Tile> tiles = new ArrayList<>();
	public ArrayList<NPCInfo> npcs = new ArrayList<>();
	public ArrayList<MobInfo> spawnEntities = new ArrayList<>();
	public double spawnTick = 0.0D;
	public Level(String mapname) {
		new LevelGenerator().generate(this, mapname);
	}
}
