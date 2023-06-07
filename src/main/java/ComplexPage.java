import lombok.Builder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Arrays;
import java.util.stream.Collectors;


@Builder
public class ComplexPage {

        // 1cm = 0,3937 cali
        // 2,154 cm = 1 cal
        // 21 cm = 8,268 cal
        // 29,7 = 11,693 cal
        public static double dpi = 300.0;
        static double a4Width = 8.268;
        static double a4WidthPixels = a4Width * dpi;

        static double a4Height = 11.693;
        static double a4HeightPixels = a4Height * dpi;


    SimplePage _1;
    SimplePage _2;
    SimplePage _3;
    SimplePage _4;

    public TwoPages createTwoSidedPdf() {
 
        var bufferedImageA = new BufferedImage((int) a4WidthPixels, (int) a4HeightPixels, _1.getImp().getType());
        var bufferedImageB = new BufferedImage((int) a4WidthPixels, (int) a4HeightPixels, _1.getImp().getType());

        var graphicsA = bufferedImageA.getGraphics();
        graphicsA.setColor(Color.WHITE);
        graphicsA.fillRect(0, 0, (int) a4WidthPixels, (int) a4HeightPixels);
        var graphicsB = bufferedImageB.getGraphics();
        graphicsB.setColor(Color.WHITE);
        graphicsB.fillRect(0, 0, (int) a4WidthPixels, (int) a4HeightPixels);

        int scaleType =  java.awt.Image.SCALE_DEFAULT;
        int margin =200;

       var bim1 = _1.getImp();
       var bim2 = _2.getImp();
       var bim3 = _3.getImp();
       var bim4 = _4.getImp();


        var scaled1 = bim1.getScaledInstance((int)countWidth(bim1, a4WidthPixels, a4HeightPixels, margin), (int) countHeight(bim1, a4WidthPixels, a4HeightPixels, margin), scaleType);
        var scaled2 = bim2.getScaledInstance((int)countWidth(bim1, a4WidthPixels, a4HeightPixels, margin), (int) countHeight(bim2, a4WidthPixels, a4HeightPixels, margin), scaleType);
        var scaled3 = bim3.getScaledInstance((int)countWidth(bim1, a4WidthPixels, a4HeightPixels, margin), (int) countHeight(bim3, a4WidthPixels, a4HeightPixels, margin), scaleType);
        var scaled4 = bim4.getScaledInstance((int)countWidth(bim1, a4WidthPixels, a4HeightPixels, margin), (int) countHeight(bim4, a4WidthPixels, a4HeightPixels, margin), scaleType);

        // // Create a buffered image with transparency
        // BufferedImage bimage = new BufferedImage(scaled2.getWidth(null), scaled2.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);

        // // Draw the image on to the buffered image
        // Graphics2D bGr = bimage.createGraphics();
        // bGr.drawImage(scaled2, 0, 0, null);
        // bGr.dispose();
       
        ImageObserver imageObserver = (img, infoflags, x, y, width, height) -> true;

        graphicsA.drawImage(scaled1, margin, (int) 0, imageObserver);
        graphicsB.drawImage(scaled2, 0, (int) 0, imageObserver);
        graphicsA.drawImage(scaled3, margin, (int) a4HeightPixels / 2, imageObserver);
        graphicsB.drawImage(scaled4, 0, (int) a4HeightPixels / 2, imageObserver);

        String title = "";
        String[] splitted = _1.getFilePath().split("/");
        if(splitted.length > 1) {
            var collect = Arrays.stream(splitted).collect(Collectors.toList());
            title = collect.get(collect.size() - 2);
            
            var fontSize = 20;
            graphicsA.setFont(new Font("Arial", Font.BOLD, fontSize));
            graphicsB.setFont(new Font("Arial", Font.BOLD, fontSize));

            graphicsA.setColor(Color.LIGHT_GRAY);
            // graphicsA.fillRect(margin, fontSize + 5, title.length() * fontSize, -fontSize);
            // graphicsA.fillRect(margin, (int) a4HeightPixels / 2 + fontSize + 5, title.length() * fontSize, -fontSize);
            graphicsA.setColor(Color.BLACK);
            graphicsA.drawString(title, margin, fontSize + 5);
            graphicsA.drawString(title, margin,  (int) a4HeightPixels / 2 + fontSize + 5);
            
            graphicsB.setColor(Color.LIGHT_GRAY);
            // graphicsB.fillRect(margin, fontSize + 5, title.length() * fontSize, -fontSize);
            // graphicsB.fillRect(margin, (int) a4HeightPixels / 2 + fontSize + 5, title.length() * fontSize, -fontSize);
            graphicsB.setColor(Color.BLACK);
            graphicsB.drawString(title, margin, fontSize + 5);
            graphicsB.drawString(title, margin,  (int) a4HeightPixels / 2 + fontSize + 5);
        }
        graphicsA.setColor(Color.BLACK);
        graphicsA.drawLine(0, (int) a4HeightPixels / 2, (int) a4WidthPixels, (int) a4HeightPixels / 2);
        graphicsA.drawRect(0, 0, (int) a4WidthPixels, (int) a4HeightPixels);

        graphicsB.setColor(Color.BLACK);
        graphicsB.drawLine(0, (int) a4HeightPixels / 2, (int) a4WidthPixels, (int) a4HeightPixels / 2);
        graphicsB.drawRect(0, 0, (int) a4WidthPixels, (int) a4HeightPixels);


        return new TwoPages(title, bufferedImageA, bufferedImageB);

    }

    double countWidth(BufferedImage buf, double a4WidthPixels, double a4HeightPixels, int margin) {
        if(countHeight(buf, a4WidthPixels, a4HeightPixels, margin) < a4HeightPixels / 2) {
            return a4WidthPixels - margin;

        } else {
            var hpercent = (a4HeightPixels) / buf.getHeight();
            var width = buf.getWidth() * hpercent;
            if(width >= a4WidthPixels - margin) {
                return a4WidthPixels - margin;
            }
            return width;
        }   
     }

    double countHeight(BufferedImage buf, double a4WidthPixels, double a4HeightPixels, int margin) {
        System.out.println("W: " + buf.getWidth());
        System.out.println("H: " + buf.getHeight());
        var wpercent = (a4WidthPixels - margin) / buf.getWidth();
        System.out.println("W%: " + wpercent);
        if(buf.getHeight() * wpercent >= a4HeightPixels / 2){ //dzielone na 2 bo dwa utworki na strone - hardcoded
            return a4HeightPixels / 2;
        }
        return buf.getHeight() * wpercent;
    }


}
