package yongbi.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import yongbi.protocol.AbilityPacket;
import yongbi.protocol.ChatElement;
import yongbi.protocol.ChatPacket;
import yongbi.protocol.ClassType;
import yongbi.protocol.EffectProtocol;
import yongbi.protocol.GetEffectPacket;
import yongbi.protocol.GetInvPacket;
import yongbi.protocol.GetMonsterPacket;
import yongbi.protocol.GetNPCConPacket;
import yongbi.protocol.GetSkillPacket;
import yongbi.protocol.InvItem;
import yongbi.protocol.Inventory;
import yongbi.protocol.ItemInfo;
import yongbi.protocol.MobInfo;
import yongbi.protocol.MovePacket;
import yongbi.protocol.NPC;
import yongbi.protocol.NPCResPacket;
import yongbi.protocol.Packet;
import yongbi.protocol.PartType;
import yongbi.protocol.SendChatPacket;
import yongbi.protocol.SigninPacket;
import yongbi.protocol.SignupPacket;
import yongbi.protocol.SkillIns;
import yongbi.protocol.SkillInses;
import yongbi.protocol.TalkNPCPacket;
import yongbi.protocol.UseItemPacket;
import yongbi.protocol.UseSkillPacket;
import yongbi.protocol.UserInfo;
import yongbi.protocol.UserInfoPacket;
import yongbi.protocol.UserListPacket;
import yongbi.server.script.ScriptManager;
import yongbi.ts.NPCInfo;
import yongbi.ts.PortalInfo;
import yongbi.ts.Tile;

