package model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import contract.Direction;
import contract.IModel;
import contract.IModelListener;

public class Model implements IModel {

    private static final double DEFAULT_PLAYER_SPEED = 48;   // px/s: 3 tiles

    private final SpritePool pool = new SpritePool();
    private final List<Entity> entities = new CopyOnWriteArrayList<>();

    private Entity player;
    private TileMap map;
    private volatile IModelListener listener;

    // ------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------

    public Model() {}

    // ------------------------------------------------------------------
    // Configuration (not in the contract)
    // ------------------------------------------------------------------

    // --- only main calls these, before start()
    public void setTileMap(String resource, int sheetColumns, int sheetRows, int[][] layout) {
        this.map = new TileMap(resource, sheetColumns, sheetRows, layout);
    }

    public void setSolidTiles(int... tiles) {
        if (this.map == null) {
            throw new IllegalStateException("Call setTileMap() before setSolidTiles()");
        }
        this.map.setSolidTiles(tiles);
    }

    public void addPlayer(String sprite, int x, int y, int fps) {
        if (this.player != null) {
            throw new IllegalStateException("There is already a player");
        }
        this.player = createEntity("Player", sprite, x, y, fps,
                EntityState.IDLE, DEFAULT_PLAYER_SPEED, this::playerBlocked);
        this.entities.add(0, this.player);
    }

    // --- NPCs walk in place; bumping does nothing yet
    public void addNpc(String sprite, int x, int y, int fps) {
        this.entities.add(createEntity(sprite + " #" + this.entities.size(), sprite, x, y, fps,
                EntityState.WALK, 0, () -> {}));
    }

    // ------------------------------------------------------------------
    // Lifecycle
    // ------------------------------------------------------------------

    @Override
    public void setListener(IModelListener listener) {
        this.listener = listener;
    }

    @Override
    public void start() {
        for (Entity entity : this.entities) {
            entity.start();
        }
    }

    // ------------------------------------------------------------------
    // Scene
    // ------------------------------------------------------------------

    @Override
    public BufferedImage background() {
        if (this.map == null) {
            throw new IllegalStateException("No tile map: call setTileMap() in main");
        }
        return copyOf(this.map.background());
    }

    // ------------------------------------------------------------------
    // Entities
    // ------------------------------------------------------------------

    // --- render thread calls these
    @Override
    public int entityCount() {
        return this.entities.size();
    }

    @Override
    public BufferedImage frame(int index) {
        return copyOf(this.entities.get(index).currentFrame());
    }

    @Override public int x(int index) { return this.entities.get(index).x(); }
    @Override public int y(int index) { return this.entities.get(index).y(); }
    @Override public String name(int index) { return this.entities.get(index).name(); }
    @Override public int fps(int index) { return this.entities.get(index).fps(); }

    // ------------------------------------------------------------------
    // Player
    // ------------------------------------------------------------------

    // --- Swing thread calls these (keys and sliders)
    @Override public int playerFps() { return requirePlayer().fps(); }
    @Override public void setPlayerFps(int fps) { requirePlayer().setFps(fps); }

    @Override public int playerMoveSpeed() { return (int) requirePlayer().speed(); }
    @Override public void setPlayerMoveSpeed(int speed) { requirePlayer().setSpeed(speed); }

    @Override public void movePlayer(Direction direction) { requirePlayer().walk(direction); }
    @Override public void stopPlayer() { requirePlayer().stop(); }

    // ------------------------------------------------------------------
    // Internals
    // ------------------------------------------------------------------

    private Entity createEntity(String name, String sprite, int x, int y, int fps,
                                EntityState state, double speed, Runnable onBlocked) {
        this.pool.loadEntity(sprite);
        Map<Direction, SpriteAnimation> idle = new EnumMap<>(Direction.class);
        Map<Direction, SpriteAnimation> walk = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            String dir = direction.name().toLowerCase();
            idle.put(direction, new SpriteAnimation(this.pool.get(sprite + "/idle-" + dir), 0));
            walk.put(direction, new SpriteAnimation(this.pool.get(sprite + "/walk-" + dir), fps));
        }
        return new Entity(name, idle, walk, x, y, Direction.DOWN, state,
                this::isFree, speed, onBlocked);
    }

    private Entity requirePlayer() {
        if (this.player == null) {
            throw new IllegalStateException("No player: call addPlayer() in main");
        }
        return this.player;
    }

    // --- entity threads call this; reads the map at call time
    private boolean isFree(double x, double y, int width, int height) {
        return this.map == null || this.map.isFree(x, y, width, height);
    }

    // --- player thread calls this, once per bump
    private void playerBlocked() {
        IModelListener current = this.listener;
        if (current != null) {
            current.onPlayerBlocked();
        }
    }

    private static BufferedImage copyOf(BufferedImage source) {
        BufferedImage copy = new BufferedImage(
                source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = copy.createGraphics();
        try {
            g.drawImage(source, 0, 0, null);
        } finally {
            g.dispose();
        }
        return copy;
    }
}
