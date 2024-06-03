package com.example.agricola.models;

import java.util.Map;

public class Animal {
    private int x;
    private int y;
    private String type;

    public Animal(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    // 객체를 Map으로 변환하는 메서드 추가
    public Map<String, Object> toMap() {
        return Map.of(
                "x", x,
                "y", y,
                "type", type
        );
    }

    // getter와 setter 메서드
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
}
