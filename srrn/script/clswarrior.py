self.sayOK(u"자네, 나에게\n가르침을 받지 않\n겠나? 검사가 되면\n너도 이 마을\n주민에 합류\n하는 걸세.");
self.sayOK(u"이 동네\n사람들은 외부\n인들을 매우\n환영해.\n어떤가?");
self.sayOK(u"그렇다면\n바로 자네를\n검사로 전직\n시켜 주겠네.");
if self.getLevel() >= 8 and self.getCls() == 0:
    self.setCls(100);
    self.sayOK(u"좋아! 자네를\n검사로 전직\n시켜 줬네.\n앞으로 잘\n지내보게!");
else:
    self.sayOK(u"자네를 너무\n받고 싶으나\n우리 마을 규정\n상 자네는\n너무 약하네.");
    self.sayOK(u"세진 다음\n꼭 다시\n와주길 바라네!");