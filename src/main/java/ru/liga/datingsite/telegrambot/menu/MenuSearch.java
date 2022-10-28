package ru.liga.datingsite.telegrambot.menu;

import ru.liga.datingsite.telegrambot.DataBase;
import ru.liga.datingsite.telegrambot.enums.BotStateEnum;
import ru.liga.datingsite.telegrambot.questionnaire.QuestionnaireLike;

import java.io.IOException;

public class MenuSearch {
    static boolean firstLike = true;
    static QuestionnaireLike like = new QuestionnaireLike();
    public static int usingMenuSearch(String callbackQuery, DataBase dataBase, int id, long chatId ) throws IOException {

        switch (callbackQuery) {
            case "Поиск_Вправо" -> {
                dataBase.setState(BotStateEnum.SEARCH);
                like.putLikeQuestionnaire(chatId,id, firstLike);
                firstLike=false;
                if (id == 6) {
                    return 1;
                } else return ++id;
            }
            case "Поиск_Влево" -> {
                dataBase.setState(BotStateEnum.SEARCH);
                if (id == 6) {
                    return 1;
                } else return ++id;
            }
            case "Меню" -> dataBase.setState(BotStateEnum.MENU);
        }
        return id;
    }
}
