package ru.liga.datingsite.telegrambot.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.liga.datingsite.telegrambot.DataBase;
import ru.liga.datingsite.telegrambot.enums.BotStateEnum;

import static ru.liga.datingsite.telegrambot.button.MainMenuService.getMainMenuKeyboard;

public class MenuQuestionnaire {
    public static SendMessage usingMenuQuestionnaire(Long chatId, String message, DataBase dataBase) {
        SendMessage sendMessage = new SendMessage();
        switch (message) {
            case "Показать анкету":
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText("Ваша анкета:");
                dataBase.setState(BotStateEnum.SHOWING_QUESTIONNAIRE);
                return sendMessage;

            case "Изменить анкету":
                sendMessage.setChatId(String.valueOf(chatId));
                sendMessage.setText("Изменение анкеты запущено.");
                dataBase.setState(BotStateEnum.CHANGE_QUESTIONNAIRE);
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
