package com.example.agricola.models;


import com.example.agricola.enums.RoomType;

public class Room implements com.example.agricola.models.Tile {
    private RoomType type;
    private final int x;
    private final int y;
    private com.example.agricola.models.FamilyMember familyMember;
    private com.example.agricola.models.Animal animal;

    public Room(RoomType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public com.example.agricola.models.FamilyMember getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(com.example.agricola.models.FamilyMember familyMember) {
        this.familyMember = familyMember;
    }

    public com.example.agricola.models.Animal getAnimal() {
        return animal;
    }

    public void setAnimal(com.example.agricola.models.Animal animal) {
        this.animal = animal;
    }

    public boolean hasFamilyMember() {
        return familyMember != null;
    }

    public boolean hasAnimal() {
        return animal != null;
    }
}
