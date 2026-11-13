package view.theme;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public final class Fonts {
    public static final String PIXEL = "/fonts/PixelifySans-Regular.ttf";

    private Fonts() {}

    // ------------------------------------------------------------------
    // Loading
    // ------------------------------------------------------------------
    public static Font load(String resource, float size){
        try(InputStream in = Fonts.class.getResourceAsStream(resource)){
            if(in == null) throw new IOException("Font not found: " + resource);
            return Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(size);
        }catch (IOException | FontFormatException e){
            return new Font(Font.MONOSPACED, Font.PLAIN, Math.round(size));
        }
    }
}
