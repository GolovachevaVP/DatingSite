package ru.liga.datingsite.telegrambot.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.liga.datingsite.telegrambot.DataBase;
import ru.liga.datingsite.telegrambot.enums.BotStateEnum;

import static ru.liga.datingsite.telegrambot.button.MainMenuService.getMainMenuKeyboard;
import static ru.liga.datingsite.telegrambot.button.QuestionnaireServiceButton.getQuestionnaireKeyboard;


public class Menu {
    public static SendMessage usingMenu(Long chatId, String message, DataBase dataBase) {
        SendMessage sendMessage = new SendMessage();
        switch (message) {
            case "Поиск":
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText("Вы выбрали категорию \"Поиск\"! Перед вами сейчас появились анкеты," +
                        " для того чтобы добавить анкету в понравившиеся нажмите \"Вправо\", " +
                        "чтобы пропустить \"Влево\". Если хотите выйти из категории \"Поиск\", нажмите \"Меню\".");
                dataBase.setState(BotStateEnum.SEARCH);
                return sendMessage;

            case "Анкета":
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText("Вы выбрали категорию \"Анкета\"!" +
                        " Если хотите выйти из категории \"Анкета\", нажмите \"Меню\".");
                sendMessage.setReplyMarkup(getQuestionnaireKeyboard());
                dataBase.setState(BotStateEnum.QUESTIONNAIRE);
                return sendMessage;

            case "Любимцы":
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText("Вы выбрали категорию \"Любимцы\"! Перед Вами сейчас появились анкеты людей," +
                        " которые понравились Вам, которым понравились Вы." +
                        " Если хотите выйти из категории \"Любимцев\", нажмите \"Меню\".");
                dataBase.setState(BotStateEnum.FAVOURITE);
                return sendMessage;

            case "Меню":
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText("Вы выбрали категорию \"Меню\"!");
                sendMessage.setReplyMarkup(getMainMenuKeyboard());
                dataBase.setState(BotStateEnum.MENU);
                return sendMessage;

            default:
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText("Воспользуйтесь меню");
                return sendMessage;

        }
    }
}
