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
        int fontHeaderSize = 48;
        int fontDescriptionSize = 40;
        boolean textDoesNotFit = true;
        while (textDoesNotFit) {
            textDoesNotFit = saveQuestionnaire(chatId, dataBase,
                    new Font("Old Standard TT", Font.BOLD, fontHeaderSize),
                    new Font("Old Standard TT", Font.PLAIN, fontDescriptionSize));

            fontDescriptionSize -= 1;
            fontHeaderSize -= 1;
        }
        log.error("шрифт" + (++fontDescriptionSize));
        log.error("шрифт заголовка" + (++fontHeaderSize));
    }


    public boolean saveQuestionnaire(long chatId, DataBase dataBase, Font fontHeader, Font fontDescription) throws IOException {
        BufferedImage image = ImageIO.read(new File("src/main/resources/prerev-background.jpg"));

        List<String> text = changeDescription(dataBase.getDescription());


        List<String> linesHeader = addHeaderToImage(text, fontHeader, image);
        List<String> linesDescription = addDescriptionToImage(text, fontDescription, image);

        Graphics g = image.createGraphics();
        g.setFont(fontDescription);
        final FontMetrics fontMetricsDescription = g.getFontMetrics();
        int lineHeightDescription = fontMetricsDescription.getHeight();

        g.setFont(fontHeader);
        final FontMetrics fontMetricsHeader = g.getFontMetrics();
        int lineHeightHeader = fontMetricsHeader.getHeight();

        int linesHeight = lineHeightHeader * linesHeader.size() + lineHeightDescription * linesDescription.size();
        if (linesHeight > image.getHeight() - lineHeightDescription - 30) {
            log.error("текст больше картинки");
            return true;
        }

        for (int i = 0; i < linesHeader.size(); i++) {
            g.setColor(Color.BLACK);
            g.setFont(fontHeader);
            g.drawString(linesHeader.get(i), 50, (30 + lineHeightHeader + i * lineHeightHeader));
        }
        int indent = 35 + linesHeader.size() * lineHeightHeader;
        for (int i = 0; i < linesDescription.size(); i++) {
            g.setColor(Color.BLACK);
            g.setFont(fontDescription);
            g.drawString(linesDescription.get(i), 50, (indent + lineHeightDescription + i * lineHeightDescription));
        }


        g.dispose();
        ImageIO.write(image, "png",
                new File("src/main/resources/questionnaires/questionnaire" + chatId + ".png"));

        return false;
    }


    public List<String> addHeaderToImage(List<String> text, Font fontHeader, BufferedImage image) {
        Graphics gHeader = image.createGraphics();
        gHeader.setFont(fontHeader);
        final FontMetrics fontMetricsHeader = gHeader.getFontMetrics();

        List<String> lines = new ArrayList<>();
        String[] words = text.get(HEADER).split(" ");
        String line = "";

        for (String word : words) {
            if (fontMetricsHeader.stringWidth(line + word) > image.getWidth() - 100) {
                lines.add(line);
                line = "";
            }
            line += word + " ";
        }
        lines.add(line);
        return lines;
    }

    public List<String> addDescriptionToImage(List<String> text, Font fontDescription, BufferedImage image) {
        Graphics gDescription = image.createGraphics();
        gDescription.setFont(fontDescription);
        final FontMetrics fontMetricsDescription = gDescription.getFontMetrics();

        List<String> lines = new ArrayList<>();
        String[] words = text.get(DESCRIPTION).split(" ");
        String line = "";

        for (String word : words) {
            if (fontMetricsDescription.stringWidth(line + word) > image.getWidth() - 100) {
                lines.add(line);
                line = "";
            }
            line += word + " ";
        }
        lines.add(line);
        return lines;
    }


    public static List<String> changeDescription(String text) {
        String[] descriptionMultiLine = text.split("\\. ");
        String heading;
        String description = "";
        List<String> result = new ArrayList<>();
        if (descriptionMultiLine.length > 1) {
            heading = descriptionMultiLine[HEADER] + ".";
            result.add(heading);
            for (int i = 1; i < descriptionMultiLine.length; i++) {
                description += descriptionMultiLine[i];
                if (!descriptionMultiLine[i].contains(".") &&!descriptionMultiLine[i].contains("!")
                        &&!descriptionMultiLine[i].contains(",") ) {
                    description += ". ";
                }
            }
            result.add(description);
        } else {
            String[] descriptionOneLine = text.split(" ");
            heading = descriptionOneLine[HEADER];
            result.add(heading);
            if (descriptionOneLine.length > 1) {
                for (int i = 1; i < descriptionOneLine.length; i++) {
                    description += descriptionOneLine[i] + " ";
                }
            }
            result.add(description);
        }
        return result;
    }


}
