package yongbi.server;

import java.util.ArrayList;

import yongbi.protocol.ClassType;
import yongbi.protocol.MobInfo;
import yongbi.protocol.UserInfo;

// ignore its name, its JUST effect.
public class SkillStruct {
	public int x, y, clips;
	public String base;
	public double tick = 0.0D;
	public double rtick = 0.0D;
	public double delay = 0.1D;
	public boolean inPDelay = false, followPlayer = false, resetAvaterA = true;
	public int cur = 0;
	public int width, height;
	public UserInfo owner;
	public String name = "";
	public String uID = "";
	public SkillStruct baseSk = null;
	public String msg = "";
	private SkillListener listen;
	private PassiveListener passive;
	public int reqClsMin, reqClsMax;
	int tag = 0;
	public static final SkillStruct
	HIT_B,
	DOUBLESTAB,
	HEARTATTACK,
	STRIKE,
	RESSELTERROR,
	RESSELUP,
	SUMMDOOR,
	DOUBLEUP,
	FASCINATION,
	ICEBEAM,
	SHADOWSTAB;
	public static ArrayList<SkillStruct> bases = new ArrayList<>();
	static int perc(int v, float a) {
		return (int)(v * a);
	}
	static {
		SkillListener none = new SkillListener() {
			@Override
			public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo user, MobInfo selMob) {
			}
		};
		SkillListener hit = new SkillListener() {
			@Override
			public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo user, MobInfo selMob) {
				selMob.damage(server, user, lvl, Server.calcDamage(user.str_get), true);
				selMob.attack(server, user, lvl);
			}
		};
		HIT_B = new SkillStruct(0, 0, "skill.swing", "hit_b", "찌르기", 7, 0.15D, true, true, 0, 0, hit);
		HIT_B.reqClsMin = ClassType.CITIZEN;
		HIT_B.reqClsMax = ClassType.END;
		DOUBLESTAB = new SkillStruct(0, 0, "skill.doublestab", "dbstab", "더블스탭", 10, 0.04D, true, true, 0, 0, new SkillListener() {
			
			@Override
			public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo uinfo, MobInfo selMob) {
				selMob.damage(server, uinfo, lvl, Server.calcDamage(uinfo.dex_get), true);
				selMob.attack(server, uinfo, lvl);
			}
		});
		DOUBLESTAB.reqClsMin = ClassType.THIEF;
		DOUBLESTAB.reqClsMax = ClassType.THIEFEND;
		STRIKE = new SkillStruct(0, 0, "skill.strike", "strke", "스트라이크", 10, 0.08D, true, true, 0, 0, new SkillListener() {
			
			@Override
			public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo uinfo, MobInfo selMob) {
				selMob.damage(server, uinfo, lvl, perc(Server.calcDamage(uinfo.str_get), 1.5f), true);
				selMob.attack(server, uinfo, lvl);
			}
		});
		STRIKE.reqClsMin = ClassType.WARRIOR;
		STRIKE.reqClsMax = ClassType.WARRIOREND;
		HEARTATTACK = new SkillStruct(0, 0, "skill.heartattack", "htattack", "하트어택", 11, 0.1D, true, true, 0, 0, new SkillListener() {
			
			@Override
			public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo uinfo, MobInfo selMob) {
				selMob.damage(server, uinfo, lvl, perc(Server.calcDamage(uinfo.int_get), 1.5f), false);
				selMob.attack(server, uinfo, lvl);
			}
		});
		HEARTATTACK.reqClsMin = ClassType.MAGICIAN;
		HEARTATTACK.reqClsMax = ClassType.MAGICIANEND;
		RESSELTERROR = new SkillStruct(0, 0, "skill.resselterror", "rstr", "레셀 익스플로전", 24, 0.12D, true, true, 0, 0, new SkillListener() {
			
			@Override
			public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo uinfo, MobInfo selMob) {
				int dd = Server.calcDamage(uinfo.dex_get) / 4;
				int ressel = 200;
				
				selMob.damage(server, uinfo, lvl, perc((1 + dd) * ressel, 0.25f), true);
				selMob.attack(server, uinfo, lvl);
			}
		});
		RESSELTERROR.reqClsMin = ClassType.ASSASSIN;
		RESSELTERROR.reqClsMax = ClassType.THIEFEND;
		RESSELUP = new SkillStruct(0, 0, "skill.resselup", "rsup", "레셀 업", 31, 0.08D, true, true, 0, 0, new SkillListener() {
			
			@Override
			public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo uinfo, MobInfo selMob) {
				
			}
		});
		RESSELUP.reqClsMin = ClassType.ASSASSIN;
		RESSELUP.reqClsMax = ClassType.THIEFEND;
		SUMMDOOR = new SkillStruct(0, 0, "skill.summdoor", "smdr", "서먼 도어", 19, 0.09D, true, true, 0, 0, new SkillListener() {
			
			@Override
			public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo uinfo, MobInfo selMob) {
				
			}
		});
		SUMMDOOR.reqClsMin = ClassType.CLERICK;
		SUMMDOOR.reqClsMax = ClassType.CLERICK;
		DOUBLEUP = new SkillStruct(0, 0, "", "dbup", "더블업", 0, 0, true, true, 0, 0, new PassiveListener() {
			
			@Override
			public void onLevelup(UserInfo usr, Server server, SkillStruct self) {}
			
			@Override
			public int onDeal(UserInfo usr, Server server, SkillStruct self, MobInfo sel, int dam) {
				if (Math.random() < 0.05f) {
					return dam * 2;
				}
				return dam;
			}

			@Override
			public int onGotDam(UserInfo victim, Server server, SkillStruct self, MobInfo attacker, int dam) {
				return dam;
			}

			@Override
			public void beforeDeal(UserInfo usr, Server server, SkillStruct self, MobInfo sel, int dam) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterDeal(UserInfo usr, Server server, SkillStruct self, MobInfo sel, int dam) {
				// TODO Auto-generated method stub
				
			}
		});
		DOUBLEUP.reqClsMin = ClassType.THIEF;
		DOUBLEUP.reqClsMax = ClassType.THIEFEND;
		FASCINATION = new SkillStruct(0, 0, "", "fasc", "매혹", 0, 0, true, true, 0, 0, new PassiveListener() {
			
			@Override
			public void onLevelup(UserInfo usr, Server server, SkillStruct self) {}
			
			@Override
			public int onDeal(UserInfo usr, Server server, SkillStruct self, MobInfo sel, int dam) {return dam;}

			@Override
			public int onGotDam(UserInfo victim, Server server, SkillStruct self, MobInfo attacker, int dam) {
				int look = (int)(Math.pow(victim.look_get, 1.4f) * 2.3f) + victim.lvl_get / 20;
				int atLevel = attacker.level;
				if ((int)((0.4+Math.random()) * look) > atLevel) {
					dam = 0;
				}
				
				return dam;
			}

			@Override
			public void beforeDeal(UserInfo usr, Server server, SkillStruct self, MobInfo sel, int dam) {
				sel.phDef -= usr.look_get * 2;
				if (sel.phDef < 0) sel.phDef = 0;
			}

			@Override
			public void afterDeal(UserInfo usr, Server server, SkillStruct self, MobInfo sel, int dam) {
				sel.phDef = sel.findBase().phDef;
			}
		});
		FASCINATION.reqClsMin = ClassType.ASSASSIN;
		FASCINATION.reqClsMax = ClassType.THIEFEND;
		ICEBEAM = new SkillStruct(0, 0, "skill.icebeam", "icbeam", "아이스 빔", 25, 0.09D, true, true, 0, 0, new SkillListener() {
			
			@Override
			public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo uinfo, MobInfo selMob) {
				int dd = Server.calcDamage(uinfo.int_get);
				
				selMob.damage(server, uinfo, lvl, perc(dd, 1.6f), true);
				selMob.attack(server, uinfo, lvl);
			}
		});
		ICEBEAM.reqClsMin = ClassType.MAGE;
		ICEBEAM.reqClsMax = ClassType.MAGE;
		SHADOWSTAB = new SkillStruct(0, 0, "skill.shadowstab", "shdstb", "쉐도우 스탭", 21, 0.08D, true, true, 0, 0, new SkillListener() {
			
			@Override
			public void onActive(SkillStruct ss, Server server, Level lvl, UserInfo uinfo, MobInfo selMob) {
				int dd = Server.calcDamage(uinfo.dex_get);
				
				selMob.damage(server, uinfo, lvl, perc(dd, 2.7f), true);
				selMob.attack(server, uinfo, lvl);
			}
		});
		SHADOWSTAB.reqClsMin = ClassType.ASSASSIN;
		SHADOWSTAB.reqClsMax = ClassType.THIEFEND;
		bases.add(HIT_B);
		bases.add(DOUBLESTAB);
		bases.add(HEARTATTACK);
		bases.add(STRIKE);
		bases.add(RESSELTERROR);
		bases.add(RESSELUP);
		bases.add(SUMMDOOR);
		bases.add(DOUBLEUP);
		bases.add(FASCINATION);
		bases.add(ICEBEAM);
		bases.add(SHADOWSTAB);
	}
	public void onActive(Server srv, Level lvl, UserInfo uinfo, MobInfo sel) {
		if (listen != null)
			listen.onActive(this, srv, lvl, uinfo, sel);
	}
	public void pos(Server srv) {
		int px = 0, py = 0;
		if (uID != null && srv.uiTable.get(uID) != null) {
			px = srv.uiTable.get(uID).locX_get;
			py = srv.uiTable.get(uID).locY_get;
			//System.out.println(px + ", " + py + " " + followPlayer);
		}
		int xQ = x;
		int yQ = y;
		if (followPlayer) {
			xQ = baseSk.x + px;
			yQ = baseSk.y + py;
		}
		x = xQ;
		y = yQ;
	}
	public SkillListener getSkillListener() {
		return this.listen;
	}
	public PassiveListener getPassiveListener() {
		return this.passive;
	}
	public SkillStruct(int x, int y, SkillStruct baseSS) {
		this(x, y, baseSS.base, baseSS.name, baseSS.msg, baseSS.clips, baseSS.delay, baseSS.inPDelay, baseSS.followPlayer, baseSS.width, baseSS.height, baseSS.listen, baseSS.passive);
		this.baseSk = baseSS;
		this.resetAvaterA = baseSS.resetAvaterA;
		this.owner = baseSS.owner;
		this.reqClsMin = baseSS.reqClsMin;
		this.reqClsMax = baseSS.reqClsMax;
	}
	public SkillStruct(int x, int y, String base, String name, String msg, int clips, double delay, boolean inPDelay, boolean followPlayer, int w, int h, SkillListener listen) {
		this(x, y, base, name, msg, clips, delay, inPDelay, followPlayer, w, h, listen, null);
	}
	public SkillStruct(int x, int y, String base, String name, String msg, int clips, double delay, boolean inPDelay, boolean followPlayer, int w, int h, PassiveListener passive) {
		this(x, y, base, name, msg, clips, delay, inPDelay, followPlayer, w, h, null, passive);
	}
	public SkillStruct(int x, int y, String base, String name, String msg, int clips, double delay, boolean inPDelay, boolean followPlayer, int w, int h, SkillListener listen, PassiveListener passive) {
		this.msg = msg;
		this.x = x;
		this.y = y;
		this.clips = clips;
		this.followPlayer = followPlayer;
		this.base = base;
		this.name = name;
		this.delay = delay;
		this.inPDelay = inPDelay;
		this.width = w;
		this.height = h;
		this.listen = listen;
		this.passive = passive;
	}
}
