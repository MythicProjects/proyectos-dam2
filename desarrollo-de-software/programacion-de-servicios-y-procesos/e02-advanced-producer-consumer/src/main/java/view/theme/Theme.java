package view.theme;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

public record Theme(
        Color ink,
        Color paper,
        Color deskA,
        Color deskB,
        Color stripeA,
        Color stripeB,
        Color accent,
        Color accentText,
        Color muted,
        Color shadow,
        Color positive,
        Color negative,
        Paint[] productPaints,
        Font uiFont,
        Font numberFont) {

    // ------------------------------------------------------------------
    // Defensive copies
    // ------------------------------------------------------------------
    public Theme{
        productPaints = productPaints.clone();
    }

    @Override
    public Paint[] productPaints(){
        return productPaints.clone();
    }

    // ------------------------------------------------------------------
    // Product paints
    // ------------------------------------------------------------------
    public Paint productPaint(int productId){
        if(productId < 0 || productId >= productPaints.length) return muted;
        return productPaints[productId];
    }

    // ------------------------------------------------------------------
    // Fonts
    // ------------------------------------------------------------------
    private static final Font UI_FONT = Fonts.load(Fonts.PIXEL, 12f);
    private static final Font NUMBER_FONT = Fonts.load(Fonts.PIXEL, 22f);

    // ------------------------------------------------------------------
    // Themes
    // ------------------------------------------------------------------
    public static final Theme MAC = new Theme(
            new Color(0x000000), new Color(0xFFFFFF),
            new Color(0xFFFFFF), new Color(0x000000),
            new Color(0x000000), new Color(0xFFFFFF),
            new Color(0x000000), new Color(0xFFFFFF),
            new Color(0x808080), new Color(0x000000),
            new Color(0x000000), new Color(0x000000),
            new Paint[] {
                    new Color(0xF2B705), new Color(0x3A6FD8), new Color(0xF08A24),
                    Patterns.checker(new Color(0xE05A5A), new Color(0xFFFFFF), 2),
                    Patterns.checker(new Color(0x40A0A0), new Color(0xFFFFFF), 2),
                    Patterns.checker(new Color(0x8060B0), new Color(0xFFFFFF), 2)},
            UI_FONT, NUMBER_FONT);

    public static final Theme GAME_BOY = new Theme(
            new Color(0x0F380F), new Color(0x9BBC0F),
            new Color(0x8BAC0F), new Color(0x306230),
            new Color(0x0F380F), new Color(0x9BBC0F),
            new Color(0x0F380F), new Color(0x9BBC0F),
            new Color(0x306230), new Color(0x0F380F),
            new Color(0x0F380F), new Color(0x0F380F),
            new Paint[] {
                    new Color(0x0F380F), new Color(0x306230),
                    Patterns.checker(new Color(0x306230), new Color(0x8BAC0F), 2),
                    Patterns.diagonal(new Color(0x0F380F), new Color(0x9BBC0F), 2),
                    Patterns.checker(new Color(0x9BBC0F), new Color(0x8BAC0F), 2),
                    Patterns.checker(new Color(0x0F380F), new Color(0x8BAC0F), 2)},
            UI_FONT, NUMBER_FONT);

    public static final Theme SNES = new Theme(
            new Color(0x20204A), new Color(0xE8E8F0),
            new Color(0xC8C8D8), new Color(0xE8E8F0),
            new Color(0x5A5AC0), new Color(0xE8E8F0),
            new Color(0x4A3A9A), new Color(0xFFFFFF),
            new Color(0x8A8AA0), new Color(0x20204A),
            new Color(0x20204A), new Color(0x20204A),
            new Paint[] {
                    new Color(0xF2C200), new Color(0x2850C8), new Color(0x2E9A3E),
                    Patterns.checker(new Color(0xE05A5A), new Color(0xFFFFFF), 2),
                    Patterns.checker(new Color(0x7A7AD0), new Color(0xFFFFFF), 2),
                    Patterns.checker(new Color(0x5A4AA0), new Color(0xFFFFFF), 2)},
            UI_FONT, NUMBER_FONT);

    public static final Theme RETRO8 = new Theme(
            new Color(0xE8E8F8), new Color(0x2E2A78),
            new Color(0x3A3690), new Color(0x2E2A78),
            new Color(0x9A96E8), new Color(0x2E2A78),
            new Color(0xB8C860), new Color(0x2E2A78),
            new Color(0x7A76C0), new Color(0x1A1850),
            new Color(0x9CDC70), new Color(0xE87870),
            new Paint[] {
                    new Color(0xB8C060), new Color(0x6AA8B8), new Color(0x8CD07C),
                    Patterns.checker(new Color(0xE08080), new Color(0x2E2A78), 2),
                    Patterns.checker(new Color(0x9AA0D8), new Color(0x2E2A78), 2),
                    Patterns.checker(new Color(0xC08060), new Color(0x2E2A78), 2)},
            UI_FONT, NUMBER_FONT);
}
