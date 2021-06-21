package com.java.multithreading.imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class SequetialProcessing {

  public static String imagePath = "/Users/sandishkumarhn/Downloads/pic2.jpg";

  public static void main(String[] args) throws IOException, InterruptedException {
    BufferedImage originalImage = ImageIO.read(new File(imagePath));
    BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

    recolorMultiThreade(originalImage, resultImage, 4);

    File outFile = new File("/Users/sandishkumarhn/Downloads/pic2-modi2.jpg");
    ImageIO.write(resultImage, "jpg", outFile);
  }

  public static void recolorMultiThreade(BufferedImage originalImage, BufferedImage resultImage, int numThreads)
      throws InterruptedException {
    List<Thread> threads = new ArrayList<>();

    int witdh = originalImage.getWidth();
    int height = originalImage.getHeight() - numThreads;

    for(int i = 0; i < numThreads; i++) {
      int threadMultiplier = i;

      Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
          int leftCorner = 0;
          int rightCorner = height * threadMultiplier;

          reColorImage(originalImage, resultImage,  leftCorner, rightCorner, witdh, height);
        }
      });

      threads.add(thread);
    }

    for(Thread  thread: threads) {
      thread.start();
    }
    for(Thread  thread: threads) {
      thread.join();
    }

  }
  public static void recolroSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
    reColorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());

  }
  public static void reColorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner,
      int width, int height) {
    for(int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
      for(int y = topCorner; y < topCorner + height && x < originalImage.getHeight(); y++) {
        reColorPixels(originalImage, resultImage, x, y);
      }
    }
  }
  public static void reColorPixels(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {

    int rgb = originalImage.getRGB(x, y);

    int red = getRed(rgb);
    int green = getGreen(rgb);
    int blue = getBlue(rgb);


    int newRed;
    int newBlue;
    int newGreen;

    if(isShadedGray(red, green, blue)) {
      newRed = Math.min(255, red + 10);
      newGreen = Math.max(0, green - 80);
      newBlue = Math.max(0, blue - 20);
    } else {
      newRed = red;
      newGreen = green;
      newBlue = blue;
    }

    int newRGB = createRGBtFromColors(newRed, newGreen, newBlue);

    setRGB(resultImage, x, y, newRGB);
  }

  public static void setRGB(BufferedImage image, int x, int y, int rgb) {
    image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
  }
  public static boolean isShadedGray(int red, int green, int blue) {
    return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
  }

  public static int createRGBtFromColors(int red, int gree, int blue) {
    int rgb = 0;


    rgb |= blue;
    rgb |= blue << 8;
    rgb |= blue << 16;

    rgb |= 0xFF000000;

    return rgb;
  }
  public static int getRed(int rgb) {
    return rgb & 0x00FF0000 >> 16;
  }

  public static int getGreen(int rgb) {
    return rgb & 0x0000FF00 >> 8;
  }

  public static int getBlue(int rgb) {
    return rgb & 0x000000FF;
  }

}
