package common;

public enum PaletteType {
    FIRE("Fuego", 0xE03A00),
    BLUE_FIRE("Azul", 0x1040E0),
    GREEN_FIRE("Verde", 0x10B020),
    PURPLE_FIRE("Violeta", 0x8000FF),
    CYAN_FIRE("Cian", 0x00DCC8),
    RAINBOW_FIRE("Arcoíris", 0xFF00D0);
    // ------------------------------------------------------------------
    // PaletteType settings
    // ------------------------------------------------------------------
    private final String label;
    private final int previewColor;

    PaletteType(String label, int previewColor) {
        this.label = label;
        this.previewColor = previewColor;
    }
    // ---------------------- Getters
    public String getLabel() { return label;}
    public int getPreviewColor() { return previewColor; }
}