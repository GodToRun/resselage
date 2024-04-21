package yongbi.protocol;

public class SigninPacket extends Packet {
	private static final long serialVersionUID = 202L;
	public String id, pw;
	public byte result = 0;
}
