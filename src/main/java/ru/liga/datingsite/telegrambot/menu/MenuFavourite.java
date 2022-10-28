package ru.liga.datingsite.telegrambot.menu;

import ru.liga.datingsite.telegrambot.DataBase;
import ru.liga.datingsite.telegrambot.enums.BotStateEnum;
import ru.liga.datingsite.telegrambot.questionnaire.QuestionnaireLike;

import java.io.IOException;

public class MenuFavourite {
    public static int usingMenuFavourite(String callbackQuery, DataBase dataBase, int id ) throws IOException {

        switch (callbackQuery) {
            case "Любимцы_Вправо" -> {
                dataBase.setState(BotStateEnum.FAVOURITE);
                if (id == 3) {
                    return 1;
                } else return ++id;
            }
            case "Любимцы_Влево" -> {
                dataBase.setState(BotStateEnum.FAVOURITE);
                if (id == 1) {
                    return 3;
                } else return --id;
            }
            case "Меню" -> dataBase.setState(BotStateEnum.MENU);
        }
        return id;
    }
}
