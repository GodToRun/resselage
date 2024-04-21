package yongbi.protocol;

public class GetInvPacket extends Packet {
	private static final long serialVersionUID = 216L;
	public String uid;
	public Inventory inv;
	public GetInvPacket() {
		packetType = GETINV_TYPE;
	}
}
