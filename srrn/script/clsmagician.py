if self.getLevel() >= 20 and self.getCls() == 200:
    self.sayOK(u"많이 강해졌나?");
    self.sayOK(u"그런거 같군.\n전직을 하고\n싶다고?");
    self.sayOK(u"다만 자네의\n능력을 시험\n해봐야 하네.");
    self.sayOK(u"내가 특별한\n곳으로 보낼테니\n거기서\n증표를 가지고\n오면");
    self.sayOK(u"전직 시켜\n주겠네!");
    self.sayOK(u"정말 다\n모았군! 좋아.\n자네를\n전직 시켜\n주겠네.");
    arr = [u"클레릭", u"메이지", u"바드"]
    sel = self.sayQuestions(u"어느 직업\n으로 전직을\n할텐가?", arr);
    if (sel == 0):
        self.setCls(210);
    if (sel == 1):
        self.setCls(230);
    if (sel == 2):
        self.setCls(220);
    self.sayOK(u"자네를\n전직 시켜\n주었네!\n그럼 더 강해\n지고 오게나..");
else:
    self.sayOK(u"자네..\n나에게 무슨\n볼 일이라도\n있나?");
    self.sayOK(u"마술사가 되고\n싶다고? 흠...");
    self.sayOK(u"우리 요정 마을\n에 자네가 오면\n아마 차별을\n받을거야.");
    self.sayOK(u"그래도 요정들은\n지혜로우니\n자네가 오면\n많은 걸 배울수\n있겠지.");
    self.sayOK(u"직업 설명은\n오면서 들었겠지?");
    self.sayOK(u"원한다면 지금\n바로 전직을\n시켜 주겠네.");
    self.setCls(200);
    self.sayOK(u"자네는 이제\n마술사일세.\n다른 요정들과\n빨리 친해지길\n바라겠네!");
    if self.getLevel() < 8 or self.getCls() != 0:
        self.sayOK(u"안타깝지만..\n좀 더 강해지고\n오게나.");
