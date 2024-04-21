package yongbi.client;

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
import yongbi.ts.EtcInfo;
import yongbi.ts.Tile;
import yongbi.ts.TsFile;

public class LevelGenerator {
	public Level generate(Level level, String name, JPanel panel) {
		String path = ResourceLoader.game + "level/" + name;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)));
			TsFile file = (TsFile)ois.readObject();
			level.bg = file.tiles.bg.base;
			level.tiles = file.tiles.tiles;
			level.name = file.tiles.name;
			level.npcs = file.tiles.npcs;
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return level;
	}
}
