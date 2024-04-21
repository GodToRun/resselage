package yongbi.server;

import yongbi.protocol.MobInfo;
import yongbi.protocol.UserInfo;

public interface SkillListener {
	public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo uinfo, MobInfo selMob);
}
