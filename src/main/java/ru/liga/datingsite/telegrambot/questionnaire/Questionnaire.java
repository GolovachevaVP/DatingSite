package ru.liga.datingsite.telegrambot.questionnaire;

import lombok.extern.slf4j.Slf4j;
import ru.liga.datingsite.telegrambot.DataBase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Questionnaire {
    private static final int HEADER = 0;
    private static final int DESCRIPTION = 1;

    public void getQuestionnaire(long chatId, DataBase dataBase) throws IOException {
        Font fontHeader = new Font("Old Standard TT", Font.BOLD, 100);
        int fontDescriptionSize = 40;
        boolean textDoesNotFit = true;
        while (textDoesNotFit) {
            textDoesNotFit = saveQuestionnaire(chatId, dataBase, fontHeader, new Font("Old Standard TT", Font.PLAIN, fontDescriptionSize));
            fontDescriptionSize -= 1;
        }
        log.error("шрифт" +(++fontDescriptionSize));
    }


    public boolean saveQuestionnaire(long chatId, DataBase dataBase, Font fontHeader, Font fontDescription) throws IOException {
        BufferedImage image = ImageIO.read(new File("src/main/resources/prerev-background.jpg"));

        List<String> text = changeDescription(dataBase.getDescription());


        List<String> lines = addTextToImage(text, fontDescription, image);

        Graphics g = image.createGraphics();
        g.setFont(fontDescription);
        final FontMetrics fontMetrics = g.getFontMetrics();

        int lineHeight = fontMetrics.getHeight();
        int linesHeight = lineHeight * lines.size();
        if (linesHeight > image.getHeight() - lineHeight) {
            log.error("текст больше картинки");
            return true;
        }


        for (int i = 0; i < lines.size(); i++) {
            g.setFont(fontDescription);
            g.setColor(Color.BLACK);
            g.drawString(lines.get(i), 20, (lineHeight + i * lineHeight));
        }

        g.dispose();
        ImageIO.write(image, "png",
                new File("src/main/resources/questionnaires/questionnaire" + chatId + ".png"));

        return false;
    }


    public List<String> addTextToImage(List<String> text, Font font, BufferedImage image) {
        Graphics g = image.createGraphics();
        g.setFont(font);
        final FontMetrics fontMetrics = g.getFontMetrics();

        List<String> lines = new ArrayList<>();

        for (String str : text) {
            String[] words = str.split(" ");
            String line = "";

            for (int i = 0; i < words.length; i++) {
                if (fontMetrics.stringWidth(line + words[i]) > image.getWidth() - 40) {
                    lines.add(line);
                    line = "";
                }

                line += words[i] + " ";
            }
            lines.add(line);
        }

        return lines;
    }

    public static List<String> changeDescription(String text) {
        String[] descriptionMultiLine = text.split("\\.");
        String heading;
        String description = "";
        List<String> result = new ArrayList<>();
        if (descriptionMultiLine.length > 1) {
            heading = descriptionMultiLine[HEADER] + ".";
            result.add(heading);
            for (int i = 1; i < descriptionMultiLine.length; i++) {
                description += descriptionMultiLine[i] + ".";
            }
            result.add(description);
        } else {
            String[] descriptionOneLine = text.split(" ");
            heading = descriptionOneLine[HEADER];
            result.add(heading);
            if (descriptionOneLine.length > 1) {
                for (int i = 1; i < descriptionOneLine.length; i++) {
                    description += descriptionOneLine[i];
                }
            }
            result.add(description);
        }
        return result;
    }


}
