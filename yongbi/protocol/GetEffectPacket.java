package yongbi.protocol;

import java.util.ArrayList;

public class GetEffectPacket extends Packet {
	private static final long serialVersionUID = 213L;
	public ArrayList<EffectProtocol> efs = new ArrayList<>();
	public String lvl;
	public GetEffectPacket() {
		packetType = Packet.GETEFFTS_TYPE;
	}
}
