package yongbi.protocol;

import java.util.ArrayList;

public class GetMonsterPacket extends Packet {
	private static final long serialVersionUID = 221L;
	public String level;
	public ArrayList<MobInfo> monsters = new ArrayList<>();
	public GetMonsterPacket() {
		packetType = GETMONS_TYPE;
	}
}
