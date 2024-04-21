package yongbi.client;

import javax.swing.JPanel;

import yongbi.protocol.HumanAvater;
import yongbi.protocol.MobAI;
import yongbi.protocol.MobInfo;
import yongbi.protocol.Part;

public class Humanoid extends MobInfo {
	public Part hair, face, armor, weapon, helm, helmBack, bg;
	private static final long serialVersionUID = 62412L;
	public HumanAvater hm;
	public Humanoid(String name, String realname, MobAI ai) {
		super(name, realname, ai);
	}
	public void update() {
		hm.hair = hair;
		hm.face = face;
		hm.armor = armor;
		hm.weapon = weapon;
		hm.helm = helm;
		hm.helmBack = helmBack;
		hm.bg = bg;
	}
}
