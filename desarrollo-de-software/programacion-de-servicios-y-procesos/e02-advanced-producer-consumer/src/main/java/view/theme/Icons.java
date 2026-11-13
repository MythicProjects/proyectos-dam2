package view.theme;

public final class Icons {
    private static final int SCALE = 2;

    private Icons() {}

    // ------------------------------------------------------------------
    // Simulation controls
    // ------------------------------------------------------------------
    public static final PixelIcon PLAY = new PixelIcon(new String[] {
            "#....",
            "##...",
            "###..",
            "####.",
            "###..",
            "##...",
            "#...."}, SCALE, null);

    public static final PixelIcon PAUSE = new PixelIcon(new String[] {
            "##.##",
            "##.##",
            "##.##",
            "##.##",
            "##.##",
            "##.##",
            "##.##"}, SCALE, null);

    public static final PixelIcon FAST_FORWARD = new PixelIcon(new String[] {
            "#...#...",
            "##..##..",
            "###.###.",
            "########",
            "###.###.",
            "##..##..",
            "#...#..."}, SCALE, null);

    // ------------------------------------------------------------------
    // Actions
    // ------------------------------------------------------------------
    public static final PixelIcon PLUS = new PixelIcon(new String[] {
            "..#..",
            "..#..",
            "#####",
            "..#..",
            "..#.."}, SCALE, null);

    public static final PixelIcon UPGRADE = new PixelIcon(new String[] {
            "..#..",
            ".###.",
            "#####",
            "..#..",
            "..#..",
            "..#.."}, SCALE, null);

    public static final PixelIcon LOCK = new PixelIcon(new String[] {
            ".###.",
            "#...#",
            "#...#",
            "#####",
            "##.##",
            "#####"}, SCALE, null);

    // ------------------------------------------------------------------
    // Scene
    // ------------------------------------------------------------------
    public static final PixelIcon PERSON = new PixelIcon(new String[] {
            ".##.",
            ".##.",
            "####",
            "####",
            ".##.",
            ".##.",
            ".#.#"}, SCALE, null);

    public static final PixelIcon MEGAPHONE = new PixelIcon(new String[] {
            "....#...",
            "...##..#",
            "#####...",
            "#####.##",
            "#####...",
            "...##..#",
            "....#..."}, SCALE * 2, null);

    public static final PixelIcon FACTORY = new PixelIcon(new String[] {
            "#.......",
            "#..#..#.",
            "#.##.##.",
            "########",
            "########",
            "##.##.##",
            "########"}, SCALE, null);
}
