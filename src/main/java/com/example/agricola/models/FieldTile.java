package com.example.agricola.models;

public class FieldTile implements Tile {
    private int crops;
    private String cropType;
    private final int x;
    private final int y;

    public FieldTile(int x, int y, int initialCrops, String cropType) {
        this.crops = initialCrops;
        this.cropType = cropType;
        this.x = x;
        this.y = y;
    }

    public int getCrops() {
        return crops;
    }

    public String getCropType() {
        return cropType;
    }

    public void removeCrop(int amount) {
        this.crops = Math.max(0, this.crops - amount);
    }

    public void addCrops(int amount) {
        this.crops += amount;
    }

    public void setCrops(int crops, String cropType) {
        this.crops = crops;
        this.cropType = cropType;
    }

    // getter와 setter 메서드 추가
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
