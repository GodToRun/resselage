package yongbi.ts;

import java.io.Serializable;

public class PortalInfo implements Serializable {
	private static final long serialVersionUID = 10L;
	public TsPoint point;
	public String depMap, depID;
	public String arrMap, arrID;
	public int xdir = 0, ydir = 0;
	public int reqLevel, reqLook;
}
