package yongbi.protocol;

import java.util.ArrayList;

public class MovePacket extends Packet {
	private static final long serialVersionUID = 210L;
	public int xdir = 0, ydir = 0;
	public byte result = 0;
	public MovePacket() {
		packetType = MOVE_TYPE;
	}
}
