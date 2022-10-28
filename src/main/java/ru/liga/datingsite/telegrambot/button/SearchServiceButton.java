package ru.liga.datingsite.telegrambot.button;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SearchServiceButton {

    public static InlineKeyboardMarkup  getSearchInlineKeyboardMarkup () {

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        InlineKeyboardButton RightButton = new InlineKeyboardButton();
        RightButton.setText("Вправо");
        RightButton.setCallbackData("Поиск_Вправо");

        InlineKeyboardButton LeftButton = new InlineKeyboardButton();
        LeftButton.setText("Влево");
        LeftButton.setCallbackData("Поиск_Влево");

        InlineKeyboardButton MenuButton = new InlineKeyboardButton();
        MenuButton.setText("Меню");
        MenuButton.setCallbackData("Меню");

        rowInLine.add(LeftButton);
        rowInLine.add(MenuButton);
        rowInLine.add(RightButton);

        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }
}
