package model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

final class TileMap implements Walkable{
    private final int[][] layout;
    private boolean[][] solid;
    private final int tileSize;
    private final BufferedImage background;

    // ------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------

    TileMap(String resource, int sheetColumns, int sheetRows, int[][] layout){
        BufferedImage[] tiles =
            new SpriteSheet(resource, sheetColumns, sheetRows, sheetColumns * sheetRows).frames();
        this.tileSize = tiles[0].getWidth();
        this.layout = validateCopy(layout, tiles.length);
        this.background = render(tiles);
    }

    // ------------------------------------------------------------------
    // Queries
    // ------------------------------------------------------------------

    int rows() { return this.layout.length; }
    int columns() { return this.layout[0].length; }
    int tileSize() { return this.tileSize; }

    BufferedImage background(){ return this.background; }

    // ------------------------------------------------------------------
    // Collisions
    // ------------------------------------------------------------------

    void setSolidTiles(int... solidTiles) {
        boolean[][] grid = new boolean[rows()][columns()];
        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                grid[row][column] = contains(solidTiles, this.layout[row][column]);
            }
        }
        this.solid = grid;
    }

    // --- '#' is solid, '.' is free
    void setObstacles(String[] rows) {
        if (rows.length != rows()) {
            throw new IllegalArgumentException("Obstacles have " + rows.length + " rows, map has " + rows());
        }
        boolean[][] grid = new boolean[rows()][columns()];
        for (int row = 0; row < rows(); row++) {
            if (rows[row].length() != columns()) {
                throw new IllegalArgumentException("Obstacle row " + row + " has " + rows[row].length()
                        + " columns, map has " + columns());
            }
            for (int column = 0; column < columns(); column++) {
                char cell = rows[row].charAt(column);
                if (cell != '#' && cell != '.') {
                    throw new IllegalArgumentException("Unknown obstacle '" + cell + "' at (" + row + ", " + column + ")");
                }
                grid[row][column] = cell == '#';
            }
        }
        this.solid = grid;
    }

    // --- entity threads call this; false outside the map or on any solid cell
    @Override
    public boolean isFree(double x, double y, int width, int height) {
        if (x < 0 || y < 0 || x + width > columns() * this.tileSize || y + height > rows() * this.tileSize) {
            return false;
        }
        if (this.solid == null) {
            return true;
        }
        int firstColumn = (int) (x / this.tileSize);
        int lastColumn = (int) ((x + width - 0.001) / this.tileSize);
        int firstRow = (int) (y / this.tileSize);
        int lastRow = (int) ((y + height - 0.001) / this.tileSize);

        for (int row = firstRow; row <= lastRow; row++) {
            for (int column = firstColumn; column <= lastColumn; column++) {
                if (this.solid[row][column]) {
                    return false;
                }
            }
        }
        return true;
    }

    // ------------------------------------------------------------------
    // Internals
    // ------------------------------------------------------------------

    private BufferedImage render(BufferedImage[] tiles){
        BufferedImage image = new BufferedImage(
           columns() * this.tileSize, rows() * this.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        try {
            for (int row = 0; row < rows(); row++) {
                for (int column = 0; column < columns(); column++) {
                    g.drawImage(tiles[this.layout[row][column]],
                        column * this.tileSize, row * this.tileSize, null
                    );
                }
            }
        } finally {
            g.dispose();
        }
        return image;
    }

    private static int[][] validateCopy(int[][] layout, int tileCount){
        if (layout == null || layout.length == 0 || layout[0].length == 0){
            throw new IllegalArgumentException("Layout must have at least one row and one column");
        }

        int columns = layout[0].length;
        int[][] copy = new int[layout.length][];

        for (int row = 0; row < layout.length; row++) {
            if (layout[row].length != columns){
                throw new IllegalArgumentException("Row " + row + " has " + layout[row].length
                        + " columns, expected " + columns);
            }
            for (int column = 0; column < columns; column++) {
                int tile = layout[row][column];
                if (tile < 0 || tile >= tileCount){
                    throw new IllegalArgumentException("Tile " + tile + " at (" + row + ", " + column
                        + ") is outside 0.." + (tileCount - 1));
                }
            }
            copy[row] = layout[row].clone();
        }

        return copy;
    }

    private static boolean contains(int[] values, int value) {
        for (int candidate : values) {
            if (candidate == value) {
                return true;
            }
        }
        return false;
    }
}
