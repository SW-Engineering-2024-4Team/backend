package models;

public class FieldTile implements Tile {
    private int crops;
    private final int x;
    private final int y;

    public FieldTile(int x, int y, int initialCrops) {
        this.crops = initialCrops;
        this.x = x;
        this.y = y;
    }

    public int getCrops() {
        return crops;
    }

    public void removeCrop(int amount) {
        this.crops = Math.max(0, this.crops - amount);
    }

    public void addCrops(int amount) {
        this.crops += amount;
    }

    // getter와 setter 메서드 추가
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCrops(int crops) {
        this.crops = crops;
    }
}
