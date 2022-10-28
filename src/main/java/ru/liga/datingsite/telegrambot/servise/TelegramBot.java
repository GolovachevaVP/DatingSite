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
import ru.liga.datingsite.telegrambot.enums.BotStateEnum;
import ru.liga.datingsite.telegrambot.enums.GenderTypeEnum;
import ru.liga.datingsite.telegrambot.property.BotConfig;
import ru.liga.datingsite.telegrambot.questionnaire.Questionnaire;

import java.io.File;
import java.io.IOException;

import static ru.liga.datingsite.telegrambot.button.FavouriteServiceButton.getFavouriteInlineKeyboardMarkup;
import static ru.liga.datingsite.telegrambot.button.GenderButton.createGengerButtons;
import static ru.liga.datingsite.telegrambot.button.GenderButton.getButtonName;
import static ru.liga.datingsite.telegrambot.button.MainMenuService.getMainMenuKeyboard;
import static ru.liga.datingsite.telegrambot.button.PreferencesButton.createPreferencesButtons;
import static ru.liga.datingsite.telegrambot.button.QuestionnaireServiceButton.getQuestionnaireKeyboard;
import static ru.liga.datingsite.telegrambot.button.SearchServiceButton.getSearchInlineKeyboardMarkup;
import static ru.liga.datingsite.telegrambot.menu.Menu.usingMenu;
import static ru.liga.datingsite.telegrambot.menu.MenuFavourite.usingMenuFavourite;
import static ru.liga.datingsite.telegrambot.menu.MenuQuestionnaire.usingMenuQuestionnaire;
import static ru.liga.datingsite.telegrambot.menu.MenuSearch.usingMenuSearch;


@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    DataBase dataBase = new DataBase();
    Questionnaire image = new Questionnaire();
    int idSearch = 1;
    int idFavourite = 1;


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
            String messageText = "";
            Long chatId = null;
            if (update.hasMessage() && update.getMessage().hasText()) {
                messageText = update.getMessage().getText();
                chatId = update.getMessage().getChatId();
            }
            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                chatId = callbackQuery.getMessage().getChatId();
            }

            if (dataBase.getState() == BotStateEnum.MENU || messageText.equalsIgnoreCase("Меню")) {
                execute(usingMenu(chatId, messageText, dataBase));
            } else if (dataBase.getState() == BotStateEnum.QUESTIONNAIRE) {
                execute(usingMenuQuestionnaire(chatId, messageText, dataBase));
            }
            if (dataBase.getState() == BotStateEnum.SHOWING_QUESTIONNAIRE) {
                sendPhoto(chatId);
                dataBase.setState(BotStateEnum.QUESTIONNAIRE);
            }
            if (dataBase.getState() == BotStateEnum.SEARCH) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                if (update.hasCallbackQuery()) {
                    idSearch = usingMenuSearch(getButtonName(callbackQuery), dataBase, idSearch, chatId);
                }
                if (dataBase.getState() != BotStateEnum.MENU) {
                    sendQuestionnaireSearch(chatId, idSearch);
                } else {
                    execute(usingMenu(chatId, getButtonName(callbackQuery), dataBase));
                }
            }

            if (dataBase.getState() == BotStateEnum.FAVOURITE) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                if (update.hasCallbackQuery()) {
                    idFavourite = usingMenuFavourite(getButtonName(callbackQuery), dataBase, idFavourite);
                }
                if (dataBase.getState() != BotStateEnum.MENU) {
                    sendQuestionnaireFavourite(chatId, idFavourite);
                } else {
                    execute(usingMenu(chatId, getButtonName(callbackQuery), dataBase));
                }
            }

            if (dataBase.getState() == null && messageText.equals("/start")
                    || dataBase.getState() == BotStateEnum.CHANGE_QUESTIONNAIRE) {
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
        }
    }


    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage(String.valueOf(chatId), textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка при попытке отправки сообщения");
        }
    }

    public void sendPhoto(long chatId) throws IOException, TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        if (dataBase.getState() == BotStateEnum.ASK_PREFERENCES) {
            sendPhoto.setReplyMarkup(getMainMenuKeyboard());
            image.getQuestionnaire(chatId, dataBase);
            sendPhoto.setCaption(dataBase.getGender().getGenType() + ", " + dataBase.getName());
        }
        if (dataBase.getState() == BotStateEnum.SHOWING_QUESTIONNAIRE) {
            sendPhoto.setReplyMarkup(getQuestionnaireKeyboard());
            sendPhoto.setCaption("Ваша анкета");
        }
        File image = ResourceUtils.getFile("src/main/resources/questionnaires/questionnaire" + chatId + ".png");
        sendPhoto.setPhoto(new InputFile(image));
        sendPhoto.setChatId(String.valueOf(chatId));
        execute(sendPhoto);
    }

    public void sendQuestionnaireSearch(long chatId, int id) throws IOException, TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        File image = ResourceUtils.getFile("src/main/resources/questionnaires/questionnaire" + id + ".png");
        sendPhoto.setPhoto(new InputFile(image));
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption("Претендент №" + id);
        sendPhoto.setReplyMarkup(getSearchInlineKeyboardMarkup());
        execute(sendPhoto);
    }

    public void sendQuestionnaireFavourite(long chatId, int id) throws IOException, TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        File image = ResourceUtils.getFile("src/main/resources/questionnaires/favourite/questionnaire"
                + id + chatId+ ".png");
        sendPhoto.setPhoto(new InputFile(image));
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption("Претендент №" + id);
        sendPhoto.setReplyMarkup(getFavouriteInlineKeyboardMarkup());
        execute(sendPhoto);
    }
}

