package yongbi.protocol;

import java.awt.Graphics;

import yongbi.client.Game;
import yongbi.client.ResourceLoader;

public class HumanAvater extends Avater {
	public Part bg, hair, face, armor, weapon, helm, helmBack;
	double pi = 0.0D;
	int fi = 0;
	private static final long serialVersionUID = 306L;
	@Override
	public void action(int actNum) {
		// TODO Auto-generated method stub
		
	}
	void renderPart(Graphics g, Part part) {
		renderPart(g, part, 0, 0);
	}
	void renderPart(Graphics g, Part part, int xoff, int yoff) {
		if (part == null) return;
		if (part.getLength() == 0)
			g.drawImage(ResourceLoader.get(part.getPrefix()), x+xoff, y+yoff, null);
		else {
			g.drawImage(ResourceLoader.get(part.getPrefix() + part.i), x+xoff, y+yoff, null);
		}
	}

	@Override
	public void render(Graphics g) {
		renderPart(g, bg, -10, -13);
		renderPart(g, helmBack);
		g.drawImage(ResourceLoader.get("female.body"), x, y, null);
		renderPart(g, armor);
		g.drawImage(ResourceLoader.get("female.armor.frame"), x, y, null);
		if (face != null)
			g.drawImage(ResourceLoader.get(face.getPrefix() + fi), x, y, null);
		renderPart(g, hair);
		renderPart(g, helm);
		renderPart(g, weapon);
	}

	@Override
	public void update(Game game, double delta) {
		if (helm != null)
		helm.update(delta);
		if (helmBack != null)
		helmBack.update(delta);
		if (weapon != null)
		weapon.update(delta);
		if (hair != null)
		hair.update(delta);
		if (bg != null)
		bg.update(delta);
		if (armor != null)
		armor.update(delta);
		pi += delta;
		if (pi > 3.1f) {
			fi = 0;
			pi = 0.0f;
		}
		else if (pi > 3f) {
			fi = 1;
		}
	}

}
