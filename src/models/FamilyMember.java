package models;

public class FamilyMember {
    private int x;
    private int y;
    private boolean isAdult;

    public FamilyMember(int x, int y, boolean isAdult) {
        this.x = x;
        this.y = y;
        this.isAdult = isAdult;
    }

    // getter와 setter 메서드
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean isAdult) {
        this.isAdult = isAdult;
    }
}
