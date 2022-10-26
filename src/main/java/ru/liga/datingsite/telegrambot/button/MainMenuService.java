package ru.liga.datingsite.telegrambot.button;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainMenuService {

//    public SendPhoto getMainMenuMessage(final long chatId, SendPhoto image) {
//        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
//        final SendPhoto mainMenuMessage =
//                createMessageWithKeyboard(chatId, image, replyKeyboardMarkup);
//
//        return mainMenuMessage;
//    }
    public static ReplyKeyboardMarkup getMainMenuKeyboard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton("Поиск"));
        row2.add(new KeyboardButton("Анкета"));
        row3.add(new KeyboardButton("Любимцы"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

//    private SendPhoto createMessageWithKeyboard(final long chatId,
//                                                  SendPhoto image,
//                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
//        final SendPhoto sendMessage = new SendPhoto();
//        sendMessage.setPhoto(image.getPhoto());
//        sendMessage.setChatId(String.valueOf(chatId));
//        if (replyKeyboardMarkup != null) {
//            sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        }
//        return sendMessage;
//    }
}
