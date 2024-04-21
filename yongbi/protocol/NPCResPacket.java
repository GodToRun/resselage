package yongbi.protocol;

import java.util.ArrayList;

public class NPCResPacket extends Packet {
	private static final long serialVersionUID = 213L;
	public int resNum;
	public NPCResPacket() {
		packetType = Packet.NPCRES_TYPE;
	}
}
