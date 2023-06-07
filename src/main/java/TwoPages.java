import lombok.Getter;

import java.awt.image.BufferedImage;

@Getter
public class TwoPages {
    private String title;
    private BufferedImage bufferedImageA;
    private BufferedImage bufferedImageB;

    public TwoPages(String title, BufferedImage bufferedImageA, BufferedImage bufferedImageB) {
        this.title = title;
        this.bufferedImageA = bufferedImageA;
        this.bufferedImageB = bufferedImageB;
    }
}
