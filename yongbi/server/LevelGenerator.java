package yongbi.server;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;

import pw.common.AABB;
import pw.common.GameObject;
import pw.common.ObjectType;
import yongbi.protocol.MobInfo;
import yongbi.ts.EntityInfo;
import yongbi.ts.EtcInfo;
import yongbi.ts.Tile;
import yongbi.ts.TsFile;

public class LevelGenerator {
	public Level generate(Level level, String name) {
		//AABB.aabbs.clear();
		String path = "srrn/level/" + name;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)));
			TsFile file = (TsFile)ois.readObject();
			for (EtcInfo etc : file.tiles.etcs) {
				if (etc.key.equals("aabb")) {
					String[] sp = etc.value.split(",");
					new AABB(Integer.parseInt(sp[0]),Integer.parseInt(sp[1]),Integer.parseInt(sp[2]),Integer.parseInt(sp[3])).tag = name;
				}
			}
			level.tiles = file.tiles.tiles;
			level.portals = file.tiles.portals;
			level.npcs = file.tiles.npcs;
			for (EntityInfo einf : file.tiles.entities) {
				for (MobInfo minf : MobInfo.monBases) {
					if (einf.entName.equals(minf.mobName)) {
						MobInfo mi = new MobInfo(minf);
						for (int i = 0; i < einf.maxSpawn; i++)
							level.spawnEntities.add(mi);
						break;
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return level;
	}
}
