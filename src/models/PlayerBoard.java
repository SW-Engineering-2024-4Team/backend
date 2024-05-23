package models;

import enums.RoomType;

import java.util.*;

public class PlayerBoard {
    private Tile[][] tiles;
    private boolean[][] fences;
    private FamilyMember[][] familyMembers;
    private Animal[][] animals;
    private List<FenceArea> fenceAreas;

    public PlayerBoard() {
        this.tiles = new Tile[3][5];
        this.fences = new boolean[4][6];
        this.familyMembers = new FamilyMember[3][5];
        this.animals = new Animal[3][5];
        this.fenceAreas = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        tiles[0][0] = new Room(RoomType.WOOD, 0, 0);
        tiles[0][1] = new Room(RoomType.WOOD, 0, 1);
        tiles[0][2] = new Room(RoomType.WOOD, 0, 2);
        familyMembers[0][1] = new FamilyMember(0, 1, true);
        familyMembers[0][2] = new FamilyMember(0, 2, true);
    }

    public boolean canBuildHouse(int x, int y, RoomType type, Map<String, Integer> resources) {
        // 1. 타일이 비어 있는지 확인
        if (tiles[x][y] != null) {
            return false;
        }

        // 2. 위치가 기존의 집과 이웃해 있는지 확인
        boolean adjacentToExistingHouse = false;
        if (x > 0 && tiles[x - 1][y] instanceof Room) adjacentToExistingHouse = true;
        if (x < tiles.length - 1 && tiles[x + 1][y] instanceof Room) adjacentToExistingHouse = true;
        if (y > 0 && tiles[x][y - 1] instanceof Room) adjacentToExistingHouse = true;
        if (y < tiles[0].length - 1 && tiles[x][y + 1] instanceof Room) adjacentToExistingHouse = true;
        if (!adjacentToExistingHouse) {
            return false;
        }

        // 3. 같은 타입의 집을 지으려는지 확인
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof Room && ((Room) tile).getType() != type) {
                    return false;
                }
            }
        }

        // 4. 자원이 충분한지 확인
        return true;
    }

    public void buildHouse(int x, int y, RoomType type) {
        if (tiles[x][y] == null) {
            tiles[x][y] = new Room(type, x, y);
            updateFenceAreas();
        }
    }

    public Set<int[]> getValidHousePositions() {
        Set<int[]> validPositions = new HashSet<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == null && isAdjacentToExistingHouse(i, j)) {
                    validPositions.add(new int[]{i, j});
                }
            }
        }
        return validPositions;
    }

    private boolean isAdjacentToExistingHouse(int x, int y) {
        if (x > 0 && tiles[x - 1][y] instanceof Room) return true;
        if (x < tiles.length - 1 && tiles[x + 1][y] instanceof Room) return true;
        if (y > 0 && tiles[x][y - 1] instanceof Room) return true;
        if (y < tiles[0].length - 1 && tiles[x][y + 1] instanceof Room) return true;
        return false;
    }

    // 기존에 지어진 집의 타입을 반환하는 메서드
    public RoomType getExistingRoomType() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof Room) {
                    return ((Room) tile).getType();
                }
            }
        }
        return null; // 집이 없으면 null 반환
    }

    public boolean canBuildBarn(int x, int y) {
        if (tiles[x][y] != null) {
            return false; // 타일이 비어있지 않으면 외양간을 지을 수 없음
        }
        return true;
    }

    public void buildBarn(int x, int y) {
        if (tiles[x][y] == null) {
            tiles[x][y] = new Barn(x, y);
        }
    }

    // 유효한 밭 일구기 위치를 반환하는 메서드
    public Set<int[]> getValidPlowPositions() {
        Set<int[]> validPositions = new HashSet<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == null) {
                    validPositions.add(new int[]{i, j});
                }
            }
        }
        return validPositions;
    }

    // 유효한 외양간 짓기 위치를 반환하는 메서드
    public Set<int[]> getValidBarnPositions() {
        Set<int[]> validPositions = new HashSet<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (canBuildBarn(i, j)) {
                    validPositions.add(new int[]{i, j});
                }
            }
        }
        return validPositions;
    }

    // 울타리 짓기 메서드
    public void buildFences(List<int[]> coordinates) {
        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];
            if (!hasTopFence(x, y)) fences[x][y - 1] = true;
            if (!hasBottomFence(x, y)) fences[x + 1][y] = true;
            if (!hasLeftFence(x, y)) fences[x - 1][y] = true;
            if (!hasRightFence(x, y)) fences[x][y + 1] = true;
        }
        updateFenceAreas();
    }

    public boolean isValidFencePosition(int x, int y, List<int[]> existingFences) {
        for (int[] coord : existingFences) {
            if (Math.abs(coord[0] - x) + Math.abs(coord[1] - y) == 1) {
                return true;
            }
        }
        return false;
    }

    public int calculateRequiredWoodForFences(List<int[]> coordinates) {
        Set<String> uniqueEdges = new HashSet<>();

        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];

            uniqueEdges.add(x + "," + (y - 1)); // 상단 울타리
            uniqueEdges.add((x + 1) + "," + y); // 하단 울타리
            uniqueEdges.add(x + "," + (y + 1)); // 좌측 울타리
            uniqueEdges.add((x - 1) + "," + y); // 우측 울타리
        }

        // 겹치는 울타리 제거
        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];

            uniqueEdges.remove(x + "," + (y - 1)); // 이미 추가된 상단 울타리
            uniqueEdges.remove((x + 1) + "," + y); // 이미 추가된 하단 울타리
            uniqueEdges.remove(x + "," + (y + 1)); // 이미 추가된 좌측 울타리
            uniqueEdges.remove((x - 1) + "," + y); // 이미 추가된 우측 울타리
        }

        return uniqueEdges.size();
    }

    private boolean hasTopFence(int x, int y) {
        return x > 0 && fences[x - 1][y];
    }

    private boolean hasBottomFence(int x, int y) {
        return x < fences.length - 1 && fences[x + 1][y];
    }

    private boolean hasLeftFence(int x, int y) {
        return y > 0 && fences[x][y - 1];
    }

    private boolean hasRightFence(int x, int y) {
        return y < fences[0].length - 1 && fences[x][y + 1];
    }

    private void updateFenceAreas() {
        fenceAreas.clear();
        boolean[][] visited = new boolean[fences.length][fences[0].length];
        for (int i = 0; i < fences.length; i++) {
            for (int j = 0; j < fences[i].length; j++) {
                if (fences[i][j] && !visited[i][j]) {
                    FenceArea area = new FenceArea();
                    exploreFenceArea(i, j, visited, area);
                    fenceAreas.add(area);
                }
            }
        }
    }

    private void exploreFenceArea(int x, int y, boolean[][] visited, FenceArea area) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x, y});
        visited[x][y] = true;
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int px = pos[0];
            int py = pos[1];
            area.addTile(px, py, tiles[px][py]);
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int nx = px + dir[0];
                int ny = py + dir[1];
                if (nx >= 0 && ny >= 0 && nx < fences.length && ny < fences[0].length && fences[nx][ny] && !visited[nx][ny]) {
                    queue.add(new int[]{nx, ny});
                    visited[nx][ny] = true;
                }
            }
        }
    }

    public Set<int[]> getValidFencePositions() {
        Set<int[]> validPositions = new HashSet<>();
        if (noFencesBuilt()) {
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    if (tiles[i][j] == null) validPositions.add(new int[]{i, j});
                }
            }
        } else {
            for (boolean[] row : fences) {
                for (int i = 0; i < row.length; i++) {
                    if (row[i]) addAdjacentPositions(validPositions, i, i);
                }
            }
        }
        return validPositions;
    }

    public Set<int[]> getInitialFencePositions() {
        Set<int[]> validPositions = new HashSet<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == null || tiles[i][j] instanceof Barn) validPositions.add(new int[]{i, j});
            }
        }
        return validPositions;
    }

    private void addAdjacentPositions(Set<int[]> positions, int x, int y) {
        if (x > 0 && !hasFence(x - 1, y) && tiles[x - 1][y] == null) positions.add(new int[]{x - 1, y});
        if (x < tiles.length - 1 && !hasFence(x + 1, y) && tiles[x + 1][y] == null) positions.add(new int[]{x + 1, y});
        if (y > 0 && !hasFence(x, y - 1) && tiles[x][y - 1] == null) positions.add(new int[]{x, y - 1});
        if (y < tiles[0].length - 1 && !hasFence(x, y + 1) && tiles[x][y + 1] == null) positions.add(new int[]{x, y + 1});
    }

    private boolean noFencesBuilt() {
        for (boolean[] row : fences) {
            for (boolean hasFence : row) {
                if (hasFence) return false;
            }
        }
        return true;
    }

    public int getAnimalCapacity() {
        int totalCapacity = 0;
        for (FenceArea area : fenceAreas) {
            totalCapacity += area.calculateCapacity();
        }
        return totalCapacity;
    }

    // 밭 위치 확인 메서드
    public Set<int[]> getValidFieldPositions() {
        Set<int[]> validPositions = new HashSet<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof FieldTile) {
                    validPositions.add(new int[]{i, j});
                }
            }
        }
        return validPositions;
    }

    // 곡식 또는 야채 심기 메서드
    public void plantField(int x, int y, int initialCrops, String cropType) {
        if (tiles[x][y] instanceof FieldTile) {
            ((FieldTile) tiles[x][y]).setCrops(initialCrops, cropType);
        }
    }

    public void plowField(int x, int y) {
        if (tiles[x][y] == null) {
            tiles[x][y] = new FieldTile(x, y, 0, null);
        }
    }

    public void resetFamilyMembers() {
        // 가족 구성원을 초기 위치로 리셋하는 로직
        initializeBoard();
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public boolean[][] getFences() {
        return fences;
    }

    public FamilyMember[][] getFamilyMembers() {
        return familyMembers;
    }

    public Animal[][] getAnimals() {
        return animals;
    }

    public void removeFamilyMember(int x, int y) {
        familyMembers[x][y] = null;
    }

    public void changeHouse() {
    }

    // 빈 방이 있는지 확인하는 메서드
    public boolean hasEmptyRoom() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof Room && !((Room) tile).hasFamilyMember()) {
                    return true;
                }
            }
        }
        return false;
    }

    // 방이 비어 있는지 확인하는 메서드
    public boolean isEmptyRoom(int x, int y) {
        return tiles[x][y] instanceof Room && !((Room) tiles[x][y]).hasFamilyMember();
    }

    // 빈 방의 위치를 반환하는 메서드
    public List<int[]> getEmptyRoomPositions() {
        List<int[]> emptyRooms = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (isEmptyRoom(i, j)) {
                    emptyRooms.add(new int[]{i, j});
                }
            }
        }
        return emptyRooms;
    }

    // 보드에 가족 구성원을 추가하는 메서드
    public void addFamilyMemberToBoard(FamilyMember familyMember, int x, int y) {
        if (isEmptyRoom(x, y)) {
            ((Room) tiles[x][y]).setFamilyMember(familyMember);
            familyMember.setX(x);
            familyMember.setY(y);
        }
    }

    // 특정 위치에 동물을 배치할 수 있는지 확인하는 메서드
    public boolean canPlaceAnimal(int x, int y, String animalType) {
        if (tiles[x][y] instanceof Room) {
            Room room = (Room) tiles[x][y];
            return !room.hasAnimal() || room.getAnimal().getType().equals(animalType);
        } else if (tiles[x][y] instanceof Barn) {
            Barn barn = (Barn) tiles[x][y];
            if (isFenceArea(x, y)) {
                int capacity = calculateFenceCapacity();
                int animalCount = countAnimalsInsideFences(animalType);
                return animalCount < capacity;
            } else {
                return !barn.hasAnimal() || barn.getAnimal().getType().equals(animalType);
            }
        } else if (isFenceArea(x, y)) {
            int capacity = calculateFenceCapacity();
            int animalCount = countAnimalsInsideFences(animalType);
            return animalCount < capacity;
        } else {
            return false;
        }
    }

    // 보드에 동물을 추가하는 메서드
    public void addAnimalToBoard(Animal animal, int x, int y) {
        if (canPlaceAnimal(x, y, animal.getType())) {
            if (tiles[x][y] instanceof Room) {
                ((Room) tiles[x][y]).setAnimal(animal);
            } else if (tiles[x][y] instanceof Barn) {
                if (isFenceArea(x, y)) {
                    addAnimalToFenceArea(animal, x, y);
                } else {
                    ((Barn) tiles[x][y]).setAnimal(animal);
                }
            } else if (isFenceArea(x, y)) {
                addAnimalToFenceArea(animal, x, y);
            } else {
                animals[x][y] = animal;
            }
            animal.setX(x);
            animal.setY(y);
        } else {
            System.out.println("해당 위치에는 동물을 배치할 수 없습니다.");
        }
    }

    // 유효한 동물 배치 위치를 반환하는 메서드
    public Set<int[]> getValidAnimalPositions(String animalType) {
        Set<int[]> validPositions = new HashSet<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (canPlaceAnimal(i, j, animalType)) {
                    validPositions.add(new int[]{i, j});
                }
            }
        }
        return validPositions;
    }

    // 해당 위치가 울타리 영역인지 확인하는 메서드
    public boolean isFenceArea(int x, int y) {
        for (FenceArea area : fenceAreas) {
            if (area.containsTile(x, y)) {
                return true;
            }
        }
        return false;
    }

    private int calculateFenceCapacity() {
        int capacity = 0;
        for (FenceArea area : fenceAreas) {
            int fenceAreaCapacity = 1;
            for (int[] tile : area.getTiles()) {
                if (tiles[tile[0]][tile[1]] instanceof Barn) {
                    fenceAreaCapacity *= 4;
                } else {
                    fenceAreaCapacity *= 2;
                }
            }
            capacity += fenceAreaCapacity;
        }
        return capacity;
    }

    private int countAnimalsInsideFences(String animalType) {
        int count = 0;
        for (Animal[] row : animals) {
            for (Animal animal : row) {
                if (animal != null && animal.getType().equals(animalType)) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getAnimalCount(String animalType, FenceArea area) {
        int count = 0;
        for (Animal animal : area.getAnimals()) {
            if (animal.getType().equals(animalType)) {
                count++;
            }
        }
        return count;
    }

    public List<FenceArea> getFenceAreas() {
        return fenceAreas;
    }

    // 동물 번식 단계 구현
    public List<Animal> breedAnimals() {
        List<Animal> newAnimals = new ArrayList<>();
        for (FenceArea area : fenceAreas) {
            int animalCount = getAnimalCount("sheep", area);
            if (animalCount >= 2) {
                newAnimals.add(new Animal(-1, -1, "sheep")); // 새끼 동물 추가
            }
        }
        return newAnimals;
    }

    // 보드에 동물을 추가하는 메서드
    // 특정 위치에 동물을 추가하는 메서드
    public void addAnimalToFenceArea(Animal animal, int x, int y) {
        for (FenceArea area : fenceAreas) {
            if (area.containsTile(x, y)) {
                area.addAnimal(animal);
                animals[x][y] = animal;
                animal.setX(x);
                animal.setY(y);
                return;
            }
        }
        System.out.println("해당 위치에 동물을 추가할 수 없습니다.");
    }

    public void addAnimal(Animal animal) {
        // 초기에는 보드 밖에 배치
        // 실제 게임에서는 플레이어가 특정 위치에 동물을 배치하게 될 것
        animal.setX(-1);
        animal.setY(-1);
        // 동물을 리스트나 맵에 추가하는 로직 필요
    }

    public void renovateRooms(RoomType newType, Player player) {
        int roomCount = 0;
        RoomType currentType;

        // 1. 현재 방의 타입을 확인하고 업그레이드할 수 있는 방의 수를 계산
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof Room) {
                    Room room = (Room) tile;
                    currentType = room.getType();
                    if (currentType == RoomType.WOOD && newType == RoomType.CLAY) {
                        roomCount++;
                    } else if (currentType == RoomType.CLAY && newType == RoomType.STONE) {
                        roomCount++;
                    }
                }
            }
        }

        if (roomCount == 0) {
            System.out.println("업그레이드할 수 있는 방이 없습니다.");
            return;
        }

        // 2. 자원이 충분한지 확인
        Map<String, Integer> cost = new HashMap<>();
        cost.put(newType.name().toLowerCase(), roomCount); // 방 수만큼 자원 필요
        if (player.checkResources(cost)) {
            player.payResources(cost);

            // 3. 방 타입 업그레이드
            for (Tile[] row : tiles) {
                for (Tile tile : row) {
                    if (tile instanceof Room) {
                        Room room = (Room) tile;
                        currentType = room.getType();
                        if (currentType == RoomType.WOOD && newType == RoomType.CLAY) {
                            room.setType(newType);
                        } else if (currentType == RoomType.CLAY && newType == RoomType.STONE) {
                            room.setType(newType);
                        }
                    }
                }
            }
        } else {
            System.out.println("자원이 부족하여 집을 고칠 수 없습니다.");
        }
    }

    public boolean hasFence(int x, int y) {
        return fences[x][y];
    }

    // getter and setter methods
}
