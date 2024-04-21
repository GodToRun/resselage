package yongbi.protocol;

import java.io.Serializable;
import java.util.ArrayList;

import yongbi.server.Level;
import yongbi.server.Server;
import yongbi.server.SkillStruct;

public class MobInfo implements Serializable {
	public boolean humanoid = false, died = false;
	public MobAI ai;
	public int ani = 0;
	public int hp = 4, mp = 4;
	int maxhp = 4, maxmp = 4;
	public int giveexp = 0;
	double tick = 0.0D;
	public int phDam = 1, mgDam = 0, phDef = 0, mgDef = 0;
	public int level = 1;
	public int damCount = 1;
	public String damMsg = "의 피해를 입었습니다.", deathMsg = "에게 살해당하였습니다.";
	public static final MobInfo GG,
	chick,
	darkbear,
	snailKing,
	pig,
	self,
	velo,
	radt,
	badt;
	public static ArrayList<MobInfo> monBases = new ArrayList<>();
	static {
		GG = new MobInfo("snail", "달팽이", new NearAttackAI(null));
		GG.phDam = 1;
		GG.giveexp = 1;
		GG.deathMsg = "에게 허무하게 당했습니다.";
		snailKing = new MobInfo("snailking", "달팽이왕", new NearAttackAI(null));
		snailKing.giveexp = 90;
		snailKing.phDam = 200;
		snailKing.level = 24;
		snailKing.hp = snailKing.maxhp = 1000;
		chick = new MobInfo("chick", "병아리", new NearAttackAI(null));
		chick.giveexp = 3;
		chick.level = 3;
		chick.maxhp = chick.hp = 20;
		chick.phDam = 2;
		chick.deathMsg = "의 부리에 쪼여 쓰러졌습니다.";
		pig = new MobInfo("pig", "돼지", new NearAttackAI(null));
		pig.giveexp = 5;
		pig.level = 6;
		pig.maxhp = pig.hp = 36;
		pig.phDam = 5;
		pig.damMsg = "만큼 돌진당했습니다.";
		pig.deathMsg = "에게 짓이기져 쓰러졌습니다.";
		self = new MobInfo("self", "숲요정", new NearAttackAI(null));
		self.giveexp = 9;
		self.level = 9;
		self.maxhp = self.hp = 45;
		self.mgDam = 5;
		self.mgDef = 2;
		self.damCount = 2;
		self.damMsg = "만큼 방울 당했습니다.";
		self.deathMsg = "에게 매혹돼 쓰러졌습니다.";
		darkbear = new MobInfo("darkbear", "어둠곰", new NearAttackAI(null));
		darkbear.giveexp = 24;
		darkbear.maxhp = darkbear.hp = 260;
		darkbear.phDam = 25;
		darkbear.phDef = 8;
		darkbear.level = 18;
		darkbear.deathMsg = "에게 어둠 속에서 물어뜯겨 사망하였습니다.";
		radt = new MobInfo("radt", "레드 어데타닌", new NearAttackAI(null));
		radt.giveexp = 70;
		radt.maxhp = radt.hp = 1500;
		radt.phDam = 20;
		radt.damCount = 2;
		radt.phDef = 35;
		radt.level = 35;
		radt.deathMsg = "에게 으스러져 살해당하였습니다.";
		badt = new MobInfo("badt", "블루 아우토스", new NearAttackAI(null));
		badt.giveexp = 80;
		badt.maxhp = badt.hp = 1600;
		badt.mgDam = 30;
		badt.mgDef = 35;
		badt.damCount = 2;
		badt.level = 40;
		badt.deathMsg = "에게 저주를 실현당했습니다.";
		velo = new MobInfo("velo", "벨루", new NearAttackAI(null));
		velo.giveexp = 2000;
		velo.maxhp = velo.hp = 80000;
		velo.phDam = 200;
		velo.mgDam = 100;
		velo.phDef = 70;
		velo.mgDef = 45;
		velo.level = 50;
		velo.damCount = 2;
		velo.damMsg = "만큼 할퀴어졌습니다.";
		velo.deathMsg = "에게 밟혀 압사당했습니다.";
		monBases.add(GG);
		monBases.add(chick);
		monBases.add(darkbear);
		monBases.add(snailKing);
		monBases.add(pig);
		monBases.add(self);
		monBases.add(velo);
		monBases.add(radt);
		monBases.add(badt);
	}
	public MobInfo(String name, String realname, MobAI ai) {
		this.mobName = name;
		this.realname = realname;
		this.ai = ai;
	}
	public MobInfo(MobInfo other) {
		this(other.mobName, other.realname, other.ai.copy());
		this.maxhp = other.maxhp;
		this.maxmp = other.maxmp;
		this.ai.setMob(this);
		this.humanoid = other.humanoid;
		this.shape = other.shape;
		this.symm = other.symm;
		this.hp = other.hp;
		this.mp = other.mp;
		this.giveexp = other.giveexp;
		this.phDam = other.phDam;
		this.mgDam = other.mgDam;
		this.level = other.level;
		this.lvl = other.lvl;
		this.damCount = other.damCount;
		this.phDef = other.phDef;
		this.mgDef = other.mgDef;
	}
	public void update(Server server, double delta) {
		died = hp <= 0;
		if (this.ai != null) {
			this.ai.update(server, delta);
		}
	}
	public void damageUser(Server server, UserInfo user, Level lvl, int amount) {
		ArrayList<SkillStruct> ss = server.getPassives(user);
		for (SkillStruct s : ss) {
			amount = s.getPassiveListener().onGotDam(user, server, s, this, amount);
		}
		ChatElement ce = new ChatElement();
		ce.contents = realname + "으로부터 " + amount + damMsg;
		if (amount == 0)
			ce.contents = "피해를 회피했습니다.";
		ce.sender = "[피격]";
		ce.showTo = user.uid_send;
		server.chats.add(ce);
		server.damageUser(this, user, amount, deathMsg);
	}
	public void attack(Server server, UserInfo user, Level lvl) {
		for (int i = 0; i < damCount; i++) {
			if (phDam != 0)
				damageUser(server, user, lvl, Server.calcDef(phDam, user.phdef_get));
			if (mgDam != 0)
				damageUser(server, user, lvl, mgDam);
		}
	}
	public MobInfo findBase() {
		for (MobInfo m : monBases) {
			if (m.mobName.equals(this.mobName)) return m;
		}
		return null;
	}
	public void damage(Server server, UserInfo user, Level lvl, int amount, boolean isPhys) {
		ArrayList<SkillStruct> ss = server.getPassives(user);
		for (SkillStruct s : ss) {
			s.getPassiveListener().beforeDeal(user, server, s, this, amount);
		}
		for (SkillStruct s : ss) {
			amount = s.getPassiveListener().onDeal(user, server, s, this, amount);
		}
		ChatElement ce = new ChatElement();
		ce.contents = realname + "에게 " + amount + "의 피해를 입혔습니다.";
		ce.sender = "[타격]";
		ce.showTo = user.uid_send;
		server.chats.add(ce);
		if (isPhys)
			hp -= Server.calcDef(amount, phDef);
		else
			hp -= Server.calcDef(amount, mgDef);
		for (SkillStruct s : ss) {
			s.getPassiveListener().afterDeal(user, server, s, this, amount);
		}
		if (hp <= 0 && lvl.monsters.contains(this)) {
			user.exp_get += giveexp;
			lvl.monsters.remove(this);
		}
	}
	private static final long serialVersionUID = 218L;
	public int xdir = 0, ydir = 1;
	public boolean symm;
	public String mobName, tex, realname, shape = "", lvl = "";
}
