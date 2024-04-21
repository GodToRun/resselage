package yongbi.server;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

import pw.common.AABB;
import pw.common.Camera;
import yongbi.protocol.*;
import yongbi.server.SkillStruct;
import yongbi.server.script.ScriptManager;
import yongbi.ts.NPCInfo;
import yongbi.ts.PortalInfo;
import yongbi.ts.Tile;

class ServerUpdate extends Thread {
	long curTime, lastTime;
	double notifyTick = 0.0D;
	Socket side;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	ArrayList<String> msgs = new ArrayList<>();
	public Server rootServer;
	public ServerUpdate(Server root) {
		this.rootServer = root;
		msgs.add("지나친 게임 이용은 건강을 해칠수 있습니다.");
		msgs.add("서버 포트는 " + GameProtocol.PORT + "입니다.");
		msgs.add("RETURN키로 채팅을 칠수 있습니다.");
		side = new Socket();
		try {
			side.connect(new InetSocketAddress("localhost", GameProtocol.PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		curTime = System.nanoTime();
	}
	
	private void resetStream() {
		try {
			oos = new ObjectOutputStream(side.getOutputStream());
			ois = new ObjectInputStream(side.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			lastTime = System.nanoTime();
			long diff = lastTime - curTime;
			curTime = lastTime;
			double delta = ((double)diff) / 1000000000D;
			//System.out.println(delta);
			int sz = rootServer.chats.size();
			for (int i = 0; i < sz; i++) {
				//sz = rootServer.chats.size();
				if (i < rootServer.chats.size())
					rootServer.chats.get(i).tick += delta;
				//sz = rootServer.chats.size();
			}
			notifyTick += delta;
			if (notifyTick > 90) {
				resetStream();
				SendChatPacket pkt = new SendChatPacket();
				try {
					pkt.ce = new ChatElement();
					pkt.ce.chatType = 1;
					pkt.ce.sender = "[서버]";
					pkt.ce.contents = msgs.get((int)(Math.random() * msgs.size()));
					oos.writeObject(pkt);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				notifyTick = 0;
			}
			rootServer.levels.forEach((k, v) -> {
				v.spawnTick += delta;
				if (v.spawnTick >= 10 && v.monsters.size() <= 0) {
					v.spawnTick = 0;
					for (MobInfo mi : v.spawnEntities) {
						MobInfo real = new MobInfo(mi);
						real.lvl = k;
						v.monsters.add(real);
					}
				}
				for (MobInfo mi : v.monsters) {
					mi.update(rootServer, delta);
				}
				SkillStruct ssRemove = null;
				for (SkillStruct ss : v.skills) {
					
					
					ss.tick += delta;
					ss.rtick += delta;
					if (ss.tick >= ss.delay) {
						ss.tick = 0;
						ss.cur++;
					}
					ss.pos(rootServer);
					if (ss.cur >= ss.clips) {
						ssRemove = ss;
						continue;
					}
				}
				if (ssRemove != null) {
					if (ssRemove.inPDelay && ssRemove.owner != null) ssRemove.owner.udelay = false;
					v.skills.remove(ssRemove);
				}
			});
		}
	}
}
public class Server {
	public ServerSocket socket;
	public ArrayList<String> userIds = new ArrayList<>();
	public ArrayList<ChatElement> chats = new ArrayList<>();
	public Hashtable<String, UserInfo> uiTable = new Hashtable<>();
	public Hashtable<String, Inventory> uinvTable = new Hashtable<>();
	public Hashtable<String, SkillInses> usTable = new Hashtable<>();
	public Hashtable<Level, ArrayList<AABB>> aabbTable = new Hashtable<>();
	public Hashtable<String, Level> levels = new Hashtable<>();
	double notifyTick = 0.0D;
	ServerUpdate update;
	void level(String lvl) {
		Level level = new Level(lvl);
		levels.put(lvl, level);
		aabbTable.put(level, new ArrayList<AABB>(AABB.aabbs));
		AABB.aabbs.clear();
	}
	void userDie(MobInfo att, String deathMsg, UserInfo vict) {
		ChatElement ce = new ChatElement();
		ce.contents = att.realname + deathMsg;
		ce.sender = "[사망]";
		ce.showTo = vict.uid_send;
		vict.exp_get = 0;
		this.chats.add(ce);
		vict.hp_get = vict.maxhp_get >= 10 ? 10 : 1;
	}
	public void damageUser(MobInfo att, UserInfo vict, int amount, String deathMsg) {
		vict.hp_get -= amount;
		if (vict.hp_get <= 0) userDie(att, deathMsg, vict);
	}
	public boolean block(String lvl, int x, int y) {
		ArrayList<AABB> aabbs = aabbTable.get(levels.get(lvl));
		for (AABB aabb : aabbs) {
			if (aabb.tag.equals(lvl) && aabb.getRect().intersects(new Rectangle(x+10, y+32, 28, 32))) {
				return true;
			}
		}
		return false;
	}
	public ItemInfo findItemInfo(InvItem i) {
		if (i == null) return null;
		for (ItemInfo info : ItemInfo.bases) {
			if (info.getName().equals(i.itemName)) {
				return info;
			}
		}
		return null;
	}
	public void incHP(UserInfo usr, int a) {
		usr.hp_get += a;
		if (usr.hp_get >= usr.maxhp_get) usr.hp_get = usr.maxhp_get;
	}
	public void incMP(UserInfo usr, int a) {
		usr.mp_get += a;
		if (usr.mp_get >= usr.maxmp_get) usr.mp_get = usr.maxmp_get;
	}
	public void removeInv(String userid, String code, int p) {
		InvItem r = null;
		ArrayList<InvItem> items = uinvTable.get(userid).items;
		for (InvItem i : items) {
			if (i.itemName.equals(code)) {
				i.count -= p;
				if (i.count <= 0) {
					r = i;
					break;
				}
			}
		}
		if (r != null) {
			items.remove(r);
		}
	}
	public void useItem(UserInfo usr, InvItem item) {
		ItemInfo info = findItemInfo(item);
		if (info.getPt().ordinal() >= PartType.WEAPON.ordinal()
				&& info.getPt().ordinal() <= PartType.BGS.ordinal()) {
				item.equiped = true;
		}
		else if (info.getPt() == PartType.POTION) {
			int hp = info.getTag2();
			int mp = info.getTag3();
			incHP(usr, hp);
			incMP(usr, mp);
			removeInv(usr.uid_send, item.itemName, 1);
		}
	}
	public void unequipItem(InvItem item) {
		ItemInfo info = findItemInfo(item);
		if (info.getPt().ordinal() >= PartType.WEAPON.ordinal()
				&& info.getPt().ordinal() <= PartType.BGS.ordinal()) {
				item.equiped = false;
		}
	}
	public static int calcDamage(int stat) {
		int max = (int)Math.pow(1.12 * stat, 1.3);
		int min = (int)Math.pow(0.9f * stat, 1.1);
		return min + (int)((max-min) * Math.random());
	}
	public static int calcDef(float dam, float def) {
		return (int)(dam * Math.pow(def + 1, 0.35f));
	}
	double distance(double x, double y, double x2, double y2) {
		double dx = x2-x;
		double dy = y2-y;
		double magnitude = Math.sqrt(dx * dx + dy * dy);
		return magnitude;
	}
	public ArrayList<SkillStruct> getPassives(UserInfo usr) {
		ArrayList<SkillStruct> skills = new ArrayList<>();
		for (SkillStruct ss : SkillStruct.bases) {
			if (ss.getPassiveListener() != null && usr.cls_get >= ss.reqClsMin && usr.cls_get <= ss.reqClsMax)
				skills.add(ss);
		}
		return skills;
	}
	public Server(int port) {
		try {
			for (int i = 0; i < 10; i++)
				level("00" + i + ".ts");
			for (int i = 10; i < 24; i++)
				level(i + ".ts");
			socket = new ServerSocket(port);
			if (!socket.isBound())
				socket.bind(new InetSocketAddress(port));
			System.out.println("socket created in port " + port);
			update = new ServerUpdate(this);
			update.start();
			while (true) {
				Socket client = socket.accept();
				System.out.println("Connected " + client.getLocalPort() + " Port From " + socket.getLocalSocketAddress());
				ServerInstance instance = new ServerInstance(this, client);
				instance.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Server(GameProtocol.PORT);
	}
}
