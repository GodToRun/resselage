package yongbi.protocol;

import java.io.Serializable;
import java.util.ArrayList;

public class Inventory implements Serializable {
	public ArrayList<InvItem> items = new ArrayList<>();
	private static final long serialVersionUID = 217L;
}
