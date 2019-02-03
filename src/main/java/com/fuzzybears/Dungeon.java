package com.fuzzybears;


public class Dungeon implements Comparable<Dungeon> {
    private final Block[][] area;

    private final int index;
    public final static int HEIGHT = 4;
    public final static int WIDTH = 7;

    public Dungeon(Block[][] area) {
        int index = 0;
        for (int column = 0; column < WIDTH; column++) {
            for (int row = 0; row < HEIGHT; row++) {
                index = (index << 1) + (area[column][row] == Block.GROUND ? 0 : 1);
            }
        }
        this.index = index;
        this.area = area;
    }

    public boolean match(Dungeon nextRoom) {
        if (nextRoom == null) return false;
        for (int i = 0; i < this.getExit().length; i++) {
            if (this.getExit()[i] == Dungeon.Block.AIR && this.getExit()[i] == nextRoom.getEntrance()[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean hasExit() {
        for (Block row : getExit()) {
            if (row == Block.AIR) return true;
        }
        return false;
    }

    public boolean hasEntrance() {
        for (Block row : getEntrance()) {
            if (row == Block.AIR) return true;
        }
        return false;
    }

    public Block[][] getArea() {
        return this.area;
    }

    public Block[] getEntrance() {
        return this.area[0];
    }

    public Block[] getExit() {
        return this.area[WIDTH - 1];
    }

    @Override
    public int compareTo(Dungeon o) {
        if (o == null) throw new NullPointerException("Specified object must be not null.");
        return Integer.compare(this.index, o.index);
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj != null & obj instanceof Dungeon)) return false;
        return this.index == ((Dungeon) obj).index;
    }

    public enum Block {
        GROUND, AIR
    }
}
