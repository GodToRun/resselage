package yongbi.protocol;

public class LoadInfoPacket extends Packet {
	public String mapname;
	public int locx, locy;
	public int actNum;
	public int xdir = 0, ydir = 0;
	public byte result = 0;
	private static final long serialVersionUID = 203L;
}
