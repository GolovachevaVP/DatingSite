package ru.liga.datingsite.telegrambot.enums;

import lombok.Getter;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum GenderTypeEnum {
    MALE("Сударъ"),
    FEMALE("Сударыня"),
    ALL("Всех");
    final String genType;

    GenderTypeEnum(String genType) {
        this.genType = genType;
    }

    private static final Map<String, GenderTypeEnum> roles = Arrays
            .stream(GenderTypeEnum.values())
            .map(r -> new AbstractMap.SimpleEntry<>(r.genType, r))
            .collect(
                    Collectors.toMap(
                            AbstractMap.SimpleEntry::getKey,
                            AbstractMap.SimpleEntry::getValue
                    )
            );

    public static GenderTypeEnum fromString(String algType) {
        GenderTypeEnum genderTypeEnum = roles.get(algType);
        if (genderTypeEnum == null) {
            throw new RuntimeException("Неверный запрос");
        }
        return genderTypeEnum;
    }
}
