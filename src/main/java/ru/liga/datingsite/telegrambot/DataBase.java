package ru.liga.datingsite.telegrambot;

import lombok.Data;
import ru.liga.datingsite.telegrambot.enums.BotStateEnum;
import ru.liga.datingsite.telegrambot.enums.GenderTypeEnum;


@Data
public class DataBase {
    GenderTypeEnum gender;
    String name;
    String description;
    GenderTypeEnum preferences;
    BotStateEnum state;
}
