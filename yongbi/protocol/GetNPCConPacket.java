package yongbi.protocol;

import java.util.ArrayList;

public class GetNPCConPacket extends Packet {
	private static final long serialVersionUID = 213L;
	public String contents;
	public ArrayList<String> res = new ArrayList<>();
	public GetNPCConPacket() {
		packetType = Packet.GETNPCCON_TYPE;
	}
}
