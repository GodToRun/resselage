package yongbi.ts;

import java.io.Serializable;
import java.util.ArrayList;

public class Tiles implements Serializable {
	static final long serialVersionUID = 4L;
	public Background bg = new Background();
	public String name, description;
	public ArrayList<Tile> tiles = new ArrayList<Tile>();
	public ArrayList<NPCInfo> npcs = new ArrayList<NPCInfo>();
	public ArrayList<PortalInfo> portals = new ArrayList<PortalInfo>();
	public ArrayList<EtcInfo> etcs = new ArrayList<EtcInfo>();
	public ArrayList<EntityInfo> entities = new ArrayList<EntityInfo>();
}