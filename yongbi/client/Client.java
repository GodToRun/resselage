package yongbi.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

import yongbi.protocol.AbilityPacket;
import yongbi.protocol.ChatElement;
import yongbi.protocol.ChatPacket;
import yongbi.protocol.EffectProtocol;
import yongbi.protocol.GetEffectPacket;
import yongbi.protocol.GetInvPacket;
import yongbi.protocol.GetMonsterPacket;
import yongbi.protocol.GetNPCConPacket;
import yongbi.protocol.GetSkillPacket;
import yongbi.protocol.Inventory;
import yongbi.protocol.MobInfo;
import yongbi.protocol.MovePacket;
import yongbi.protocol.NPCResPacket;
import yongbi.protocol.Packet;
import yongbi.protocol.SendChatPacket;
import yongbi.protocol.SignupPacket;
import yongbi.protocol.TalkNPCPacket;
import yongbi.protocol.UseItemPacket;
import yongbi.protocol.UseSkillPacket;
import yongbi.protocol.UserInfo;
import yongbi.protocol.UserInfoPacket;
import yongbi.protocol.UserListPacket;
import yongbi.ts.TsPoint;

public class Client {
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	public String id;
	public Client() {
		
	}
	public synchronized void sendChat(ChatElement ele) {
		resetStream();
		SendChatPacket pkt = new SendChatPacket();
		try {
			pkt.ce = ele;
			oos.writeObject(pkt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void useSkill(String skillName, int i) {
		resetStream();
		UseSkillPacket pkt = new UseSkillPacket();
		pkt.skillName = skillName;
		pkt.mobi = i;
		try {
			oos.writeObject((Object)pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void useItem(int c) {
		resetStream();
		UseItemPacket pkt = new UseItemPacket();
		pkt.itemNum = c;
		try {
			oos.writeObject((Object)pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void useAP(int str, int dex, int inT) {
		resetStream();
		AbilityPacket pkt = new AbilityPacket();
		pkt.addSTR = str;
		pkt.addDEX = dex;
		pkt.addINT = inT;
		try {
			oos.writeObject((Object)pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void NPCres(int num) {
		resetStream();
		NPCResPacket pkt = new NPCResPacket();
		pkt.resNum = num;
		try {
			oos.writeObject((Object)pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void talkNPC(int npc) {
		resetStream();
		TalkNPCPacket pkt = new TalkNPCPacket();
		pkt.npc = npc;
		try {
			oos.writeObject((Object)pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized ArrayList<String> getSkills() {
		resetStream();
		GetSkillPacket pkt = new GetSkillPacket();
		try {
			oos.writeObject((Object)pkt);
			pkt = (GetSkillPacket)ois.readObject();
			return pkt.skills;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public synchronized GetNPCConPacket getNPCCon() {
		resetStream();
		GetNPCConPacket pkt = new GetNPCConPacket();
		try {
			oos.writeObject((Object)pkt);
			pkt = (GetNPCConPacket)ois.readObject();
			return pkt;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public synchronized ArrayList<MobInfo> getMonsterList(String lvl) {
		resetStream();
		GetMonsterPacket pkt = new GetMonsterPacket();
		pkt.level = lvl;
		try {
			oos.writeObject((Object)pkt);
			pkt = (GetMonsterPacket)ois.readObject();
			return pkt.monsters;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	public synchronized ArrayList<EffectProtocol> getSkillList(String lvl) {
		resetStream();
		GetEffectPacket pkt = new GetEffectPacket();
		pkt.lvl = lvl;
		try {
			oos.writeObject((Object)pkt);
			//resetStream();
			pkt = (GetEffectPacket)ois.readObject();
			return pkt.efs;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	public synchronized void saveUInfo(UserInfo ui) {
		resetStream();
		UserInfoPacket inf = new UserInfoPacket();
		inf.ui = ui;
		try {
			inf.save = true;
			oos.writeObject((Object)inf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized UserInfo getUInfo(String id) {
		resetStream();
		UserInfoPacket inf = new UserInfoPacket();
		inf.ui.uid_send = id;
		try {
			oos.writeObject((Object)inf);
			inf = (UserInfoPacket)ois.readObject();
			return inf.ui;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public synchronized void move(int xd, int yd) {
		resetStream();
		MovePacket move = new MovePacket();
		move.xdir = xd;
		move.ydir = yd;
		try {
			oos.writeObject(move);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void signup(String id, String gender, String name, String pw) {
		resetStream();
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(new String(pw).getBytes(StandardCharsets.UTF_8));
			
			SignupPacket packet = new SignupPacket();
			packet.packetType = Packet.SINGUP_TYPE;
			packet.id = id;
			packet.gender = gender;
			packet.name = name;
			packet.pw = new String(hash);
			oos.writeObject((Object)packet);
		} catch (NoSuchAlgorithmException | IOException e1) {
			e1.printStackTrace();
		}
	}
	public synchronized Inventory loadInv(String uid) {
		resetStream();
		GetInvPacket pkt = new GetInvPacket();
		pkt.uid = uid;
		try {
			oos.writeObject(pkt);
			pkt = (GetInvPacket)ois.readObject();
			Inventory ret = pkt.inv;
			return ret;
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Inventory();
	}
	public synchronized ArrayList<String> uidsList() {
		resetStream();
		UserListPacket pkt = new UserListPacket();
		try {
			oos.writeObject(pkt);
			pkt = (UserListPacket)ois.readObject();
			ArrayList<String> ret = pkt.uids;
			return new ArrayList<String>(Collections.unmodifiableList(ret));
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public synchronized ArrayList<ChatElement> chatList() {
		resetStream();
		ChatPacket pkt = new ChatPacket();
		try {
			oos.writeObject(pkt);
			pkt = (ChatPacket)ois.readObject();
			ArrayList<ChatElement> ret = pkt.chats;
			return new ArrayList<ChatElement>(Collections.unmodifiableList(ret));
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private synchronized void resetStream() {
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Client(Socket socket, String id) {
		this.socket = socket;
		this.id = id;
	}
	public Client(int port, String id) {
		socket = new Socket();
		this.id = id;
		try {
			socket.connect(new InetSocketAddress("localhost", port));
			//int rec = dis.readInt();
			//System.out.println("[CLIENT] recieved data " + rec + " from server");
			
			/*socket.getOutputStream().close();
			socket.getInputStream().close();
			socket.close();
			System.out.println("Socket closed.");*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
