package yongbi.server;

import yongbi.protocol.MobInfo;
import yongbi.protocol.UserInfo;

public interface PassiveListener {
	public void onLevelup(UserInfo usr, Server server, SkillStruct self);
	public void beforeDeal(UserInfo usr, Server server, SkillStruct self, MobInfo sel, int dam);
	public void afterDeal(UserInfo usr, Server server, SkillStruct self, MobInfo sel, int dam);
	public int onDeal(UserInfo usr, Server server, SkillStruct self, MobInfo sel, int dam);
	public int onGotDam(UserInfo victim, Server server, SkillStruct self, MobInfo attacker, int dam);
}
