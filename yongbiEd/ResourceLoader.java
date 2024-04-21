package yongbiEd;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
// image extension must be .png format.
public class ResourceLoader {
	public static final String game = "srrn/";
	public static final String tiles = game + "tiles/";
	public static final String objects = game + "objs/";
	public static final String entities = game + "entities/";
	public static final String gg_prefix = "gg";
	private static Hashtable<String, BufferedImage> imageTable = new Hashtable<>();
	public static BufferedImage get(String code) {
		if (code == null) return null;
		return imageTable.get(code);
	}
	public static void load() {
		try {
			imageTable.put("grass.tile", ImageIO.read(new File(tiles + "grass.png")));
			imageTable.put("grass.bottom", ImageIO.read(new File(tiles + "grass_bottom.png")));
			imageTable.put("grass.top", ImageIO.read(new File(tiles + "grass_top.png")));
			imageTable.put("grass.left", ImageIO.read(new File(tiles + "grass_left.png")));
			imageTable.put("grass.right", ImageIO.read(new File(tiles + "grass_right.png")));
			
			imageTable.put("dirthill.tile", ImageIO.read(new File(tiles + "dirthill.png")));
			imageTable.put("dirthill.left", ImageIO.read(new File(tiles + "dirthill_left.png")));
			
			imageTable.put("dirt.tile", ImageIO.read(new File(tiles + "dirt.png")));
			imageTable.put("dirt.bottom", ImageIO.read(new File(tiles + "dirt_bottom.png")));
			imageTable.put("dirt.top", ImageIO.read(new File(tiles + "dirt_top.png")));
			imageTable.put("dirt.left", ImageIO.read(new File(tiles + "dirt_left.png")));
			imageTable.put("dirt.right", ImageIO.read(new File(tiles + "dirt_right.png")));
			
			imageTable.put("dirt2.tile", ImageIO.read(new File(tiles + "dirt2.png")));
			imageTable.put("dirt2.bottom", ImageIO.read(new File(tiles + "dirt2_bottom.png")));
			imageTable.put("dirt2.top", ImageIO.read(new File(tiles + "dirt2_top.png")));
			imageTable.put("dirt2.left", ImageIO.read(new File(tiles + "dirt2_left.png")));
			imageTable.put("dirt2.right", ImageIO.read(new File(tiles + "dirt2_right.png")));
			
			imageTable.put("dirt3.tile", ImageIO.read(new File(tiles + "dirt3.png")));
			imageTable.put("dirt3.bottom", ImageIO.read(new File(tiles + "dirt3_bottom.png")));
			imageTable.put("dirt3.top", ImageIO.read(new File(tiles + "dirt3_top.png")));
			imageTable.put("dirt3.left", ImageIO.read(new File(tiles + "dirt3_left.png")));
			imageTable.put("dirt3.right", ImageIO.read(new File(tiles + "dirt3_right.png")));
			
			imageTable.put("oak.tile", ImageIO.read(new File(tiles + "oak.png")));
			
			imageTable.put("water.tile", ImageIO.read(new File(tiles + "water.png")));
			imageTable.put("water.top", ImageIO.read(new File(tiles + "water_top.png")));
			imageTable.put("water.bottom", ImageIO.read(new File(tiles + "water_bottom.png")));
			imageTable.put("water.left", ImageIO.read(new File(tiles + "water_left.png")));
			imageTable.put("water.right", ImageIO.read(new File(tiles + "water_right.png")));
			
			imageTable.put("su.tile", ImageIO.read(new File(tiles + "su.png")));
			imageTable.put("su.bottom", ImageIO.read(new File(tiles + "su_bottom.png")));
			imageTable.put("su.top", ImageIO.read(new File(tiles + "su_top.png")));
			
			imageTable.put("su2.tile", ImageIO.read(new File(tiles + "su2.png")));
			imageTable.put("su2.left", ImageIO.read(new File(tiles + "su2_left.png")));
			imageTable.put("su2.right", ImageIO.read(new File(tiles + "su2_right.png")));
			imageTable.put("su2.bottom", ImageIO.read(new File(tiles + "su2_bottom.png")));
			
			imageTable.put("obj.stairs", ImageIO.read(new File(objects + "stairs.png")));
			imageTable.put("obj.tree", ImageIO.read(new File(objects + "tree.png")));
			imageTable.put("obj.rock", ImageIO.read(new File(objects + "rock.png")));
			imageTable.put("obj.house0", ImageIO.read(new File(objects + "house0.png")));
			imageTable.put("obj.house1", ImageIO.read(new File(objects + "house1.png")));
			imageTable.put("obj.gwang", ImageIO.read(new File(objects + "gwang.png")));
			
			for (int i = 0; i < 2; i++)
				imageTable.put("entity." + gg_prefix + "t" + i, ImageIO.read(new File(entities + gg_prefix + "t" + i + ".png")));	
			for (int i = 0; i < 2; i++)
				imageTable.put("entity." + gg_prefix + "r" + i, ImageIO.read(new File(entities + gg_prefix + "r" + i + ".png")));
			for (int i = 0; i < 2; i++)
				imageTable.put("entity." + gg_prefix + "b" + i, ImageIO.read(new File(entities + gg_prefix + "b" + i + ".png")));
		} catch (IOException e) { e.printStackTrace(); }
	}
}
