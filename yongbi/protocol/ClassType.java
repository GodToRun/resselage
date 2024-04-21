package yongbi.protocol;

public class ClassType {
	public static final int CITIZEN = 0,
			WARRIOR = 100,
			KNIGHT = 110,
			WARRIOREND = 199,
			MAGICIAN = 200,
			CLERICK = 210,
			BARD = 220,
			MAGE = 230,
			MAGICIANEND = 299,
			THIEF = 300,
			ASSASSIN = 310,
			THIEFEND = 399,
			END = 10000;
	public static String cls2name(int type) {
		switch (type) {
		case CITIZEN:
			return "민간인";
		case WARRIOR:
			return "검사";
		case KNIGHT:
			return "나이트";
		case MAGICIAN:
			return "마술사";
		case CLERICK:
			return "클레릭";
		case BARD:
			return "바드";
		case MAGE:
			return "메이지";
		case THIEF:
			return "도둑";
		case ASSASSIN:
			return "어쌔신";
		}
		return "불청객";
	}
}
