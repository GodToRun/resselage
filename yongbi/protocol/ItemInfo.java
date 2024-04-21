package yongbi.protocol;

import java.util.ArrayList;

import yongbi.protocol.ItemInfo;

public class ItemInfo {
	public static final ItemInfo pie,
	bingsu,
	chicken,
	podoju,
	melon,
	money,
	jub,
	gj,
	cbi,
	wdd,
	rob,
	sera,
	veil,
	bouquet,
	knife,
	rine,
	wand,
	umb,
	richdress,
	flowerhat,
	lightbg;
	public static ArrayList<ItemInfo> bases = new ArrayList<>();
	static {
		pie = new ItemInfo("pie", "체력을 20 회복시킨다.", "파이", PartType.POTION, 1, 0);
		pie.setTag2(20);
		bingsu = new ItemInfo("clt", "마력을 400 회복시킨다.", "클레멘타인", PartType.POTION, 24, 12);
		bingsu.setTag3(400);
		chicken = new ItemInfo("wski", "체력을 100 회복시킨다.", "위스키", PartType.POTION, 5, 2);
		chicken.setTag2(100);
		podoju = new ItemInfo("elx", "체력을 1600 회복시킨다.", "엘릭서", PartType.POTION, 80, 55);
		podoju.setTag2(1600);
		jub = new ItemInfo("jub", "체력을 400 회복시킨다.", "포도주", PartType.POTION, 22, 15);
		jub.setTag2(400);
		melon = new ItemInfo("mln", "마력을 100 회복시킨다.", "레몬", PartType.POTION, 6, 3);
		melon.setTag3(100);
		money = new ItemInfo("rsl", "레셀 왕국에서 쓰이는 은화이다.", "레셀", PartType.NONE, 1, 1);
		gj = new ItemInfo("gj", "마력을 20 회복시킨다.", "토마토", PartType.POTION, 1, 0);
		gj.setTag3(20);
		cbi = new ItemInfo("hfelx", "마력을 1600 회복시킨다.", "하프엘릭서", PartType.POTION, 100, 60);
		cbi.setTag3(1600);
		wdd = new ItemInfo("wdd", "아름다운 여성용 웨딩드레스다. 이 옷을 입어야 결혼을 할수 있다.", "웨딩드레스", PartType.ARMOR, 2147483647, 400000);
		wdd.setPart(Part.WEDDING);
		wdd.setLook(8);
		wdd.setHp(35);
		wdd.setPhDef(5);
		wdd.setMgDef(5);
		veil = new ItemInfo("veil", "아름다운 여성용 베일이다. 이 수건을 씌어야 결혼을 할수 있다.", "베일", PartType.HELM, 2147483647, 80000);
		veil.setPart(Part.VEIL);
		veil.setPart2(Part.WEDDINGCROWN);
		veil.setLook(2);
		richdress = new ItemInfo("rcdrs", "귀부인들이 레셀 왕국에서 즐겨 입는 풍성한 드레스다.", "풍성드레스", PartType.ARMOR, 400000, 80000);
		richdress.setPart(Part.RICHDRESS);
		richdress.setLook(3);
		flowerhat = new ItemInfo("flwh", "귀부인들이 레셀 왕국에서 쓰는 꽃 모자이다.", "꽃모자", PartType.HELM, 125000, 25000);
		flowerhat.setPart2(Part.FLOWERHAT);
		flowerhat.setLook(1);
		wand = new ItemInfo("wnd", "마술사가 사용하는 완드다.", "완드", PartType.WEAPON, 2000, 40);
		wand.setInt(1);
		wand.setPart(Part.WAND);
		rine = new ItemInfo("rine", "검사가 사용하는 대검이다.", "라인 대검", PartType.WEAPON, 2500, 40);
		rine.setStr(1);
		rine.setPart(Part.RINE);
		knife = new ItemInfo("knfe", "도둑이 사용하는 단검이다.", "단검", PartType.WEAPON, 1700, 40);
		knife.setDex(1);
		knife.setPart(Part.KNIFE);
		bouquet = new ItemInfo("bq", "여성이 받은 꽃다발이다. 이 꽃다발을 들어야 결혼을 할수 있다.", "부케", PartType.WEAPON, 2147483647, 100000);
		bouquet.setPart(Part.BOUQUET);
		bouquet.setLook(2);
		bouquet.setDex(6);
		umb = new ItemInfo("umb", "레셀 왕국에서 귀부인들이 사용하는 우산이다.", "우산", PartType.WEAPON, 50000, 10000);
		umb.setPart(Part.UMBRELLA);
		umb.setLook(1);
		rob = new ItemInfo("rob", "사제가 입는 로브이다. 매우 두껍다.", "사제의로브", PartType.ARMOR, 25000, 5000);
		rob.setMgDef(1);
		rob.setMp(1);
		rob.setPart(Part.ROB);
		sera = new ItemInfo("sera", "여성용 세일러복이다. 가볍고 강력하다.", "세일러복", PartType.ARMOR, 2147483647, 40000);
		sera.setPart(Part.SERA);
		lightbg = new ItemInfo("lbg", "전구가 있는 배경이다.", "전구배경", PartType.BGS, 2147483647, 0);
		lightbg.setPart(Part.AVBG0);
		bases.add(pie);
		bases.add(bingsu);
		bases.add(chicken);
		bases.add(podoju);
		bases.add(melon);
		bases.add(money);
		bases.add(jub);
		bases.add(gj);
		bases.add(cbi);
		bases.add(wdd);
		bases.add(rob);
		bases.add(sera);
		bases.add(bouquet);
		bases.add(richdress);
		bases.add(flowerhat);
		bases.add(umb);
		bases.add(veil);
		bases.add(wand);
		bases.add(rine);
		bases.add(knife);
		bases.add(lightbg);
	}
	public static ItemInfo find(String name) {
		for (ItemInfo i : bases) {
			if (i.getName().equals(name)) return i;
		}
		return null;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public PartType getPt() {
		return pt;
	}
	public void setPt(PartType pt) {
		this.pt = pt;
	}
	private int tag2, tag3;
	private String name;
	private String tag;
	private String realName;
	private PartType pt;
	private Part part, part2;
	private int price, sell, reqLVL = 1, reqClassMin = ClassType.CITIZEN, reqClassMax = ClassType.END;
	private int strStat, dexStat, intStat, lookStat, hpStat, mpStat, phDefStat, mgDefStat, needPro, reqLvl, reqClass;
	public int getHp() {
		return hpStat;
	}
	public void setHp(int hpStat) {
		this.hpStat = hpStat;
	}
	public int getMp() {
		return mpStat;
	}
	public void setMp(int mpStat) {
		this.mpStat = mpStat;
	}
	public int getPhDef() {
		return phDefStat;
	}
	public void setPhDef(int phDefStat) {
		this.phDefStat = phDefStat;
	}
	public int getMgDef() {
		return mgDefStat;
	}
	public void setMgDef(int mgDefStat) {
		this.mgDefStat = mgDefStat;
	}
	public int getNeedPro() {
		return needPro;
	}
	public void setNeedPro(int needPro) {
		this.needPro = needPro;
	}
	public int getReqLvl() {
		return reqLvl;
	}
	public void setReqLvl(int reqLvl) {
		this.reqLvl = reqLvl;
	}
	public int getReqClass() {
		return reqClass;
	}
	public void setReqClass(int reqClass) {
		this.reqClass = reqClass;
	}
	public int getSell() {
		return sell;
	}
	public void setPart(Part part) {
		this.part = part;
	}
	public Part getPart() {
		return this.part;
	}
	public void setSell(int sell) {
		this.sell = sell;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public ItemInfo(String name, String tag, String realName, PartType pt, int price, int sell) {
		setName(name);
		setTag(tag);
		setRealName(realName);
		setPt(pt);
		setPrice(price);
		setSell(sell);
	}
	public int getStr() {
		return strStat;
	}
	public void setStr(int strStat) {
		this.strStat = strStat;
	}
	public int getDex() {
		return dexStat;
	}
	public void setDex(int dexStat) {
		this.dexStat = dexStat;
	}
	public int getInt() {
		return intStat;
	}
	public void setInt(int intStat) {
		this.intStat = intStat;
	}
	public int getLook() {
		return lookStat;
	}
	public void setLook(int lookStat) {
		this.lookStat = lookStat;
	}
	public Part getPart2() {
		return part2;
	}
	public void setPart2(Part part2) {
		this.part2 = part2;
	}
	public int getReqLVL() {
		return reqLVL;
	}
	public void setReqLVL(int reqLVL) {
		this.reqLVL = reqLVL;
	}
	public int getReqClassMin() {
		return reqClassMin;
	}
	public void setReqClassMin(int reqClassMin) {
		this.reqClassMin = reqClassMin;
	}
	public int getReqClassMax() {
		return reqClassMax;
	}
	public void setReqClassMax(int reqClassMax) {
		this.reqClassMax = reqClassMax;
	}
	public int getTag2() {
		return tag2;
	}
	public void setTag2(int tag2) {
		this.tag2 = tag2;
	}
	public int getTag3() {
		return tag3;
	}
	public void setTag3(int tag3) {
		this.tag3 = tag3;
	}
}
