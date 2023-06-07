import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


@Getter
@AllArgsConstructor
public class SimplePage {

    private String filePath;
    private BufferedImage imp;
    private String page;
    private int number;

    public BufferedImage getImp() {

        if(imp.getWidth() < imp.getHeight() ) {
            if("A".equals(page)){
                return rotateImageByDegrees(imp, 270);
            } else {
                return rotateImageByDegrees(imp, 90);
            }
           
        }
        return imp;
    }

    public BufferedImage rotateImageByDegrees(BufferedImage image, double angle) {
        final double rads = Math.toRadians(angle);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads,0, 0);
        at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(image,rotatedImage);
        return rotatedImage;

    //    int w = bimg.getWidth();    
    //     int h = bimg.getHeight();

    //     BufferedImage rotated = new BufferedImage(w, h, bimg.getType());  
    //     Graphics2D graphic = rotated.createGraphics();
    //     graphic.rotate(Math.toRadians(angle), w/2, h/2);
    //     graphic.drawImage(bimg, null, 0, 0);
    //     graphic.dispose();
    //     return rotated;
    }

    public static SimplePage empty() {
        var imp1 = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = imp1.createGraphics();
        graphics.setPaint(new Color(255, 255, 255));
        graphics.fillRect(0, 0, imp1.getWidth(), imp1.getHeight());
        return new SimplePage("", imp1, "", 0);
    }
}
