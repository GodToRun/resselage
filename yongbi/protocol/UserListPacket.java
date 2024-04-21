package yongbi.protocol;

import java.util.ArrayList;

public class UserListPacket extends Packet {
	private static final long serialVersionUID = 207L;
	public ArrayList<String> uids = new ArrayList<>();
	public byte result = 0;
	public UserListPacket() {
		packetType = USERLIST_TYPE;
	}
}
