package yongbi.protocol;

public class UserInfoPacket extends Packet {
	private static final long serialVersionUID = 208L;
	public UserInfo ui;
	public boolean save = false;
	public UserInfoPacket() {
		ui = new UserInfo();
		packetType = USERINFO_TYPE;
	}
}
