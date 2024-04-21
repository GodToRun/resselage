package yongbi.ts;

import java.io.Serializable;

public class Tile implements Serializable {
	static final long serialVersionUID = 3L;
	public int x, y;
	public int type;
	public String shape;
	public boolean hasCollider = true;
}