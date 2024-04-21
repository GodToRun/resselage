package yongbi.protocol;

import java.io.Serializable;

public class InvItem implements Serializable {
	private static final long serialVersionUID = 217L;
	public String itemName;
	public int count;
	public boolean equiped = false;
	public int upgradeCount;
	public int strUp, dexUp, intUp, lookUp, hpUp, mpUp, phDefUp, mgDefUp, pro;
}
