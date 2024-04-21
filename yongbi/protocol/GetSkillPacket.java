package yongbi.protocol;

import java.util.ArrayList;

public class GetSkillPacket extends Packet {
	private static final long serialVersionUID = 221L;
	public ArrayList<String> skills = new ArrayList<>();
	public GetSkillPacket() {
		packetType = GETSKILLS_TYPE;
	}
}
