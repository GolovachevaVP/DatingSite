package ru.liga.datingsite.telegrambot.questionnaire;

import lombok.extern.slf4j.Slf4j;
import ru.liga.datingsite.telegrambot.DataBase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class Questionnaire {
    private static final int HEADER = 0;
    private static final int DESCRIPTION = 1;


    public void getQuestionnaire(DataBase dataBase) throws IOException {
        BufferedImage image = ImageIO.read(new File("src/main/resources/prerev-background.jpg"));

        List<String> text = changeDescription(dataBase.getDescription());

        Font fontHead = new Font("Old Standard TT", Font.BOLD, 100);
        Font fontDescr = new Font("Old Standard TT", Font.PLAIN, 40);

       // Font fontHeader = fontCreateFontToFit(fontHead, text.get(HEADER), image);
        List<String> lines = addTextToImage(text, fontDescr, image);

        String result="";
        for(String str1: lines){
            result = str1 + "\n";
        }

        //Font fontDescription = fontCreateFontToFit(fontDescr, result, image);



        Graphics g = image.createGraphics();
        g.setFont(fontDescr);
        final FontMetrics fontMetrics = g.getFontMetrics();

        int lineHeight = fontMetrics.getHeight();
        int linesHeight = lineHeight*lines.size();
        if(linesHeight>image.getHeight()-lineHeight){
            log.error("текст больше картинки");


        }
        for (int i = 0; i < lines.size(); i++) {
                g.setFont(fontDescr);
                g.setColor(Color.BLACK);
                g.drawString(lines.get(i), 20, (lineHeight + i * lineHeight));


        }



        g.dispose();
//
//       // ArrayList<String> res = addTextToImage(text, fontDescription, image);
        ImageIO.write(image, "png", new File("src/main/resources/questionnaire.png"));



    }

    public static Font fontCreateFontToFit(Font baseFont, String text, BufferedImage image) {
        Font newFont = baseFont;

        FontMetrics ruler = image.getGraphics().getFontMetrics(baseFont);
        GlyphVector vector = baseFont.createGlyphVector(ruler.getFontRenderContext(), text);

        Shape outline = vector.getOutline(0, 0);

        double expectedWidth = outline.getBounds().getWidth();
        double expectedHeight = outline.getBounds().getHeight();
        int lineHeight = ruler.getHeight();


        boolean textFits = image.getWidth() >= expectedWidth && image.getHeight() >= expectedHeight;

        if (!textFits) {
            double widthBasedFontSize = (baseFont.getSize2D() * (image.getWidth()-lineHeight)) / expectedWidth;
            double heightBasedFontSize = (baseFont.getSize2D() * (image.getHeight()-lineHeight)) / expectedHeight;

            double newFontSize = widthBasedFontSize < heightBasedFontSize ? widthBasedFontSize : heightBasedFontSize;
            newFont = baseFont.deriveFont(baseFont.getStyle(), (float) newFontSize);
        }
        return newFont;
    }


    public List<String> addTextToImage(List<String> text, Font font, BufferedImage image) {
        Graphics g = image.createGraphics();
        g.setFont(font);
        final FontMetrics fontMetrics = g.getFontMetrics();

        List<String> lines = new ArrayList<>();
        int lineHeight = fontMetrics.getHeight();

        for (String str : text) {
            String[] words = str.split(" ");
            String line = "";

            for (int i = 0; i < words.length; i++) {
                if (fontMetrics.stringWidth(line + words[i]) > image.getWidth()-20) {
                    lines.add(line);
                    line = "";
                }

                line += words[i] + " ";
            }
           lines.add(line);
        }

//        for (int i = 0; i < lines.size(); i++) {
//            if(i==0){
//                g.setFont(font);
//                g.setColor(Color.BLACK);
//                g.drawString(lines.get(i), 20,  lineHeight);
//            } else {
//                g.setFont(font);
//                g.setColor(Color.BLACK);
//                g.drawString(lines.get(i), 20, (lineHeight + i * lineHeight));
//            }
//        }
//        g.dispose();
        //ImageIO.write(image, "png", new File("src/main/resources/questionnaire.png"));

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
