package yongbi.protocol;

import java.util.ArrayList;

public class UseItemPacket extends Packet {
	private static final long serialVersionUID = 260L;
	public int itemNum;
	public UseItemPacket() {
		packetType = Packet.USEITEM_TYPE;
	}
}
