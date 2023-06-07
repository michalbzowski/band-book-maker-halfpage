import com.itextpdf.text.DocumentException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException, DocumentException {
        var path = "C:\\Users\\Monika Bzowska\\Documents\\NUTY_MARSZOWE\\STEP_2\\";
        // var path = "C:\\Users\\Monika Bzowska\\Downloads\\ChomikBox\\Finale (OnagerAttack)\\STEP_2\\";

        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for (String directory : directories) {
            System.out.println("Directory: " + directory);
            var strings = listFilesUsingFilesList(path + directory, "pdf");
            extracted(path, directory, strings);
        }

        System.out.println();

    }

    private static void extracted(String path, String directory, List<String> strings) throws IOException, DocumentException {
        // if(!directory.contains("Sax Alt 2 Eb")){
            //   return;
        // }
        String[] page = {"A", "B"};
        int i = 0;
        SimplePage[] simplePages = new SimplePage[strings.size()];
        for (String s : strings) {

            var pathToImage = path + "/" + directory + "/" + s;
            File file = new File(pathToImage.toString());
            PDDocument document = PDDocument.load(file);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            // Map<String, PDFont> findFontInForm = findFontInForm(document);
            PDPageTree pages = document.getDocumentCatalog().getPages();
            BufferedImage myPicture = null;
            while(pages.iterator().hasNext()) {
                PDPage pdfPage = pages.iterator().next();
                PDResources resources = pdfPage.getResources();
                // findFontInForm.values().forEach(f -> resources.add(f));
                // myPicture = pdfPage.convertToImage(BufferedImage.TYPE_3BYTE_BGR, (int)ComplexPage.dpi);
                myPicture = pdfRenderer.renderImageWithDPI(0, (int)ComplexPage.dpi, ImageType.RGB);
                break;
            }
            document.close();
            System.out.println("- path to image: " + pathToImage);
            simplePages[i] = new SimplePage(pathToImage, myPicture, page[i % 2], i + 1);
            i++;

        }
        ComplexPage complexPage = null;
        List<TwoPages> twoPagesList = new ArrayList<>();
        for (int n = 0; n < strings.size(); n = n + 4) {
            System.out.println("- page: " + n);
            var builder = ComplexPage.builder();
            
            // if(n == 0){
            //     complexPage = builder
            //     ._1(getTitlePage(directory))
            //     ._2(SimplePage.empty())
            //     ._3(getSimpleImage(simplePages, 2 + n))
            //     ._4(getSimpleImage(simplePages, 3 + n))
            //     .build();
            // } else {
                complexPage = builder
                ._1(getSimpleImage(simplePages, 1 + n))
                ._2(getSimpleImage(simplePages, 2 + n))
                ._3(getSimpleImage(simplePages, 3 + n))
                ._4(getSimpleImage(simplePages, 4 + n))
                .build();
            // }
            
                    
            TwoPages twoPages = complexPage.createTwoSidedPdf();
            twoPagesList.add(twoPages);
        }
        new Pdf(twoPagesList).saveTo("C:\\Users\\Monika Bzowska\\Documents\\NUTY_MARSZOWE\\PRINT_READY\\", directory);
        // new Pdf(twoPagesList).saveTo("C:\\Users\\Monika Bzowska\\Downloads\\ChomikBox\\Finale (OnagerAttack)\\PRINT_READY\\", directory);
    }

    private static SimplePage getTitlePage(String directory) {
        var image = new BufferedImage((int)ComplexPage.a4WidthPixels / 2, (int)ComplexPage.a4HeightPixels / 2, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, (int)ComplexPage.a4WidthPixels / 2, (int)ComplexPage.a4HeightPixels / 2);
        g.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD, 18);
        g.setFont(font);
        g.drawString(directory, (int) ComplexPage.a4WidthPixels / 4, (int) ComplexPage.a4HeightPixels / 4);
        g.dispose();
        return new SimplePage("", image, "A", 0);
    }

    private static SimplePage getSimpleImage(SimplePage[] simplePages, int i) {
        if (i > simplePages.length) {
            return SimplePage.empty();
        }
        return simplePages[i - 1];
    }

    static List<String> listFilesUsingFilesList(String dir, String ext) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dir))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .filter(fileName -> fileName.toString().contains(ext))
                    .map(Path::toString)
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    /*
     * Method searches and returns font object by the passed font name.
     */
    // public static Map<String, PDFont> findFontInForm(PDDocument pdfDoc) {
    //     List<PDPage> pages = pdfDoc.getDocumentCatalog().getPages();
    //     PDResources res = pages.stream().findAny().get().getResources();
    //     return res.getFonts();
       
    // }
}
