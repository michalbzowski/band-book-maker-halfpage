import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Pdf {
    private List<TwoPages> twoPagesList;

    public Pdf(List<TwoPages> twoPagesList) {
        this.twoPagesList = twoPagesList;
    }

    public void saveTo(String filePath, String directory) throws IOException, DocumentException {
        int i = 0;
        Document document = new Document();
        document.setMargins(0, 0, 0, 0);
        var writer = PdfWriter.getInstance(document, new FileOutputStream(filePath + "/" + directory + ".pdf"));
        document.open();

        // Title page

        // #ImageIO.write(image, "jpg", titlePage);
        // #extracted(document, titlePage);

        for (TwoPages twoPages : twoPagesList) {
            var bufferedImageA = twoPages.getBufferedImageA();
            File imgfile = new File(filePath + "/" + String.format("P_%03d.jpg", i++));
            ImageIO.write(bufferedImageA, "jpg", imgfile);
            extracted(document, imgfile);

            var bufferedImageB = twoPages.getBufferedImageB();
            imgfile = new File(filePath + "/" + String.format("P_%03d.jpg", i++));
            ImageIO.write(bufferedImageB, "jpg", imgfile);
            extracted(document, imgfile);
        }
        document.close();
        writer.close();
        System.out.println("CONVERTER STOPTED.....");
    }

    // javax.imageio.ImageIO.write(imp, "jpg", new java.io.File("C:\\Users\\Monika
    // Bzowska\\Documents\\Piesek.jpg"));
    private static void extracted(Document document, File imgfile) throws IOException, DocumentException {
        var instance = Image.getInstance(imgfile.getPath());
        instance.scaleToFit(document.getPageSize());
        document.add(instance);
        document.newPage();
        // imgfile.delete();
    }
}
