package ru.liga.datingsite.telegrambot.servise;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.datingsite.telegrambot.DataBase;
import ru.liga.datingsite.telegrambot.button.MainMenuService;
import ru.liga.datingsite.telegrambot.enums.BotStateEnum;
import ru.liga.datingsite.telegrambot.enums.GenderTypeEnum;
import ru.liga.datingsite.telegrambot.property.BotConfig;
import ru.liga.datingsite.telegrambot.questionnaire.Questionnaire;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static ru.liga.datingsite.telegrambot.button.GenderButton.createGengerButtons;
import static ru.liga.datingsite.telegrambot.button.GenderButton.getButtonName;
import static ru.liga.datingsite.telegrambot.button.MainMenuService.getMainMenuKeyboard;
import static ru.liga.datingsite.telegrambot.button.PreferencesButton.createPreferencesButtons;


@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    DataBase dataBase = new DataBase();
    Questionnaire image = new Questionnaire();

    private final MainMenuService mainMenuService;
    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText() || update.hasCallbackQuery()) {
            String messageText = null;
            Long chatId = null;
            if (update.hasMessage() && update.getMessage().hasText()) {
                messageText = update.getMessage().getText();
                chatId = update.getMessage().getChatId();
            }
            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                chatId = callbackQuery.getMessage().getChatId();
            }

            if (dataBase.getState() == null && messageText.equals("/start")) {
                execute(createGengerButtons(chatId));
                dataBase.setState(BotStateEnum.ASK_GENDER);
                System.out.println();
            } else if (dataBase.getState() == BotStateEnum.ASK_GENDER) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                dataBase.setGender(GenderTypeEnum.fromString(getButtonName(callbackQuery)));
                sendMessage(chatId, "Как Вас величать?");
                dataBase.setState(BotStateEnum.ASK_NAME);
            } else if (dataBase.getState() == BotStateEnum.ASK_NAME) {
                dataBase.setName(messageText);
                sendMessage(chatId, "Опишите себя");
                dataBase.setState(BotStateEnum.ASK_DESCRIPTION);
            } else if (dataBase.getState() == BotStateEnum.ASK_DESCRIPTION) {
                dataBase.setDescription(messageText);
                execute(createPreferencesButtons(chatId));
                dataBase.setState(BotStateEnum.ASK_PREFERENCES);
            } else if (dataBase.getState() == BotStateEnum.ASK_PREFERENCES) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                dataBase.setPreferences(GenderTypeEnum.fromString(getButtonName(callbackQuery)));
                sendPhoto(chatId);
                dataBase.setState(BotStateEnum.MENU);
            }
//                log.info("пользователь ввел команду /start");
//
//                //sendMessage(chatId, startCommandReceived());
//            } else {
//                log.info("пользователь ввел запрос - {}", messageText);
//            }
//        }else if(update.hasCallbackQuery()){
//            try {
//                CallbackQuery callbackQuery = update.getCallbackQuery();
//               execute(processCallbackQuery(callbackQuery)));
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
        }
    }


    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage(String.valueOf(chatId), textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка при попытке отправки сообщения");
        }
    }
    private void sendPhoto(long chatId) throws IOException, TelegramApiException {
        image.getQuestionnaire(dataBase);
        File image = ResourceUtils.getFile("src/main/resources/questionnaire.png");
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(image));
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption(dataBase.getGender().getGenType() + ", " + dataBase.getName());
        sendPhoto.setReplyMarkup(getMainMenuKeyboard());
        execute(sendPhoto);
    }

}

