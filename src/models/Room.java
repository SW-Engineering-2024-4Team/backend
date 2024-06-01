package models;

import enums.RoomType;

public class Room implements Tile {
    private RoomType type;
    private final int x;
    private final int y;
    private FamilyMember familyMember;
    private Animal animal;

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

    public FamilyMember getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(FamilyMember familyMember) {
        this.familyMember = familyMember;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public boolean hasFamilyMember() {
        return familyMember != null;
    }

    public boolean hasAnimal() {
        return animal != null;
    }
}
