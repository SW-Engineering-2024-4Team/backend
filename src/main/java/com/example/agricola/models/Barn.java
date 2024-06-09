package com.example.agricola.models;

public class Barn implements com.example.agricola.models.Tile {
    private final int x;
    private final int y;
    private com.example.agricola.models.Animal animal;

    public Barn(int x, int y) {
        this.x = x;
        this.y = y;
        this.animal = null;
    }

    public com.example.agricola.models.Animal getAnimal() {
        return animal;
    }

    public void setAnimal(com.example.agricola.models.Animal animal) {
        this.animal = animal;
    }

    public boolean hasAnimal() {
        return animal != null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
