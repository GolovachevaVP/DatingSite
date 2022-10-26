package ru.liga.datingsite.telegrambot.button;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class GenderButton {
    public static SendMessage createGengerButtons(long chatId) throws TelegramApiException {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Здравствуйте! Я телеграм-бот, который поможет Вам познакомится с другими людьми." +
                " Для этого давайте заполним анкету.\nВы сударь иль сударыня?");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var genderMaleButton = new InlineKeyboardButton();

        genderMaleButton.setText("Сударъ");
        genderMaleButton.setCallbackData("Сударъ");

        var genderFemaleButton = new InlineKeyboardButton();

        genderFemaleButton.setText("Сударыня");
        genderFemaleButton.setCallbackData("Сударыня");

        rowInLine.add(genderMaleButton);
        rowInLine.add(genderFemaleButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        return message;
    }




    public static String getButtonName(CallbackQuery buttonQuery){
        return buttonQuery.getData();

    }


}
