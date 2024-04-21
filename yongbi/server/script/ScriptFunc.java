package yongbi.server.script;

import java.util.ArrayList;

import yongbi.protocol.ItemInfo;
import yongbi.protocol.InvItem;
import yongbi.protocol.UserInfo;
import yongbi.server.ServerInstance;

public class ScriptFunc {
	ServerInstance srv;
	public ScriptFunc(ServerInstance srv) {
		this.srv = srv;
	}
	public void sayOK(String str) {
		srv.npcCon.res.add("확인");
		while (srv.resNum != 0) {
			srv.npcCon.contents = str;
		}
		srv.resNum = -1;
		srv.talkEnd();
	}
	public int sayQuestions(String str, String[] args) {
		for (String arg : args)
			srv.npcCon.res.add(arg);
		while (srv.resNum == -1) {
			srv.npcCon.contents = str;
		}
		int tmp = srv.resNum;
		srv.resNum = -1;
		srv.talkEnd();
		return tmp;
	}
	public int getLevel() {
		return uinfo().lvl_get;
	}
	public int getStr() {
		return uinfo().str_get;
	}
	public int getDex() {
		return uinfo().dex_get;
	}
	public int getInt() {
		return uinfo().int_get;
	}
	public int getLook() {
		return uinfo().look_get;
	}
	public int getCls() {
		return uinfo().cls_get;
	}
	UserInfo uinfo() {
		return srv.rootServer.uiTable.get(srv.userid);
	}
	public void addInvItem(String code, int count) {
		srv.addInv(ItemInfo.find(code), count);
	}
	public void removeInvItem(String code, int p) {
		srv.rootServer.removeInv(srv.userid, code, p);
	}
	public int getInvItem(String code) {
		for (InvItem i : srv.rootServer.uinvTable.get(srv.userid).items) {
			if (i.itemName.equals(code)) {
				return i.count; 
			}
		}
		return 0;
	}
	public void setCls(int cls) {
		uinfo().cls_get = cls;
	}
}
