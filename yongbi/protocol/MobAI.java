package yongbi.protocol;

import java.io.Serializable;
import java.util.Random;

import yongbi.server.Server;

public class MobAI implements Serializable {
	private static final long serialVersionUID = 225L;
	private MobInfo mob;
	int speed = 2;
	double change = 0.0D;
	double move = 0.0D;
	double tick = 0.0D;
	public MobAI(MobInfo mob) {
		this.setMob(mob);
	}
	
	public void setMob(MobInfo mob) {
		this.mob = mob;
	}
	public MobInfo getMob() {
		return this.mob;
	}
	
	public MobAI copy() {
		return new MobAI(getMob());
	}
	
 	public void update(Server server, double delta) {
 		MobInfo mob = getMob();
 		if (mob.died) return;
	}
}
