package yongbi.protocol;

import yongbi.server.Server;

public class NearAttackAI extends MobAI {
	private static final long serialVersionUID = 226L;
	public NearAttackAI(MobInfo mob) {
		super(mob);
	}
	@Override
	public MobAI copy() {
		return new NearAttackAI(getMob());
	}
	@Override
	public void update(Server server, double delta) {
		super.update(server, delta);
	}

}
