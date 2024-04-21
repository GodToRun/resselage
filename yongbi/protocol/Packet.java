package yongbi.protocol;

import java.io.Serializable;

public class Packet implements Serializable {
	private static final long serialVersionUID = 200L;
	public static final int SINGUP_TYPE = 0,
			SIGNIN_TYPE = 1,
			SENDCHAT_TYPE = 2,
			CHATLIST_TYPE = 3,
			USERLIST_TYPE = 4,
			USERINFO_TYPE = 5,
			MOVE_TYPE = 6,
			USESKILL_TYPE = 7,
			GETEFFTS_TYPE = 8,
			GETINV_TYPE = 9,
			GETMONS_TYPE = 10,
			TALKNPC_TYPE = 11,
			GETNPCCON_TYPE = 12,
			NPCRES_TYPE = 13,
			GETSKILLS_TYPE = 14,
			ABILITY_TYPE = 15,
			USEITEM_TYPE = 16
			;
	public int packetType;
}
