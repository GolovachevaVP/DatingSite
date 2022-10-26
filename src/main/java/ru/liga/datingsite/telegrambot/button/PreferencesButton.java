package ru.liga.datingsite.telegrambot.button;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class PreferencesButton {
    public static SendMessage createPreferencesButtons(long chatId) throws TelegramApiException {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Кого вы ищите?");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        InlineKeyboardButton preferencesMaleButton = new InlineKeyboardButton();
        preferencesMaleButton.setText("Сударъ");
        preferencesMaleButton.setCallbackData("Сударъ");

        InlineKeyboardButton preferencesFemaleButton = new InlineKeyboardButton();
        preferencesFemaleButton.setText("Сударыня");
        preferencesFemaleButton.setCallbackData("Сударыня");

        InlineKeyboardButton preferencesAllButton = new InlineKeyboardButton();
        preferencesAllButton.setText("Всех");
        preferencesAllButton.setCallbackData("Всех");

        rowInLine.add(preferencesMaleButton);
        rowInLine.add(preferencesFemaleButton);
        rowInLine.add(preferencesAllButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        return message;
    }




    public static String getButtonName(CallbackQuery buttonQuery){
        return buttonQuery.getData();

    }


}
