package ru.liga.datingsite.telegrambot.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "bot" )
@Data
public class BotConfig {

    String botName;

    String token;

}
