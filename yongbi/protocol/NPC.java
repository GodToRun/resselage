package yongbi.protocol;

import java.util.ArrayList;

import yongbi.ts.NPCInfo;

public class NPC {
	public static ArrayList<NPC> npcs = new ArrayList<>();
	private String code, name, script;
	static {
		npcs.add(new NPC("sera", "세라", "begin.py"));
		npcs.add(new NPC("renowik", "레노 윅", "begin2.py"));
		npcs.add(new NPC("joshu", "조슈", "begin3.py"));
		npcs.add(new NPC("vialon", "비아론", "clsthief.py"));
		npcs.add(new NPC("pildon", "필돈", "clswarrior.py"));
		npcs.add(new NPC("olomon", "올로몬", "clsmagician.py"));
		npcs.add(new NPC("june", "주네 아주머니", "shopha.py"));
		npcs.add(new NPC("dorn", "도르닌", "clsthief.py"));
		npcs.add(new NPC("walf", "월프", "clsmagician.py"));
	}
	public NPC(String code, String name, String script) {
		setCode(code);
		setScript(script);
		setName(name);
	}
	public static NPC findNPCByInfo(NPCInfo i) {
		for (NPC npc : npcs) {
			if (npc.getCode().equals(i.npcCode)) return npc;
		}
		return null;
	}
	public static NPC findNPC(String code) {
		for (NPC npc : npcs) {
			if (npc.getCode().equals(code)) {
				return npc;
			}
		}
		return null;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
}
