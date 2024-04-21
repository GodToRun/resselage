package yongbi.protocol;

import java.io.Serializable;

public class Part implements Serializable {
	private static final long serialVersionUID = 219L;
	public static final Part
	HAIR
	,WEDDING
	,COTTON
	,ROB
	,SERA
	,HIDESUIT
	,IRON
	,AGILE
	,FACE
	,UNDER
	,VEIL
	,BOUQUET
	,WEDDINGCROWN
	,KNIFE
	,RINE
	,WAND
	,RICHDRESS
	,UMBRELLA
	,FLOWERHAT
	,AVBG0
	;
	static {
		HAIR = new Part("female.hair.hair");
		FACE = new Part("female.face.face0");
		IRON = new Part("female.hair.iron");
		WEDDING = new Part("female.armor.wedding");
		RICHDRESS = new Part("female.armor.richdress");
		COTTON = new Part("female.armor.cotton");
		SERA = new Part("female.armor.sera");
		ROB = new Part("female.armor.rob");
		UNDER = new Part("female.armor.under");
		HIDESUIT = new Part("female.armor.hidesuit");
		AGILE = new Part("female.armor.agile");
		VEIL = new Part("female.helm.veil");
		FLOWERHAT = new Part("female.helm.flowerhat");
		WEDDINGCROWN = new Part("female.helm.weddingcrown");
		BOUQUET = new Part("female.weapon.bouquet");
		KNIFE = new Part("female.weapon.knife");
		RINE = new Part("female.weapon.rine");
		WAND = new Part("female.weapon.wand");
		UMBRELLA = new Part("female.weapon.umb");
		AVBG0 = new Part("female.bgs.0");
		AVBG0.setDelay(0.25);
		AVBG0.setLength(7);
	}
	public int i = 0;
	public double tick = 0.0D;
	private String prefix;
	private int length = 0;
	private double delay = 0.2;
	public Part(String prefix) {
		this.setPrefix(prefix);
	}
	public void update(double dt) {
		tick += dt;
		if (tick >= delay) {
			i++;
			tick = 0.0D;
			if (i >= getLength()) i = 0;
		}
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public double getDelay() {
		return delay;
	}
	public void setDelay(double delay) {
		this.delay = delay;
	}
}
