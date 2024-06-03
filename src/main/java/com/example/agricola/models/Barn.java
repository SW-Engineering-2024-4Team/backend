package com.example.agricola.models;

public class Barn implements Tile {
    private final int x;
    private final int y;
    private Animal animal;

    public Barn(int x, int y) {
        this.x = x;
        this.y = y;
        this.animal = null;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
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
