package com.nazdaq.payrms.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.dropbox.core.v1.DbxEntry.File;

public class ResizeImageExample {

    public static void main(String... args) throws IOException {
java.io.File input=new java.io.File("C://Users//Public//Pictures//Sample Pictures//Hydrangeas.jpg");
       // File input = new File("/tmp/duke.png");
        BufferedImage image = ImageIO.read(input);

        BufferedImage resized = resize(image, 500, 500);

        java.io.File   output = new java.io.File("C://Users//Public//Pictures//Sample Pictures//duke-resized-500x500.png");
        ImageIO.write(resized, "png", output);

    }

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

}