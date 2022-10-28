package ru.liga.datingsite.telegrambot.questionnaire;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QuestionnaireLike {
    int id;
    public void putLikeQuestionnaire(long chatId,int idSearch, boolean firstLike) throws IOException {
        BufferedImage image = ImageIO.read(new File("src/main/resources/questionnaires/questionnaire"+idSearch+".png"));
        Graphics g = image.createGraphics();
        g.setFont(new Font("Old Standard TT", Font.ITALIC, 20));
        g.setColor(Color.BLACK);
        if(firstLike){
            id =1;
        }else {
            id++;
        }
        g.drawString("претендент № "+id+", Любим Вами" , 300, 610);
        g.dispose();

        ImageIO.write(image, "png",
                new File("src/main/resources/questionnaires/favourite/questionnaire" + id + chatId+ ".png"));
    }
}
