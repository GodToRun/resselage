package yongbi.protocol;

public class SendChatPacket extends Packet {
	private static final long serialVersionUID = 206L;
	public ChatElement ce;
	public SendChatPacket() {
		packetType = SENDCHAT_TYPE;
	}
}
