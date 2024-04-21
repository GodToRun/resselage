package yongbi.protocol;

import java.util.ArrayList;

public class AbilityPacket extends Packet {
	private static final long serialVersionUID = 261L;
	public int addSTR, addDEX, addINT;
	public AbilityPacket() {
		packetType = Packet.ABILITY_TYPE;
	}
}
