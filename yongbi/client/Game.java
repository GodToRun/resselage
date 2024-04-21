package yongbi.client;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import pw.*;
import pw.common.Camera;
import pw.common.RegistryValue;
import pw.component.Physics;
import pw.kit.GameManager;
import yongbi.client.ResourceLoader;
import yongbi.protocol.ChatElement;
import yongbi.protocol.ClassType;
import yongbi.protocol.EffectProtocol;
import yongbi.protocol.GameProtocol;
import yongbi.protocol.GetNPCConPacket;
import yongbi.protocol.HumanAvater;
import yongbi.protocol.InvItem;
import yongbi.protocol.Inventory;
import yongbi.protocol.ItemInfo;
import yongbi.protocol.MobInfo;
import yongbi.protocol.NPC;
import yongbi.protocol.Packet;
import yongbi.protocol.Part;
import yongbi.protocol.PartType;
import yongbi.protocol.SigninPacket;
import yongbi.protocol.UserInfo;
import yongbi.ts.NPCInfo;
import yongbi.ts.Tile;
@SuppressWarnings("serial")
public class Game extends GameManager {
	public Player player;
	public float speed = 25f;
	public static final int PLAYER_LAYER = 2;
	public static final int MONSTER_LAYER = 3;
	public static final int GROUND_LAYER = 4;
	public static final int ATTACK_NEAR = 1;
	public static final int ATTACK_PROJECTILE = 2;
	public static final int ATTACK_MAGIC = 3;
	int pX = 0, pY = 0;
	ArrayList<ChatElement> chats = new ArrayList<>();
	ArrayList<EffectProtocol> efs = new ArrayList<>();
	public Level level;
	Client client;
	UserInfo info;
	double fixedTick = 0.0D;
	public static final double FDT = 0.06D;
	double fdtTick = 0.0D;
	boolean eqToggle = false;
	double hpMotion = 0.0D, mpMotion = 0.0D;
	double aniTick = 0.0D;
	int ani = 0;
	int scrollElement = 0;
	Font font;
	public ChatDialog chatDialog;
	public ArrayList<Element> elements = new ArrayList<>();
	//public ArrayList<MobInfo> monsters = new ArrayList<>();
	public ArrayList<MobInfo> mobs = new ArrayList<>();
	public ArrayList<Player> otherPlayers = new ArrayList<>();
	BufferedImage screen;
	static boolean fullScreen = true;
	static Point point = new Point(1*(640)+16, 1*(480)+38);
	int strStat, dexStat, intStat, lookStat;
	int eleMode = 0;
	String cur = "";
	int selNPC, selMonster;
	public boolean logedIn = false;
	Socket clientSocket = null;
	
