package yongbi.protocol;

import java.io.Serializable;

public class ChatElement implements Serializable {
	private static final long serialVersionUID = 205L;
	public String sender;
	public String contents;
	public String senderLevel;
	public String showTo = null;
	public int chatType;
	public int x, y;
	public double tick = 0.0D;
	public ChatElement(String str) {
		contents = str;
		sender = "[클라이언트]";
	}
	public ChatElement(String str, boolean serv) {
		if (serv) {
			contents = str;
			sender = "[서버]";
		}
		
	}
	public ChatElement() {
		
	}
}
