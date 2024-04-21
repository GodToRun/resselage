package yongbi.protocol;

import java.util.ArrayList;

public class TalkNPCPacket extends Packet {
	private static final long serialVersionUID = 213L;
	public int npc;
	public TalkNPCPacket() {
		packetType = Packet.TALKNPC_TYPE;
	}
}
