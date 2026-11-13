package model;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import contract.Direction;

final class Entity implements Runnable{
    private static final long TICK_MS = 5;

    private final String name;

    private final Map<Direction, SpriteAnimation> idle;
    private final Map<Direction, SpriteAnimation> walk;

    private volatile Direction facing;
    private volatile EntityState state;
    private final Walkable terrain;

    private volatile double px;
    private volatile double py;
    private volatile boolean moving;
    private volatile double speed;

    private final Runnable onBlocked;
    private boolean wasBlocked;
    private static final int FEET_INSET_X = 3;
    private static final int FEET_TOP = 8;

    Entity(String name,
        Map<Direction, SpriteAnimation> idle,
        Map<Direction, SpriteAnimation> walk,
        int x, int y, Direction facing, EntityState state,
        Walkable terrain, double speed, Runnable onBlocked ) {
        requireAllDirections(name, "idle", idle);
        requireAllDirections(name, "walk", walk);
        this.name = name;
        this.idle = new EnumMap<>(idle);
        this.walk = new EnumMap<>(walk);
        this.px = x;
        this.py = y;
        this.facing = facing;
        this.state = state;
        this.terrain = terrain;
        this.speed = speed;

        this.onBlocked = onBlocked;
    }

    String name() { return this.name; }
    int x() { return (int) this.px; }
    int y() { return (int) this.py; }
    double speed() { return this.speed; }
    void setSpeed(double speed) { this.speed = speed; }

    BufferedImage currentFrame(){
        return current().currentFrame();
    }


    int fps(){
        return this.walk.get(Direction.DOWN).fps();
    }

    void setFps(int fps){
        for (SpriteAnimation animation : this.walk.values()){
            animation.setFps(fps);
        }
    }

    void setFacing(Direction facing) { this.facing = facing; }
    void setState(EntityState state) { this.state = state; }

    void walk(Direction direction){
        this.facing = direction;
        this.state = EntityState.WALK;
        this.moving = true;
    }

    void stop() {
        this.moving = false;
        this.state = EntityState.IDLE;
    }


    void moveTo(int x, int y) {
        this.px = x;
        this.py = y;
    }

    void start(){
        Thread thread = new Thread(this, "Entity " + this.name);
        thread.setDaemon(true);
        thread.start();
    }

    @Override 
    public void run(){
        SpriteAnimation playing = current();
        long previous = System.nanoTime();

        while (!Thread.currentThread().isInterrupted()) {
            long now = System.nanoTime();
            double elapsedSecond = (now - previous) / 1_000_000_000.0;
            previous = now;

            SpriteAnimation next = current();
            if (next != playing) {
                next.reset();
                playing = next;
            }
            playing.advance(elapsedSecond);

            if (this.moving) {
                step(elapsedSecond);
            } else {
                this.wasBlocked = false;
            }

            try{
                Thread.sleep(TICK_MS);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private SpriteAnimation current(){
        Map<Direction, SpriteAnimation> set = (this.state == EntityState.WALK) ? this.walk : this.idle;
        return set.get(this.facing);
    }

    private static void requireAllDirections(String name, String kind, Map<Direction, SpriteAnimation> map) {
        for (Direction direction : Direction.values()){
            if (!map.containsKey(direction)){
                throw new IllegalArgumentException(name + " has no " + kind + " animation for " + direction);            }
        }
    }

    private void step(double elapsedSeconds) {
        double distance = this.speed * elapsedSeconds;
        double dx = 0;
        double dy = 0;
        switch (this.facing) {
            case UP -> dy = -distance;
            case DOWN -> dy = distance;
            case LEFT -> dx = -distance;
            case RIGHT -> dx = distance;
        }

        BufferedImage frame = currentFrame();
        int feetWidth = frame.getWidth() - 2 * FEET_INSET_X;
        int feetHeight = frame.getHeight() - FEET_TOP;

        boolean blocked = false;
        if (dx != 0) {
            if (this.terrain.isFree(this.px + dx + FEET_INSET_X, this.py + FEET_TOP, feetWidth, feetHeight)) {
                this.px += dx;
            } else {
                blocked = true;
            }
        }
        if (dy != 0) {
            if (this.terrain.isFree(this.px + FEET_INSET_X, this.py + dy + FEET_TOP, feetWidth, feetHeight)) {
                this.py += dy;
            } else {
                blocked = true;
            }
        }

        if (blocked && !this.wasBlocked) {
            this.onBlocked.run();
        }
        this.wasBlocked = blocked;
    }
}
