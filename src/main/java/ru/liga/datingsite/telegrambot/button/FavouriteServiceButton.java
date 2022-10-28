package ru.liga.datingsite.telegrambot.button;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class FavouriteServiceButton {

    public static InlineKeyboardMarkup  getFavouriteInlineKeyboardMarkup() {

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        InlineKeyboardButton RightButton = new InlineKeyboardButton();
        RightButton.setText("Вправо");
        RightButton.setCallbackData("Любимцы_Вправо");

        InlineKeyboardButton LeftButton = new InlineKeyboardButton();
        LeftButton.setText("Влево");
        LeftButton.setCallbackData("Любимцы_Влево");

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
