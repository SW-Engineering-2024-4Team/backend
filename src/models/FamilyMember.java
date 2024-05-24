package models;

public class FamilyMember {
    private int x;
    private int y;
    private boolean isAdult;
    private int originalX;  // 원래 x 위치
    private int originalY;  // 원래 y 위치
    private boolean used;

    public FamilyMember(int x, int y, boolean isAdult) {
        this.x = x;
        this.y = y;
        this.isAdult = isAdult;
        this.originalX = x;
        this.originalY = y;
        this.used = false;
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

    public void resetPosition() {
        this.x = originalX;
        this.y = originalY;
    }
    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }


    public int getOriginalY() {
        return originalY;
    }

    public int getOriginalX() {
        return originalX;
    }

}
