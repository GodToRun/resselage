package yongbi.protocol;

import java.io.Serializable;

public class UserInfo implements Serializable {

	public String uid_send;
	public int locX_get;
	public int locY_get;
	public int actNum_get;
	public int xdir = 0, ydir = 0;
	public boolean udelay;
	public int hp_get, maxhp_get;
	public int mp_get, maxmp_get;
	public int phdef_get, mgdef_get;
	public int lvl_get;
	public int str_get;
	public int dex_get;
	public int int_get;
	public int look_get;
	public String level_get;
	public int exp_get;
	public int ap_get;
	public int strap_get;
	public int dexap_get;
	public int intap_get;
	public int sp_get;
	public int cls_get;
	private static final long serialVersionUID = 209L;

}
