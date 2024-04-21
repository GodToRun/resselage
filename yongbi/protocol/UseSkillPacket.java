package yongbi.protocol;

import java.util.ArrayList;

public class UseSkillPacket extends Packet {
	private static final long serialVersionUID = 212L;
	public byte result = 0;
	public String skillName;
	public int mobi;
	public UseSkillPacket() {
		packetType = Packet.USESKILL_TYPE;
	}
}
