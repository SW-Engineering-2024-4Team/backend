package models;

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

    // 울타리 내부에 있는지 확인하는 메서드
    public boolean isInsideFence(PlayerBoard board) {
        for (boolean[] row : board.getFences()) {
            for (boolean fence : row) {
                if (fence) {
                    return true;
                }
            }
        }
        return false;
    }
}
