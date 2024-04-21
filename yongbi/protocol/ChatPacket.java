package yongbi.protocol;

import java.util.ArrayList;

public class ChatPacket extends Packet {
	private static final long serialVersionUID = 204L;
	public ArrayList<ChatElement> chats = new ArrayList<>();
	public byte result = 0;
	public ChatPacket() {
		packetType = CHATLIST_TYPE;
	}
}
