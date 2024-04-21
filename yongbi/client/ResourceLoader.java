package yongbi.client;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
// image extension must be .png format.
public class ResourceLoader {
	public static final String game = "srrn/";
	public static final String tiles = game + "tiles/";
	public static final String bgs = game + "bgs/";
	public static final String females = game + "female/";
	public static final String body_prefix = "body";
	public static final String bouquet_prefix = "bouquet";
	public static final String umb_prefix = "umb";
	public static final String flowerhat_prefix = "flowerhat";
	public static final String wand_prefix = "wand";
	public static final String rine_prefix = "rine";
	public static final String knife_prefix = "knife";
	public static final String veil_prefix = "veil";
	public static final String weddingcrown_prefix = "weddingcrown";
	public static final String wedding_prefix = "wedding";
	public static final String richdress_prefix = "richdress";
	public static final String frame_prefix = "frame";
	public static final String face0_prefix = "face0";
	public static final String sera_prefix = "sera";
	public static final String under_prefix = "under";
	public static final String agile_prefix = "agile";
	public static final String hidesuit_prefix = "hidesuit";
	public static final String iron_prefix = "iron";
	public static final String rob_prefix = "rob";
	public static final String hair_prefix = "hair";
	public static final String gg_prefix = "gg";
	public static final String objects = game + "objs/";
	public static final String entities = game + "entities/";
	public static final String hairs = "hair/";
	public static final String faces = "face/";
	public static final String armors = "armor/";
	public static final String weapons = "weapon/";
	public static final String avbgs = females + "bgs/";
	public static final String helms = "helm/";
	public static final String uis = "ui/";
	public static final String hpguage = "hpbar";
	public static final String mpguage = "mpbar";
	public static final String incursor = "incursor";
	public static final String skills = game + "skill/";
	public static final String swing = "swing";
	public static final String doublestab = "doublestab";
	public static final String heartattack = "heartattack";
	public static final String strike = "strike";
	public static final String resselterror = "resselterror";
	public static final String resselup = "resselup";
	public static final String summdoor = "summdoor";
	public static final String icebeam = "icebeam";
	public static final String shadowstab = "shadowstab";
	public static final String ui = "ui";
	private static Hashtable<String, BufferedImage> imageTable = new Hashtable<>();
	static BufferedImage rotate(double rotDeg, BufferedImage image) {
		double rotationRequired = Math.toRadians(rotDeg);
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(image, null);
	}
	public static BufferedImage get(String code) {
		if (code == null) return null;
		return imageTable.get(code);
	}
	public static void load() {
		try {
			imageTable.put("female.hair." + hair_prefix, ImageIO.read(new File(females + hairs + hair_prefix + ".png")));
			for (int i = 0; i < 2; i++)
				imageTable.put("female.face." + face0_prefix + i, ImageIO.read(new File(females + faces + face0_prefix + i + ".png")));
			for (int i = 0; i < 17; i++)
				imageTable.put("bg." + i, ImageIO.read(new File(bgs + i + ".png")));
			
			imageTable.put("female." + body_prefix, ImageIO.read(new File(females + body_prefix + ".png")));
			
			imageTable.put("female.armor." + wedding_prefix, ImageIO.read(new File(females + armors + wedding_prefix + ".png")));
			imageTable.put("female.armor." + richdress_prefix, ImageIO.read(new File(females + armors + richdress_prefix + ".png")));
			imageTable.put("female.armor." + sera_prefix, ImageIO.read(new File(females + armors + sera_prefix + ".png")));
			imageTable.put("female.armor." + frame_prefix, ImageIO.read(new File(females + armors + frame_prefix + ".png")));
			imageTable.put("female.armor." + under_prefix, ImageIO.read(new File(females + armors + under_prefix + ".png")));
			imageTable.put("female.armor." + iron_prefix, ImageIO.read(new File(females + armors + iron_prefix + ".png")));
			imageTable.put("female.armor." + hidesuit_prefix, ImageIO.read(new File(females + armors + hidesuit_prefix + ".png")));
			imageTable.put("female.armor." + agile_prefix, ImageIO.read(new File(females + armors + agile_prefix + ".png")));
			imageTable.put("female.armor." + rob_prefix, ImageIO.read(new File(females + armors + rob_prefix + ".png")));
			
			imageTable.put("female.helm." + veil_prefix, ImageIO.read(new File(females + helms + veil_prefix + ".png")));
			imageTable.put("female.helm." + flowerhat_prefix, ImageIO.read(new File(females + helms + flowerhat_prefix + ".png")));
			imageTable.put("female.helm." + weddingcrown_prefix, ImageIO.read(new File(females + helms + weddingcrown_prefix + ".png")));
			
			imageTable.put("female.weapon." + bouquet_prefix, ImageIO.read(new File(females + weapons + bouquet_prefix + ".png")));
			imageTable.put("female.weapon." + umb_prefix, ImageIO.read(new File(females + weapons + umb_prefix + ".png")));
			imageTable.put("female.weapon." + rine_prefix, ImageIO.read(new File(females + weapons + rine_prefix + ".png")));
			imageTable.put("female.weapon." + wand_prefix, ImageIO.read(new File(females + weapons + wand_prefix + ".png")));
			imageTable.put("female.weapon." + knife_prefix, ImageIO.read(new File(females + weapons + knife_prefix + ".png")));
			
			for (int i = 0; i < 7; i++)
				imageTable.put("female.bgs.0" + i, ImageIO.read(new File(avbgs + "bg0" + i + ".png")));
			
			imageTable.put("ui.ui", ImageIO.read(new File(game + uis + ui + ".png")));
			imageTable.put("ui.cursor", ImageIO.read(new File(game + uis + "cursor" + ".png")));
			imageTable.put("ui.init", ImageIO.read(new File(game + uis + "init" + ".png")));
			imageTable.put("ui.upinfo", ImageIO.read(new File(game + uis + "upinfo" + ".png")));
			imageTable.put("ui." + hpguage, ImageIO.read(new File(game + uis + hpguage + ".png")));
			imageTable.put("ui." + mpguage, ImageIO.read(new File(game + uis + mpguage + ".png")));
			//imageTable.put("ui." + mpguage, ImageIO.read(new File(game + uis + mpguage + ".png")));
			
			imageTable.put("game." + incursor + ".t", rotate(0, ImageIO.read(new File(game + incursor + ".png"))));
			imageTable.put("game." + incursor + ".r", rotate(90, ImageIO.read(new File(game + incursor + ".png"))));
			imageTable.put("game." + incursor + ".l", rotate(-90, ImageIO.read(new File(game + incursor + ".png"))));
			imageTable.put("game." + incursor + ".b", rotate(180, ImageIO.read(new File(game + incursor + ".png"))));
			
			imageTable.put("ui.ele0", ImageIO.read(new File(game + uis + "ele0" + ".png")));
			imageTable.put("ui.ele1", ImageIO.read(new File(game + uis + "ele1" + ".png")));
			imageTable.put("ui.ele2", ImageIO.read(new File(game + uis + "ele2" + ".png")));
			
			for (int i = 0; i < 7; i++) {
				imageTable.put("skill.swing" + i, ImageIO.read(new File(skills + swing + i + ".png")));
			}
			for (int i = 0; i < 10; i++) {
				imageTable.put("skill.doublestab" + i, ImageIO.read(new File(skills + doublestab + i + ".png")));
				imageTable.put("skill.strike" + i, ImageIO.read(new File(skills + strike + i + ".png")));
			}
			for (int i = 0; i < 11; i++) {
				imageTable.put("skill.heartattack" + i, ImageIO.read(new File(skills + heartattack + i + ".png")));
			}
			for (int i = 0; i < 24; i++) {
				imageTable.put("skill.resselterror" + i, ImageIO.read(new File(skills + resselterror + i + ".png")));
			}
			for (int i = 0; i < 31; i++) {
				imageTable.put("skill.resselup" + i, ImageIO.read(new File(skills + resselup + i + ".png")));
			}
			for (int i = 0; i < 19; i++) {
				imageTable.put("skill.summdoor" + i, ImageIO.read(new File(skills + summdoor + i + ".png")));
			}
			for (int i = 0; i < 25; i++) {
				imageTable.put("skill.icebeam" + i, ImageIO.read(new File(skills + icebeam + i + ".png")));
			}
			for (int i = 0; i < 21; i++) {
				imageTable.put("skill.shadowstab" + i, ImageIO.read(new File(skills + shadowstab + i + ".png")));
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
}

