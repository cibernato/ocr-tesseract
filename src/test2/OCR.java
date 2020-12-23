package test2;

import java.awt.image.*;
import java.io.*;
import java.util.HashSet;
import javax.imageio.*;

public class OCR
{

    public static BufferedImage procesar(BufferedImage origImg,int tipo ) throws IOException    {
        String filename = "Pics/epis.jpg";
        System.out.println("Holaaa");
        int index = filename.indexOf(".");
        String type = filename.substring(index + 1);

        File orig = new File(filename);
//        File gray = new File(filename.substring(0, index) + "_gray." + type);
//        File otsu = new File(filename.substring(0, index) + "_otsu." + type);
//        File nred = new File(filename.substring(0, index) + "_nred." + type);
//        File outl = new File(filename.substring(0, index) + "_outl." + type);

//        BufferedImage origImg   = ImageIO.read(orig);
        BufferedImage grayImg   = new BufferedImage(origImg.getWidth(), origImg.getHeight(), origImg.getType());
        BufferedImage otsuImg   = new BufferedImage(origImg.getWidth(), origImg.getHeight(), origImg.getType());
        BufferedImage nredImg   = new BufferedImage(origImg.getWidth(), origImg.getHeight(), origImg.getType());
        BufferedImage outlImg   = new BufferedImage(origImg.getWidth(), origImg.getHeight(), origImg.getType());

        grayImg = Process.grayscale(origImg);
        if (tipo ==1 ) return grayImg;
//        ImageIO.write(grayImg, type, gray);

        otsuImg = Process.binarize(grayImg);
        if (tipo ==2 ) return grayImg;
//        ImageIO.write(otsuImg, type, otsu);

        int times = 3;
        for (int i = 0; i < times-1; i++) {
            if (i==1) {
                nredImg = Process.reduceNoise(otsuImg);
            }
            else {
                nredImg = Process.reduceNoise(nredImg);
            }
        }
//        ImageIO.write(nredImg, type, nred);
        if (tipo ==3 ) return nredImg;
        outlImg = Process.outline(nredImg);
        //Process.outlinel(nredImg);

//        ImageIO.write(outlImg, type, outl);

      /*  HashSet<BufferedImage> segments = (HashSet<BufferedImage>) Process.segment(new Image (outlImg));

        int i = 0;
        for (BufferedImage segment : segments) {
            File segmnt = new File ( filename.substring ( 0, index ) + "_segmtn" + i + "." + type );
//            ImageIO.write ( segment, type, segmnt );
            i++;
        }*/

        return outlImg;

    }
}