public class ServerInstance extends Thread {
	public static final String url = "jdbc:oracle:thin:@localhost:1521:XE";
	Socket client;
	Connection con;
	public Server rootServer;
	public String userid = "";
	public int resNum = -1;
	double ht = 0;
	public GetNPCConPacket npcCon = new GetNPCConPacket();
	double mt = 0.0D;
	// dyn packet struct per uid
	public ServerInstance(Server server, Socket client) {
		this.rootServer = server;
		this.client = client;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, "system", "tiger");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	InputStream saveInv(String id) {
		Inventory inv = rootServer.uinvTable.get(id);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject((Object)inv);
		  out.flush();
		  ByteArrayInputStream binput = new ByteArrayInputStream(bos.toByteArray());
		  return binput;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		  try {
		    bos.close();
		  } catch (IOException ex) {
		  }
		}
		return null;
	}
	InputStream saveSkills(String id) {
		SkillInses si = rootServer.usTable.get(id);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject((Object)si);
		  out.flush();
		  ByteArrayInputStream binput = new ByteArrayInputStream(bos.toByteArray());
		  return binput;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		  try {
		    bos.close();
		  } catch (IOException ex) {
		  }
		}
		return null;
	}
	SkillInses loadSkill(InputStream input) {
		SkillInses i = null;
		if (input == null) {
			i = new SkillInses();
		}
		else {
			ObjectInput in = null;
			try {
			  in = new ObjectInputStream(input);
			  i = (SkillInses)in.readObject();
			  if (i == null) i = new SkillInses();
			} catch (IOException | ClassNotFoundException ex) {
			    ex.printStackTrace();
			}
			finally {
			    if (in != null) {
			      try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			    }
			}
		}
		bl:
		for (SkillStruct ss : SkillStruct.bases) {
			for (SkillIns in : i.si) {
				if (in.skillID.equals(ss.name)) {
					continue bl;
				}
			}
			SkillIns ins = new SkillIns();
			ins.skillID = ss.name;
			i.si.add(ins);
		}
		return i;
		    
	}
	Inventory loadInv(InputStream input) {
		if (input == null) {
			return new Inventory();
		}
		ObjectInput in = null;
		try {
		  in = new ObjectInputStream(input);
		  Inventory i = (Inventory)in.readObject();
		  if (i == null) i = new Inventory();
		  return i;
		} catch (IOException | ClassNotFoundException ex) {
		    ex.printStackTrace();
		}
		finally {
		    if (in != null) {
		      try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    }
		}
		return new Inventory();
		    
	}
	
	long lastTime, curTime;
	double upd = 0;
	public void addInv(ItemInfo item, int count) {
		if (count == 0) return;
		rootServer.chats.add(new ChatElement(item.getRealName() + "(을)를 " + count + "개 획득하였습니다.", true));
		ArrayList<InvItem> items = rootServer.uinvTable.get(userid).items;
		for (InvItem i : items) {
			if (i.itemName.equals(item.getName())) {
				i.count += count;
				return;
			}
		}
		InvItem i = new InvItem();
		i.count = count;
		i.itemName = item.getName();
		items.add(i);
		
	}
	boolean collCheck(UserInfo ui) {
		for (Tile tile : rootServer.levels.get(ui.level_get).tiles) {
			if (tile.x == ui.locX_get && tile.y == ui.locY_get) return false;
		}
		return true;
	}
	public InvItem getEquiped(PartType pt) {
		Inventory inv = rootServer.uinvTable.get(userid);
		if (inv != null) {
			for (InvItem item : inv.items) {
				if (!item.equiped) continue;
				if (rootServer.findItemInfo(item).getPt() == pt) return item;
			}
		}
		return null;
	}
	boolean cmt() {
		if (mt > 0.8D) {
			mt = 0.0D;
			return true;
		}
		return false;
	}
	void right(UserInfo uinfo) {
		if (!cmt()) return;
		uinfo.locX_get++;
		if (!collCheck(uinfo)) uinfo.locX_get--;
	}
	void left(UserInfo uinfo) {
		if (!cmt()) return;
		uinfo.locX_get--;
		if (!collCheck(uinfo)) uinfo.locX_get++;
	}
	void up(UserInfo uinfo) {
		if (!cmt()) return;
		uinfo.locY_get--;
		if (!collCheck(uinfo)) uinfo.locY_get++;
	}
	void down(UserInfo uinfo) {
		if (!cmt()) return;
		uinfo.locY_get++;
		if (!collCheck(uinfo)) uinfo.locY_get--;
	}
	void parseChat(ChatElement ce) {
		if (!ce.sender.equals(userid)) return;
		UserInfo uinfo = rootServer.uiTable.get(userid);
		if (uinfo.udelay) return;
		for (SkillStruct ss : SkillStruct.bases) {
			if (ss.msg.equals(ce.contents)) {
				useSkill(ss.msg, 0);
				return;
			}
		}
		//System.out.println(ce.contents);
		if (ce.contents.equals("동")) {
			right(uinfo);
			return;
		}
		if (ce.contents.equals("서")) {
			left(uinfo);
			return;
		}
		if (ce.contents.equals("북")) {
			up(uinfo);
			return;
		}
		if (ce.contents.equals("남")) {
			down(uinfo);
			return;
		}
		String[] sp = ce.contents.split(" ");
		if (sp.length > 1 && (sp[1].equals("입어") || sp[1].equals("벗어"))) {
			Inventory inv = rootServer.uinvTable.get(userid);
			for (InvItem item : inv.items) {
				ItemInfo info = rootServer.findItemInfo(item);
				if (info.getRealName().equals(sp[0])) {
					if (sp[1].equals("입어")) rootServer.useItem(uinfo, item);
					else if (sp[1].equals("벗어")) rootServer.unequipItem(item);
					break;
				}
			}
			return;
		}
		String[] split = ce.contents.split(" ");
		if (split.length >= 3 && split[split.length-1].equals("줘") && split[1].contains("개")) {
			for (ItemInfo item : ItemInfo.bases) {
				if (split[0].equals(item.getRealName())) {
					addInv(item, Integer.parseInt(split[1].replace("개", "")));
					return;
				}
			}
		}
	}
	void statAdd(UserInfo usr, InvItem item) {
		ItemInfo info = rootServer.findItemInfo(item);
		if (info == null) return;
		usr.str_get += info.getStr() + item.strUp;
		usr.dex_get += info.getDex() + item.dexUp;
		usr.int_get += info.getInt() + item.intUp;
		usr.look_get += info.getLook() + item.lookUp;
		usr.maxhp_get += info.getHp() + item.hpUp;
		usr.maxmp_get += info.getMp() + item.mpUp;
		usr.phdef_get += info.getPhDef() + item.phDefUp;
		usr.mgdef_get += info.getMgDef() + item.mgDefUp;
	}
	public int needExp() {
		return (int)(1.2f * Math.pow(rootServer.uiTable.get(userid).lvl_get - 1, 3) + 1);
	}
	public void hp(int i) {
		UserInfo usr = rootServer.uiTable.get(userid);
		if (i+usr.hp_get > usr.maxhp_get) i = usr.maxhp_get-usr.hp_get;
		usr.hp_get += i;
	}
	public void mp(int i) {
		UserInfo usr = rootServer.uiTable.get(userid);
		if (i+usr.mp_get > usr.maxmp_get) i = usr.maxmp_get-usr.mp_get;
		usr.mp_get += i;
	}
	void levelUp(UserInfo usr) {
		usr.lvl_get++;
		usr.ap_get += 2;
		usr.sp_get++;
	}
	void update() {
		if (userid == null) return;
		lastTime = System.currentTimeMillis();
		long diff = lastTime - curTime;
		double delta = ((double)diff) / 1000D;
		curTime = lastTime;
		upd += delta;
		UserInfo usr = rootServer.uiTable.get(userid);
		Inventory inv = rootServer.uinvTable.get(userid);
		mt += delta;
		ht += delta;
		if (ht > 10) {
			ht = 0;
			hp(1);
			mp(1);
		}
		int a = 5;
		int m = 3;
		if (usr.cls_get >= ClassType.WARRIOR && usr.cls_get <= ClassType.WARRIOREND) a = 10;
		if (usr.cls_get >= ClassType.MAGICIAN && usr.cls_get <= ClassType.MAGICIANEND) m = 6;
		usr.str_get = usr.strap_get + 1;
		usr.dex_get = usr.dexap_get + 1;
		usr.int_get = usr.intap_get + 1;
		usr.look_get = 0;
		usr.maxhp_get = 5 + (usr.lvl_get-1) * a;
		usr.maxmp_get = 5 + (usr.lvl_get-1) * m;
		usr.phdef_get = 0;
		usr.mgdef_get = 0;
		//usr.lvl_get;
		InvItem arm = getEquiped(PartType.ARMOR);
		InvItem hlm = getEquiped(PartType.HELM);
		InvItem wpn = getEquiped(PartType.WEAPON);
		statAdd(usr, arm);
		statAdd(usr, hlm);
		statAdd(usr, wpn);
		if (usr.exp_get >= needExp()) {
			usr.exp_get = 0;
			levelUp(usr);
		}
		ArrayList<MobInfo> ms = rootServer.levels.get(usr.level_get).monsters;
		if (upd > 0.8f && usr != null) {
			try {
				PreparedStatement query = con.prepareStatement("UPDATE accounts set LOCX=?, LOCY=?, LVLNAME=?, INV=?, LVL=?, EXP=?, AP=?, SP=?, SKILLS=?, STRAP=?, DEXAP=?, INTAP=?, HP=?, MP=?, CLS=? WHERE ID=?");
				UserInfo uinfo = rootServer.uiTable.get(userid);
				query.setInt(1, uinfo.locX_get);
				query.setInt(2, uinfo.locY_get);
				query.setString(3, uinfo.level_get);
				query.setBinaryStream(4, saveInv(userid));
				query.setInt(5, uinfo.lvl_get);
				query.setInt(6, uinfo.exp_get);
				query.setInt(7, uinfo.ap_get);
				query.setInt(8, uinfo.sp_get);
				query.setBinaryStream(9, saveSkills(userid));
				query.setInt(10, uinfo.strap_get);
				query.setInt(11, uinfo.dexap_get);
				query.setInt(12, uinfo.intap_get);
				query.setInt(13, uinfo.hp_get);
				query.setInt(14, uinfo.mp_get);
				query.setInt(15, uinfo.cls_get);
				query.setString(16, userid);
				query.executeUpdate();
				query.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			upd = 0f;
		}
		if (userid != null) {
			UserInfo uinfo = usr;
			Level lvl = rootServer.levels.get(uinfo.level_get);
			int x = uinfo.locX_get;
			int y = uinfo.locY_get;
			int tmpX = uinfo.locX_get;
			int tmpY = uinfo.locY_get;
			String tmp = uinfo.level_get;
			for (PortalInfo pi : lvl.portals) {
				if (x == pi.point.x && y == pi.point.y) {
					//resetStream();
					uinfo.level_get = pi.arrMap; // switch!
					Level lvlAfter = rootServer.levels.get(uinfo.level_get);
					// find arrival portal's location!
					for (PortalInfo piAf : lvlAfter.portals) {
						if (piAf.depID.equals(pi.arrID)) {
							uinfo.locX_get = piAf.point.x + pi.xdir;
							uinfo.locY_get = piAf.point.y + pi.ydir;
							break;
						}
					}
					break;
				}
			}
		}
	}
	void useSkill(String skName, int sel) {
		UserInfo user = rootServer.uiTable.get(userid);
		if (user.udelay) return;
		String lvl = user.level_get;
		ArrayList<SkillStruct> al = rootServer.levels.get(lvl).skills;
		if (al == null) { al = new ArrayList<>(); }
		for (SkillStruct ss : SkillStruct.bases) {
			if (ss.msg.equals(skName)) {
				SkillStruct skill = new SkillStruct(0, 0, ss);
				skill.owner = user;
				if (skill.inPDelay) {
					user.udelay = true;
				}
				skill.uID = userid;
				skill.pos(rootServer);
				Level level = rootServer.levels.get(lvl);
				if (level.monsters.size() <= sel) return;
				skill.onActive(rootServer, level, user, level.monsters.get(sel));
				al.add(skill);
				break;
			}
		}
		rootServer.levels.get(lvl).skills = al;
	}
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	public synchronized void resetStream() {
		try {
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
		} catch (SocketException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public boolean block(String lvl, int x, int y) {
		return rootServer.block(lvl, x, y);
	}
	public boolean block(UserInfo ref) {
		return block(ref.level_get, ref.locX_get, ref.locY_get);
	}
	public void talkEnd() {
		npcCon.contents = "";
		npcCon.res.clear();
	}
	
	
	@Override
	public synchronized void run() {
		try {
			curTime = System.currentTimeMillis();
			while (true) {
				resetStream();
				Packet pkt = (Packet)ois.readObject();
				PreparedStatement query;
				switch (pkt.packetType) {
					case Packet.SINGUP_TYPE:
						SignupPacket signup = (SignupPacket)pkt;
						query = con.prepareStatement("INSERT INTO accounts (ID, NAME, PW, LVLNAME, LOCX, LOCY, LVL, EXP, AP, SP, GENDER,STRAP,DEXAP,INTAP) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						query.setString(1, signup.id.trim());
						query.setString(2, signup.name.trim());
						query.setString(3, signup.pw.trim());
						query.setString(4, "000.ts");
						query.setInt(5, 7);
						query.setInt(6, 12);
						query.setInt(7, 1);
						query.setInt(8, 0);
						query.setInt(9, 0);
						query.setInt(10, 0);
						query.setString(11, signup.gender.trim());
						query.setInt(12, 0);
						query.setInt(13, 0);
						query.setInt(14, 0);
						query.executeQuery();
						query.close();
						break;
					case Packet.SIGNIN_TYPE:
						SigninPacket signin = (SigninPacket)pkt;
						query = con.prepareStatement("SELECT * FROM accounts WHERE ID=? AND PW=?");
						query.setString(1, signin.id.trim());
						query.setString(2, signin.pw);
						ResultSet rs = query.executeQuery();
						signin.result = -1;
						if (rs.next()) {
							signin.result = 0;
							userid = signin.id;
							UserInfo info = new UserInfo();
							
							info.level_get = rs.getString("LVLNAME");
							info.locX_get = rs.getInt("LOCX");
							info.locY_get = rs.getInt("LOCY");
							info.lvl_get = rs.getInt("LVL");
							info.exp_get = rs.getInt("EXP");
							info.ap_get = rs.getInt("AP");
							info.sp_get = rs.getInt("SP");
							info.strap_get = rs.getInt("STRAP");
							info.dexap_get = rs.getInt("DEXAP");
							info.intap_get = rs.getInt("INTAP");
							info.hp_get = rs.getInt("HP");
							info.mp_get = rs.getInt("MP");
							info.cls_get = rs.getInt("CLS");
							info.uid_send = userid;
							Inventory inv = loadInv(rs.getBinaryStream("INV"));
							SkillInses si = loadSkill(rs.getBinaryStream("SKILLS"));
							rootServer.uinvTable.put(signin.id, inv);
							rootServer.usTable.put(signin.id, si);
							rootServer.uiTable.put(signin.id, info);
							rootServer.userIds.add(signin.id.trim());
							ChatElement welcome = new ChatElement();
							welcome.sender = "[서버]";
							welcome.contents = userid + "님 환영합니다";
							rootServer.chats.add(welcome);
						}
						oos.writeObject((Object)signin);
						query.close();
						break;
					case Packet.SENDCHAT_TYPE:
						SendChatPacket send = (SendChatPacket)pkt;
						rootServer.chats.add(send.ce);
						parseChat(send.ce);
						break;
					case Packet.CHATLIST_TYPE:
						try {
						ChatPacket chat = (ChatPacket)pkt;
						chat.chats = rootServer.chats;
						oos.writeObject((Object)chat);
						} catch (ConcurrentModificationException con) {} // Can't fix this error...
						break;
					case Packet.USERLIST_TYPE:
						UserListPacket list = (UserListPacket)pkt;
						list.uids = rootServer.userIds;
						oos.writeObject((Object)list);
						break;
					case Packet.GETINV_TYPE:
						GetInvPacket gi = (GetInvPacket)pkt;
						gi.inv = rootServer.uinvTable.get(gi.uid);
						oos.writeObject((Object)gi);
						break;
					case Packet.GETMONS_TYPE:
						GetMonsterPacket mpt = (GetMonsterPacket)pkt;
						mpt.monsters = rootServer.levels.get(mpt.level).monsters;
						oos.writeObject((Object)mpt);
						break;
					case Packet.USERINFO_TYPE:
						update();
						UserInfoPacket info = (UserInfoPacket)pkt;
						if (info.save) {
							rootServer.uiTable.get(info.ui.uid_send).actNum_get = info.ui.actNum_get;
							rootServer.uiTable.get(info.ui.uid_send).xdir = info.ui.xdir;
							rootServer.uiTable.get(info.ui.uid_send).ydir = info.ui.ydir;
						}
						else {
							info.ui = rootServer.uiTable.get(info.ui.uid_send);
							oos.writeObject((Object)info);
						}
						break;
					case Packet.GETEFFTS_TYPE:
						GetEffectPacket eff = (GetEffectPacket)pkt;
						ArrayList<EffectProtocol> efs = new ArrayList<>();
						if (rootServer.levels.get(eff.lvl).skills == null) rootServer.levels.get(eff.lvl).skills = new ArrayList<SkillStruct>();
						for (SkillStruct ss : rootServer.levels.get(eff.lvl).skills) {
							EffectProtocol ef = new EffectProtocol();
							ef.skillBase = ss.base + ss.cur;
							ef.user = ss.uID;
							efs.add(ef);
						}
						eff.efs = efs;
						oos.writeObject((Object)eff);
						break;
					case Packet.USESKILL_TYPE:
						UseSkillPacket sk = (UseSkillPacket)pkt;
						useSkill(sk.skillName, sk.mobi);
						ChatElement ce = new ChatElement();
						ce.chatType = 2;
						ce.sender = userid;
						UserInfo user = rootServer.uiTable.get(userid);
						ce.senderLevel = user.level_get;
						for (SkillStruct ss : SkillStruct.bases) {
							if (ss.name.equals(sk.skillName)) {
								ce.contents = ss.msg;
								rootServer.chats.add(ce);
								break;
							}
						}
						
						break;
					case Packet.TALKNPC_TYPE:
						TalkNPCPacket tp = (TalkNPCPacket)pkt;
						Level lvl = rootServer.levels.get(rootServer.uiTable.get(userid).level_get);
						NPCInfo npc = lvl.npcs.get(tp.npc);
						NPC npcDesc = NPC.findNPC(npc.npcCode);
						ScriptManager.run(this, npcDesc.getScript());
						break;
					case Packet.GETNPCCON_TYPE:
						oos.writeObject((Object)npcCon);
						break;
					case Packet.NPCRES_TYPE:
						NPCResPacket nrp = (NPCResPacket)pkt;
						resNum = nrp.resNum;
						break;
					case Packet.GETSKILLS_TYPE:
						GetSkillPacket skp = (GetSkillPacket)pkt;
						user = rootServer.uiTable.get(userid);
						skp.skills = new ArrayList<>();
						for (SkillStruct ss : SkillStruct.bases) {
							if (ss.getSkillListener() != null && user.cls_get >= ss.reqClsMin && user.cls_get <= ss.reqClsMax)
								skp.skills.add(ss.msg);
						}
						oos.writeObject((Object)skp);
						break;
					case Packet.ABILITY_TYPE:
						AbilityPacket ap = (AbilityPacket)pkt;
						int sum = ap.addSTR+ap.addDEX+ap.addINT;
						user = rootServer.uiTable.get(userid);
						if (sum <= user.ap_get) {
							user.ap_get -= sum;
							user.strap_get += ap.addSTR;
							user.dexap_get += ap.addDEX;
							user.intap_get += ap.addINT;
						}
						break;
					case Packet.USEITEM_TYPE:
						UseItemPacket uip = (UseItemPacket)pkt;
						rootServer.useItem(rootServer.uiTable.get(userid), rootServer.uinvTable.get(userid).items.get(uip.itemNum));
						break;
					case Packet.MOVE_TYPE:
						MovePacket mp = (MovePacket)pkt;
						user = rootServer.uiTable.get(userid);
						if (mp.xdir == 1) right(user);
						if (mp.xdir == -1) left(user);
						if (mp.ydir == -1) up(user);
						if (mp.ydir == 1) down(user);
						break;
				}
			}
		} catch (SocketException e) {
			
		}
		catch (IOException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			rootServer.uiTable.remove(userid);
			rootServer.userIds.remove(userid);
			System.out.println("Lost connection: client disconnected.");
			try {
				client.close();
				con.close();
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
}