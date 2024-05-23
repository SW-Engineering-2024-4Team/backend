package models;

import java.util.*;

public class FenceArea {
    private Set<int[]> tiles;
    private Set<Barn> barns;
    private List<Animal> animals;

    public FenceArea() {
        this.tiles = new HashSet<>();
        this.barns = new HashSet<>();
        this.animals = new ArrayList<>();
    }

    public void addTile(int x, int y, Tile tile) {
        tiles.add(new int[]{x, y});
        if (tile instanceof Barn) {
            barns.add((Barn) tile);
        }
    }

    public int calculateCapacity() {
        int capacity = (int) Math.pow(2, tiles.size()); // 기본 수용량: 타일 한 칸당 2배
        for (Barn barn : barns) {
            capacity *= 2; // 외양간이 있으면 타일당 수용량 2배
        }
        return capacity;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public Map<String, Integer> countAnimals() {
        Map<String, Integer> animalCounts = new HashMap<>();
        for (Animal animal : animals) {
            animalCounts.put(animal.getType(), animalCounts.getOrDefault(animal.getType(), 0) + 1);
        }
        return animalCounts;
    }

    public List<Animal> breedAnimals() {
        List<Animal> newAnimals = new ArrayList<>();
        Map<String, Integer> animalCounts = countAnimals();
        for (Map.Entry<String, Integer> entry : animalCounts.entrySet()) {
            if (entry.getValue() >= 2) {
                newAnimals.add(new Animal(-1, -1, entry.getKey())); // 새끼 동물 추가
            }
        }
        return newAnimals;
    }

    public List<Animal> getAnimals() {
        return animals;
    }


    // 특정 위치가 이 울타리 영역에 포함되는지 확인하는 메서드
    public boolean containsTile(int x, int y) {
        for (int[] pos : tiles) {
            if (pos[0] == x && pos[1] == y) {
                return true;
            }
        }
        return false;
    }

    public Set<int[]> getTiles() {
        return tiles;
    }
}
