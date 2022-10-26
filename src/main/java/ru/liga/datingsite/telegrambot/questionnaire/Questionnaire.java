package ru.liga.datingsite.telegrambot.questionnaire;

import ru.liga.datingsite.telegrambot.DataBase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Questionnaire {
    private static final int HEADER = 0;
    private static final int DESCRIPTION = 1;


    public void getQuestionnaire(DataBase dataBase) throws IOException {
        BufferedImage image = ImageIO.read(new File("src/main/resources/prerev-background.jpg"));

        ArrayList<String> text = changeDescription(dataBase.getDescription());

        Font fontHead = new Font("Old Standard TT", Font.BOLD, 100);
        Font fontDescr = new Font("Old Standard TT", Font.PLAIN, 18);

        Font fontHeader = fontCreateFontToFit(fontHead, text.get(HEADER), image);
         addTextToImage(text, fontHeader,fontDescr, image);

//        Font fontDescription = fontCreateFontToFit(fontDescr, text.get(DESCRIPTION), image);
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


    public void addTextToImage(ArrayList<String> text,Font fontHeader, Font fontDescription, BufferedImage image) {
        Graphics gHeader = image.createGraphics();
        gHeader.setFont(fontHeader);
        final FontMetrics fontMetricsHeader = gHeader.getFontMetrics();

        Graphics gDescription = image.createGraphics();
        gDescription.setFont(fontDescription);
        final FontMetrics fontMetricsDescription = gDescription.getFontMetrics();

        ArrayList<String> lines = new ArrayList<>();
        int lineHeightHeader = fontMetricsHeader.getHeight();
        int lineHeightDescription = fontMetricsDescription.getHeight();

        for (int i=0; i<text.size(); i++) {
            if (i == 0) {
                String[] words = text.get(i).split(" ");
                String line = "";

                for (int j = 0; j < words.length; i++) {
                    if (fontMetricsHeader.stringWidth(line + words[j]) > image.getWidth() - 40) {
                        lines.add(line);
                        line = "";
                    }

                    line += words[j] + " ";
                }

                lines.add(line);
            } else {
                String[] words = text.get(i).split(" ");
                String line = "";

                for (int k = 0; k < words.length; i++) {
                    if (fontMetricsHeader.stringWidth(line + words[k]) > image.getWidth() - 40) {
                        lines.add(line);
                        line = "";
                    }

                    line += words[k] + " ";
                }

                lines.add(line);
            }
        }


        for (int i = 0; i < lines.size(); i++) {
            if(i==0){
                gHeader.setFont(fontHeader);
                gHeader.setColor(Color.BLACK);
                gHeader.drawString(lines.get(i), 20,  lineHeightHeader);
            } else {
                gDescription.setFont(fontDescription);
                gDescription.setColor(Color.BLACK);
                gDescription.drawString(lines.get(i), 20, (lineHeightDescription + i * lineHeightDescription));
            }
        }
        gHeader.dispose();
        gDescription.dispose();
        //ImageIO.write(image, "png", new File("src/main/resources/questionnaire.png"));

    }

    public static ArrayList<String> changeDescription(String text) {
        String[] descriptionMultiLine = text.split("\\.");
        String heading;
        String description = "";
        ArrayList<String> result = new ArrayList<>();
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
