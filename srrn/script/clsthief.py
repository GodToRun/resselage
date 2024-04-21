if self.getLevel() >= 20 and self.getCls() == 300:
    self.sayOK(u"당신..그 허\n약했던 모습은\n어디가고 어떻게\n이렇게 강해\n지신 거죠?");
    self.sayOK(u"더 강해지고\n싶다고요?\n알겠습니다..");
    self.sayOK(u"다만 당신의\n능력을 시험\n해봐야 합니다.");
    self.sayOK(u"제가 특별한\n곳으로 보내드릴\n테니 거기서\n증표를 가지고\n오시면");
    self.sayOK(u"암살자로 전직\n시켜 드리겠\n습니다.");
    self.sayOK(u"정말 증표를\n모았군요.. 좋습\n니다.\n당신을 암살자로\n전직 시켜\n드리겠습니다.");
    self.sayOK(u"이야아아아압!");
    self.setCls(310);
    self.sayOK(u"당신을 암살자로\n전직 시켜\n드렸습니다.\n그럼 더\n강해지신 뒤\n오시길...");
else:
    self.sayOK(u"이방인이 무슨\n일로 저에게 오신\n거죠?");
    self.sayOK(u"도둑으로 전직을\n시켜 달라고요?\n물론 해드릴수\n있지만..");
    self.sayOK(u"우리 마을은 귀족\n중심 사회입니다.\n당신이 이 마을\n에 합류하면");
    self.sayOK(u"여전히 사람들은\n당신을 이방인\n이라고 생각\n할 것 입니다.");
    self.sayOK(u"..그래도 괜찮\n으시다면 지금\n바로 전직을 시켜\n드리겠습니다.");
    self.sayOK(u"도둑으로 전직\n하실겁니까?");
    if self.getLevel() >= 8 and self.getCls() == 0:
        self.setCls(300);
        self.sayOK(u"당신은 이제\n우리 마을 소속\n입니다.\n환영하고\n앞으로 잘\n지내 봅시다.");
    else:
        self.sayOK(u"하지만 당신은\n너무 약해\n보이는 군요.\n죄송하지만\n받아드릴수\n없습니다.");
    