	static {
		if (fullScreen) point = null;
	}
	Socket createSocket() {
		Socket client = new Socket();
		try {
			client.connect(new InetSocketAddress("localhost", GameProtocol.PORT));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return client;
	}
	void loginInit(Socket client, String id, String pw) {
		this.client = new Client(clientSocket, id);
		UserInfo info = this.client.getUInfo(id);
		player.mapname = info.level_get;
		player.id = id;
		level = new Level(player.mapname, getPanel());
		logedIn = true;
	}
	boolean login(Socket client, String id, String pw) {
		MessageDigest digest;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
			
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(new String(pw).getBytes(StandardCharsets.UTF_8));
			SigninPacket packet = new SigninPacket();
			packet.packetType = Packet.SIGNIN_TYPE;
			packet.id = id;
			packet.pw = new String(hash);
			oos.writeObject((Object)packet);
			packet = (SigninPacket)ois.readObject();
			if (packet.result >= 0) {
				System.out.println("[CLINT] Login success!");
				loginInit(client, id, pw);
				return true;
			}
			else System.out.println("[CLINT] Login failed." + packet.result);
		} catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) { e.printStackTrace(); }
		return false;
	}
	public Game() {
		super("ResselAge", point, fullScreen);
		screen = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		screen.createGraphics();
		//Point p = new Point(1*(640)+16, 1*(480)+38);
		clientSocket = createSocket();
		this.client = new Client(clientSocket, "");
		
		ResourceLoader.load();
		setCursorIcon(ResourceLoader.get("ui.cursor"), null);
		splashScreen = false;
		chatDialog = new ChatDialog();
		chatDialog.init(this);
		player = new Player();
		player.hm = new HumanAvater();
		player.hm.x = 22;
		player.hm.y = 15;
		player.hair = Part.HAIR;
		player.face = Part.FACE;
		player.armor = Part.WEDDING;
		player.update();
		player.id = "";
		float loading = 0f;
		setRegistryValue(RegistryValue.MSDELAY, "5");
		loading = System.currentTimeMillis();
		getPanel().setBackground(Color.BLACK);
		System.out.println("[ResourceLoader] resource loading done! elapsed time: " + (System.currentTimeMillis()-loading) + "ms");
		requestFocusInWindow();
		GraphicsEnvironment ge = null;
        font = new Font("굴림", Font.PLAIN, 12);
	}
	ItemInfo findItemInfo(InvItem i) {
		for (ItemInfo info : ItemInfo.bases) {
			if (info.getName().equals(i.itemName)) {
				return info;
			}
		}
		return null;
	}
	int tt = 0;
	String talkMsg = "";
	int tind = 0;
	public int needExp() {
		return (int)(1.2f * Math.pow(info.lvl_get - 1, 3) + 1);
	}
	void skillElement() {
		elements.clear();
		String str = "";
		char start = 'a';
		for (MobInfo m : mobs) {
			str += start + "-" + m.realname + "\n";
			start++;
		}
		int sz = mobs.size();
		if (sz == 0) sz = 1;
		elements.add(new Element(str, sz));
	}
	void selSkillElement() {
		elements.clear();
		String str = "";
		char start = 'a';
		ArrayList<String> sl = client.getSkills();
		for (String s : sl) {
			str += start + "-" + s + "\n";
			start++;
		}
		int sz = sl.size();
		if (sz == 0) sz = 1;
		elements.add(new Element(str, sz));
	}
	void talkingElement() {
		elements.clear();
		GetNPCConPacket con = client.getNPCCon();
		tt++;
		if (tt > 9 && tind < con.contents.length()) {
			talkMsg += con.contents.charAt(tind);
			tind++;
			tt = 0;
		}
		if (con != null) {
			String str = talkMsg;
			str += "\n";
			for (String r : con.res)
				str += "\n" + r;
			elements.add(new Element(str, str.split("\n").length-1));
		}
	}
	void talkElement() {
		elements.clear();
		String str = "대화 하기 \n";
		int i = 0;
		for (NPCInfo npc : level.npcs) {
			if (i == selNPC) str += "* ";
			str += NPC.findNPCByInfo(npc).getName();
			str += "\n";
			i++;
		}
		int invSize = level.npcs.size();
		if (invSize == 0) invSize = 1;
		elements.add(new Element(str, invSize));
	}
	void abilityElement() {
		elements.clear();
		elements.add(new Element("AP: " + info.ap_get + "\na-힘 AP: " + info.strap_get + "\nb-민첩 AP: " + info.dexap_get + "\nc-지능 AP: " + info.intap_get, 6));
	}
	void invElement() {
		elements.clear();
		Inventory inv = client.loadInv(client.id);
		String str = "가진 물건 \n";
		char start = 'a';
		for (InvItem i : inv.items) {
			ItemInfo ii = findItemInfo(i);
			str += start + "-" + ii.getRealName() + " " + i.count;
			if (i.equiped)
				str += " [착용됨]";
			str += "\n";
			start++;
		}
		int invSize = inv.items.size();
		if (invSize == 0) invSize = 1;
		elements.add(new Element(str, invSize));
	}
	void defaultElement() {
		elements.clear();
		if (info == null) return;
		elements.add(new Element(client.id + " Lv." + info.lvl_get + " " + info.exp_get + "/" + needExp() + " " + ClassType.cls2name(info.cls_get) + "\n" + level.name + " (" + pX + "," + pY + ")\n" + "체력:" + info.hp_get + "/" + info.maxhp_get + "\n마력: " + info.mp_get + "/" + info.maxmp_get + "\n" + "힘: " + info.str_get + " 민첩: " + info.dex_get + "\n지능: " + info.int_get + " 외모: " + info.look_get + "\n물방: " + info.phdef_get + " 마방: " + info.mgdef_get, 5));
		elements.add(new Element("F1 - 주문 외기\nF2 - 가진 물건 보기\nF3 - 대화 하기\nF4 - 어빌리티", 2));
	}
	public Player findPlayer(String id) {
		if (id.equals(client.id)) return player;
		for (Player p : otherPlayers) {
			if (p.id != null && p.id.equals(id)) return p;
		}
		return null;
	}
	public void fixed() {
		if (level == null) return;
		chatDialog.update(this, FDT);
		if (!logedIn) return;
		efs = client.getSkillList(player.mapname);
		ArrayList<String> uids = client.uidsList();
		//System.out.println("ME: " + player.X + ", " + player.Y);
		for (String id : uids) {
			if (id.equals(client.id)) continue;
			if (findPlayer(id) == null) {
				Player p = new Player();
				p.hm = new HumanAvater();
				p.armor = Part.WEDDING;
				p.face = Part.FACE;
				p.hair = Part.HAIR;
				p.id = id;
				otherPlayers.add(p);
			}
			Player p = findPlayer(id);
			//p.avater.action(0);
			UserInfo oinfo = client.getUInfo(id);
			int actNum = oinfo.actNum_get;
			p.xdir = oinfo.xdir;
			p.ydir = oinfo.ydir;
			//System.out.println("OT: " + p.X + ", " + p.Y);
		}
		info = client.getUInfo(client.id);
		pX = info.locX_get;
		pY = info.locY_get;
		info.uid_send = client.id;
		//System.out.println(info.xdir + ", " + info.ydir + ", " + info.level_get);
		if (!player.mapname.equals(info.level_get)) { // took a portal from server!
			level = new Level(info.level_get, getPanel());
		}
		player.mapname = info.level_get;
		client.saveUInfo(info);
		chats = client.chatList();
		mobs = client.getMonsterList(player.mapname);
		player.update();
		player.hm.update(this, FDT);
		Inventory inv = client.loadInv(client.id);
		boolean bgFound = false;
		boolean armorFound = false;
		boolean weaponFound = false;
		boolean helmFound = false;
		strStat = 0;
		dexStat = 0;
		intStat = 0;
		lookStat = 0;
		for (InvItem item : inv.items) {
			ItemInfo iinf = findItemInfo(item);
			if (!item.equiped) continue;
			switch (iinf.getPt()) {
				case BGS:
					if (!bgFound) {
						bgFound = true;
						player.bg = iinf.getPart();
					}
					break;
				case WEAPON:
					if (!weaponFound) {
						weaponFound = true;
						player.weapon = iinf.getPart();
					}
					break;
				case ARMOR:
					armorFound = true;
					player.armor = iinf.getPart();
					break;
				case HELM:
					helmFound = true;
					player.helmBack = iinf.getPart();
					player.helm = iinf.getPart2();
					break;
				case SHOES:
					break;
				default:
					break;
			}
		}
		if (!armorFound) {
			player.armor = Part.UNDER;
		}
		if (!helmFound) {
			player.helm = null;
			player.helmBack = null;
		}
		if (!weaponFound) {
			player.weapon = null;
		}
		if (!bgFound) {
			player.bg = null;
		}
		Player tr = null;
		for (Player op : otherPlayers) {
			if (!uids.contains(op.id)) tr = op;
			op.update();
			op.hm.update(this, FDT);
		}
		if (tr != null)
			otherPlayers.remove(tr);
	}
	public static int xtos(int x) {
		if (!fullScreen) return x;
		return 240 + (int)(x * 2.25f);
	}
	public static int ytos(int y) {
		if (!fullScreen) return y;
		return (int)(y * 2.25f);
	}
	public static int whtos(int x) {
		if (!fullScreen) return x;
		return (int)(x * 2.25f);
	}
	@Override
	public void MainLoop() {
		super.MainLoop();
		double delta = getDelta();
		fixedTick += delta;
		if (fixedTick >= FDT) {
			fixed();
			fixedTick -= FDT;
		}
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		if (info != null && info.udelay) return;
		if (chatDialog.chatFocus) {
			chatDialog.key(this, e);
			return;
		}
		
	}
	@Override
	public void onClickScreen(int x, int y) {
		
    }
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!chatDialog.chatFocus || (chatDialog.chatFocus && chatDialog.str.length() == 0))
			chatDialog.chatFocus = !chatDialog.chatFocus;
		}
		if (!logedIn) return;
		if (info.udelay) return;
		if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			scrollElement += 30;
		}
		else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			scrollElement -= 30;
		}
		else if (e.getKeyCode() == KeyEvent.VK_F2) {
			eleMode = 1;
		}
		else if (e.getKeyCode() == KeyEvent.VK_F3) {
			eleMode = 3;
		}
		else if (e.getKeyCode() == KeyEvent.VK_F4) {
			eleMode = 6;
		}
		else if (e.getKeyCode() == KeyEvent.VK_F1) {
			eleMode = 2;
		}
		else if (e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z) {
			int c = e.getKeyCode() - KeyEvent.VK_A;
			if (eleMode == 1) {
				eleMode = 0;
				client.useItem(c);
			}
			if (eleMode == 2) {
				eleMode = 5;
				selMonster = c;
			}
			else if (eleMode == 4) {
				talkMsg = "";
				tind = 0;
				client.NPCres(c);
			}
			else if (eleMode == 5) {
				eleMode = 0;
				ArrayList<String> skills = client.getSkills();
				client.useSkill(skills.get(c), selMonster);
			}
			else if (eleMode == 6) {
				switch (c) {
					case 0:
						client.useAP(1, 0, 0);
						break;
					case 1:
						client.useAP(0, 1, 0);
						break;
					case 2:
						client.useAP(0, 0, 1);
						break;
				}
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			eleMode = 0;
		}
		else if (eleMode == 4 && (e.getKeyCode() == KeyEvent.VK_F5 || e.getKeyCode() == KeyEvent.VK_SPACE)) {
			talkMsg = "";
			tind = 0;
			client.NPCres(0);
		}
		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (eleMode == 3) {
				client.talkNPC(selNPC);
				eleMode = 4;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (eleMode == 0)
				client.move(0, -1);
			if (eleMode == 3 && selNPC > 0) {
				selNPC--;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (eleMode == 0)
				client.move(0, 1);
			if (eleMode == 3 && selNPC < level.npcs.size()-1) {
				selNPC++;
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (eleMode == 0)
				client.move(-1, 0);
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (eleMode == 0)
				client.move(1, 0);
		}
	}
	@Override
	public void DrawScreen(Graphics rG) {
		if (this.player == null) return;
		switch (eleMode) {
			case 0:
				defaultElement();
				break;
			case 1:
				invElement();
				break;
			case 2:
				skillElement();
				break;
			case 3:
				talkElement();
				break;
			case 4:
				talkingElement();
				break;
			case 5:
				selSkillElement();
				break;
			case 6:
				abilityElement();
				break;
		}
		Graphics2D g2d = (Graphics2D)rG;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        Graphics g = screen.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
        g.setColor(Color.WHITE);
        String bg = "bg.10";
        if (level != null) bg = level.bg;
        g.drawImage(ResourceLoader.get(bg), -64, 66, null);
		
		BufferedImage ui = ResourceLoader.get("ui.ui");
		BufferedImage uui = ResourceLoader.get("ui.upinfo");
		BufferedImage ele0 = ResourceLoader.get("ui.ele0");
		BufferedImage ele1 = ResourceLoader.get("ui.ele1");
		BufferedImage ele2 = ResourceLoader.get("ui.ele2");
		g.drawImage(ui, 0, 0, null);
		int pi = 1;
		for (Player p : otherPlayers) {
			p.hm.x = 22 + pi * 84;
			p.hm.y = 15;
			p.hm.render(g);
			pi++;
		}
		this.player.hm.render(g);
		for (EffectProtocol ef : efs) {
			BufferedImage img = ResourceLoader.get(ef.skillBase);
			int x = 0, y = 0;
			Player p = findPlayer(ef.user);
			if (p != null) {
				x = p.hm.x-4;
				y = p.hm.y;
			}
			g.drawImage(img, x, y, null);
		}
		
		g.setFont(font);
		int sy = 30 + scrollElement;
		for (Element e : elements) {
			int sx = 455;
			g.drawImage(ele2, (sx), (sy), (ele2.getWidth()), (ele2.getHeight()), null);
			for (int i = 0; i < e.line; i++) {
				g.drawImage(ele1, (sx), (sy + 21 + i * 27), (ele1.getWidth()), (ele1.getHeight()), null);
			}
			g.drawImage(ele0, (sx), ((sy + 21*2-1 + (e.line) * 27)), (ele0.getWidth()), (ele0.getHeight()), null);
			int fy = 0;
			for (String l : e.str.split("\n")) {
				g.setColor(new Color(60, 60, 60));
				g.drawString(l, (sx + 23), (sy + 29 + fy));
				fy += 22;
			}
			sy += 21+28 + e.line * 27 + 6;
		}
		chatDialog.draw(this, chats, g);
		if (level != null && logedIn) {
			int tsx = 530, tsy = 405;
			g.setColor(Color.WHITE);
			int gap = 12;
			for (Tile tile : level.tiles) {
				int dx = tile.x * gap + tsx - pX * gap;
				int dy = tile.y * gap + tsy - pY * gap;
				if (dx < tsx-70 || dy < tsy-70 ||
						dx > 600 || dy > 460) continue;
				g.drawString("*", dx, dy);
			}
			g.drawString("@", pX * gap + tsx - pX * gap, pY * gap + tsy - pY * gap);
		}
		g.dispose();
		rG.setColor(Color.BLACK);
		rG.fillRect(0, 0, getWidth(), getHeight());
		rG.setColor(Color.WHITE);
		rG.drawImage(screen, xtos(0), ytos(0), whtos(screen.getWidth()), whtos(screen.getHeight()), null);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new Game();
	}
}
