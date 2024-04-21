package yongbi.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import pw.common.Camera;
import yongbi.protocol.ChatElement;

public class ChatDialog {
	public boolean chatFocus = false;
	double fcsTick = 0.0D;
	public String str = "";
	int cur = 0;
	public ArrayList<String> notifyMsgs = new ArrayList<>();
	int loginLevel = 0;
	String inputID, inputPassword, inputGender, inputName;
	public void init(Game game) {
		game.chats.add(new ChatElement("1: 로그인 2: 회원가입"));
	}
	void parseClientSide(Game game, ChatElement ele) {
		if (loginLevel == 0) {
			if ("1".equals(ele.contents)) {
				game.chats.add(new ChatElement("로그인 ID를 입력해주십시오."));
				loginLevel = 100; return;
			}
			else if ("2".equals(ele.contents)) {
				game.chats.add(new ChatElement("캐릭터 ID를 입력해주십시오."));
				loginLevel = 200; return;
			}
			else {
				init(game);
				loginLevel = 0; return;
			}
		}
		if (loginLevel == 100) {
			inputID = ele.contents;
			game.chats.add(new ChatElement("비밀번호를 입력해주십시오."));
		}
		if (loginLevel == 101) {
			inputPassword = ele.contents;
			if (!game.login(game.clientSocket, inputID, inputPassword)) {
				game.chats.add(new ChatElement("로그인 정보가 맞지 않습니다."));
				loginLevel = 0;
				init(game);
				return;
			}
		}
		if (loginLevel == 200) {
			inputID = ele.contents;
			game.chats.add(new ChatElement("비밀번호를 입력해주십시오."));
		}
		if (loginLevel == 201) {
			inputPassword = ele.contents;
			game.chats.add(new ChatElement("캐릭터의 성별을 정해주십시오. 남/여"));
		}
		if (loginLevel == 202) {
			inputGender = ele.contents;
			game.chats.add(new ChatElement("캐릭터의 이름을 정해주십시오."));
		}
		if (loginLevel == 203) {
			inputName = ele.contents;
			game.client.signup(inputID, inputGender, inputName, inputPassword);
			init(game);
			loginLevel = 0;
			return;
		}
		loginLevel++;
	}
	public void key(Game game, KeyEvent e) {
		if (e.getKeyChar() == '\n' && str.length() != 0) {
			ChatElement ele = new ChatElement();
			ele.chatType = 0;
			ele.contents = str;
			ele.sender = game.client.id;
			ele.x = 500;
			ele.y = 200;
			ele.tick = 0.0D;
			if (!game.logedIn)
				parseClientSide(game, ele);
			else
				game.client.sendChat(ele);
			str = "";
			cur = 0;
			chatFocus = false;
			return;
		}
		if (e.getKeyChar() == '\b') {
			if (str.length() > 0) {
				str = str.substring(0, str.length()-1);
				cur--;
			}
			return;
		}
		if (e.getKeyChar() != '\n' ) {
			str += e.getKeyChar();
			cur++;
		}
	}
	public void update(Game game, double delta) {
		if (chatFocus) {
			fcsTick += delta;
			if (fcsTick >= 0.8D) {
				fcsTick = 0.0D;
			}
		}
	}
	public ArrayList<String> splitChunks(int max, String str, ArrayList<String> l) {
		if (str != null && str != "") {
			if (str.length() < max) {
				l.add(str);
				str = null;
				return l;
			}
			l.add(str.substring(0, max));
			str = str.substring(max, str.length());
			splitChunks(max, str, l);
		}
		return l;
	}
	public void draw(Game game, ArrayList<ChatElement> es, Graphics g) {
		int cx = 20;
		int cy = 436;
		int topY = 350;
		g.setColor(Color.BLACK);
		if (chatFocus) {
			if (fcsTick >= 0.4D) {
				g.fillRect((cx + cur * 16), (cy), (10), (24));
			}
		}
		g.drawString(str, (cx), (cy+20));
		int s = 0; if (es.size() > 4) s = es.size()-4;
		int i = 0;
		for (; s < es.size(); s++) {
			ChatElement e = es.get(s);
			if (e.showTo != null && !e.showTo.equals(game.client.id)) continue;
			if (e.tick < 5.0f) {
				Player p = game.findPlayer(e.sender);
				if (p != null) {
					int rx = p.hm.x;
					int ry = p.hm.y + 95;
					ArrayList<String> line = new ArrayList<>();
					int max = 7;
					splitChunks(max, e.contents, line);
					int width = max * 20;
					int height = line.size() * 24 + 6;
					g.setColor(Color.WHITE);
					g.drawRoundRect(rx + Camera.X, ry + Camera.Y, width, height, 3, 3);
					g.setColor(new Color(0, 0, 0, 180));
					g.fillRoundRect(rx + Camera.X, ry + Camera.Y, width, height, 3, 3);
					g.setColor(Color.WHITE);
					for (int j = 0; j < line.size(); j++)
						g.drawString(line.get(j), rx + Camera.X, 14 + ry + j * 24 + Camera.Y);
				}
			}
			g.setColor(Color.BLACK);
			if (e.chatType == 1) {
				g.setColor(new Color(0, 0, 180));
				g.fillRect((cx-5), (topY+i*24-20), (422), (30));
				g.setColor(Color.WHITE);
			}
			if (e.chatType == 2) {
				g.setColor(new Color(255, 90, 90));
				g.fillRect((cx-5), (topY+i*24-20), (422), (30));
				g.setColor(Color.WHITE);
			}
			g.drawString(e.sender + ": " + e.contents, (cx), (topY+i*24));
			i++;
		}
	}
}
