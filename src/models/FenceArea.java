package models;

import java.util.*;

public class FenceArea {
    private Set<int[]> tiles;
    private Set<Barn> barns;
    private List<Animal> animals;
    private int remainingCapacity;
    private int initalCapacity;
    private String animalType; // 울타리 영역 내 동물 종류를 관리하는 필드
    private boolean hasWaterTrough; // 물통이 있는지 여부

    public int getInitalCapacity() {
        return initalCapacity;
    }

    public FenceArea() {
        this.tiles = new LinkedHashSet<>();
        this.barns = new LinkedHashSet<>();
        this.animals = new ArrayList<>();
        this.remainingCapacity = calculateInitialCapacity();
        this.initalCapacity = calculateInitialCapacity();
        this.animalType = null;
        this.hasWaterTrough = false;
    }

//    public void addTile(int x, int y, Tile tile) {
//        tiles.add(new int[]{x, y});
//        if (tile instanceof Barn) {
//            barns.add((Barn) tile);
//        }
//    }

//    public void addTile(int x, int y, Tile tile) {
//        tiles.add(new int[]{x, y});
//        if (tile instanceof Barn) {
//            barns.add((Barn) tile);
//            // 외양간에 동물이 있는 경우, 울타리 영역에 동물을 추가
//            Barn barn = (Barn) tile;
//            if (barn.hasAnimal()) {
//                addAnimal(barn.getAnimal());
//                barn.setAnimal(null); // 외양간에서 동물 제거
//            }
//        }
//    }

    public void addWaterTrough(Player player) {
        if (!hasWaterTrough) {
            hasWaterTrough = true;
            updateRemainingCapacity();
        }
    }

    public void addTile(int x, int y, Tile tile) {
        // 타일이 이미 존재하는지 확인
        boolean tileExists = false;
        for (int[] pos : tiles) {
            if (pos[0] == x && pos[1] == y) {
                tileExists = true;
                break;
            }
        }

        // 타일이 존재하지 않으면 추가
        if (!tileExists) {
            tiles.add(new int[]{x, y});
        }

        // 타일이 외양간이면 외양간 목록에 추가
        if (tile instanceof Barn) {
            barns.add((Barn) tile);
            // 외양간에 동물이 있는 경우, 울타리 영역에 동물을 추가
            Barn barn = (Barn) tile;
            if (barn.hasAnimal()) {
                addAnimal(barn.getAnimal());
                barn.setAnimal(null); // 외양간에서 동물 제거
            }
        }
        updateRemainingCapacity();

    }

    void updateRemainingCapacity() {
        this.remainingCapacity = calculateInitialCapacity() - this.animals.size();
    }

    public int getRemainingCapacity() {
        return remainingCapacity;
    }



    public boolean containsBarn(int x, int y) {
        for (Barn barn : barns) {
            if (barn.getX() == x && barn.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public int calculateInitialCapacity() {
        int capacity = (int) Math.pow(2, tiles.size()); // 기본 수용량: 타일 한 칸당 2배
        int numBarns = barns.size();
        for (Barn barn : barns) {
            capacity *= 2; // 외양간이 있으면 타일당 수용량 2배
        }
        if (hasWaterTrough) {
            capacity += numBarns * 2; // 물통이 있으면 외양간 타일당 추가 수용량 2
        }
        return capacity;
    }

    public int countAnimalsByType(String animalType) {
        int count = 0;
        for (Animal animal : animals) {
            if (animal.getType().equals(animalType)) {
                count++;
            }
        }
        return count;
    }

    public void addAnimal(Animal animal) {
        if (animalType == null) {
            animalType = animal.getType(); // 처음 추가된 동물의 종류를 설정
        }
        animals.add(animal);
//        updateRemainingCapacity();
        remainingCapacity--;
    }

    public boolean isSingleAnimalType(String type) {
        return animalType == null || animalType.equals(type);
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
    public void addBarn(Barn barn) {
        barns.add(barn);
        updateRemainingCapacity();
    }



    public Set<int[]> getTiles() {
        return tiles;
    }

    // 펜스 안 정보들을 보여 줌.
    public void printFenceAreaDetails() {
        System.out.println("Fence Area Details:");
        // 펜스로 둘러쌓인 타일들을 출력
        System.out.println("Tiles:");
        for (int[] tile : tiles) {
            System.out.println("Tile at (" + tile[0] + ", " + tile[1] + ")");
        }

        // 펜스 안에 존재하는 외양간을 출력
        System.out.println("Barns:");
        for (Barn barn : barns) {
            System.out.println("Barn at (" + barn.getX() + ", " + barn.getY() + ")");
        }

        // 펜스 안에 존재하는 동물을 출력
        System.out.println("Animals:");
        for (Animal animal : animals) {
            System.out.println("Animal: " + animal.getType() + " at (" + animal.getX() + ", " + animal.getY() + ")");
        }
        System.out.println("Remaining Capacity: " + getRemainingCapacity());
    }

    // 새로 추가된 메서드들
    public boolean hasBarn() {
        return !barns.isEmpty();
    }

    public int getTileCount() {
        return tiles.size();
    }

}
