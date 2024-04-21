arr = [u"파이: 1레셀", u"토마토: 1레셀", u"위스키: 5레셀", u"레몬: 5레셀", u"닫기"];
def q():
    sel = self.sayQuestions(u"아휴 피곤해..\n거기 알아서 잘\n골라 봐요~", arr);
    if sel == 0:
        buy(u"rsl", 1, u"pie");
    if sel == 1:
        buy(u"rsl", 1, u"gj");
    if sel == 2:
        buy(u"rsl", 5, u"wski");
    if sel == 3:
        buy(u"rsl", 5, u"mln");
    if sel != 4:
        q();
def buy(code, p, tob):
    if self.getInvItem(code) >= p:
        self.removeInvItem(code, p);
        self.addInvItem(tob, 1);
q();