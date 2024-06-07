package com.example.agricola.models;

import com.example.agricola.cards.common.ActionRoundCard;
import com.example.agricola.enums.RoomType;
import com.example.agricola.models.*;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerBoard {
    private Tile[][] tiles;
    private boolean[][][] fences; // 3차원 배열로 변경
    private FamilyMember[][] familyMembers;
    private Animal[][] animals;
    private List<FenceArea> fenceAreas;
    private Set<FenceArea> managedFenceAreas; // 추가된 컬렉션

    public Set<FenceArea> getManagedFenceAreas() {
        return managedFenceAreas;
    }

    public PlayerBoard() {
        this.tiles = new Tile[3][5];
        this.fences = new boolean[3][5][4];
        this.familyMembers = new FamilyMember[3][5];
        this.animals = new Animal[3][5];
        this.fenceAreas = new ArrayList<>();
        this.managedFenceAreas = new HashSet<>(); // 초기화
        initializeBoard();
    }

    private void initializeBoard() {
        tiles[1][0] = new Room(RoomType.WOOD, 1, 0);
        tiles[2][0] = new Room(RoomType.WOOD, 2, 0);
        familyMembers[1][0] = new FamilyMember(1, 0, true);
        ((Room) tiles[1][0]).setFamilyMember(familyMembers[1][0]);
        familyMembers[2][0] = new FamilyMember(2, 0, true);
        ((Room) tiles[2][0]).setFamilyMember(familyMembers[2][0]);
    }

    // 객체를 Map으로 변환하는 메서드 추가
    public Map<String, Object> toMap() {
        return Map.of(
                "tiles", tiles,
                "fences", fences,
                "familyMembers", familyMembers,
                "animals", animals,
                "fenceAreas", fenceAreas.stream().map(FenceArea::toMap).collect(Collectors.toList())
        );
    }

    public boolean canBuildHouse(int x, int y, RoomType type, Map<String, Integer> resources) {
        // 1. 타일이 비어 있는지 확인
        if (tiles[x][y] != null) {
            System.out.println("타일이 비어있지 않음.");
            return false;
        }

        // 2. 위치가 기존의 집과 이웃해 있는지 확인
        boolean adjacentToExistingHouse = false;
        if (x > 0 && tiles[x - 1][y] instanceof Room) adjacentToExistingHouse = true;
        if (x < tiles.length - 1 && tiles[x + 1][y] instanceof Room) adjacentToExistingHouse = true;
        if (y > 0 && tiles[x][y - 1] instanceof Room) adjacentToExistingHouse = true;
        if (y < tiles[0].length - 1 && tiles[x][y + 1] instanceof Room) adjacentToExistingHouse = true;
        if (!adjacentToExistingHouse) {
            System.out.println("타일이 기존 집과 이웃하지 않음");
            return false;
        }

        // 3. 같은 타입의 집을 지으려는지 확인
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof Room && ((Room) tile).getType() != type) {
                    System.out.println("다른 타입의 집을 지으려고 함");
                    return false;
                }
            }
        }

        // 4. 자원이 충분한지 확인
        Integer resourceAmount = resources.get(type.name().toLowerCase());
        System.out.println("resources = " + resourceAmount);
        if (resourceAmount == null || resourceAmount <= 0) {
            System.out.println("자원이 부족합니다.");
            return false;
        }

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

    // 방의 수를 반환하는 메서드 추가
    public int getRoomCount() {
        int roomCount = 0;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof Room) {
                    roomCount++;
                }
            }
        }
        return roomCount;
    }

    public boolean canBuildBarn(int x, int y) {
        // 타일이 비어 있거나 이미 울타리 영역 안에 있는 경우에만 외양간을 지을 수 있음
        if (tiles[x][y] == null || isPartOfFence(x, y)) {
            return true;
        }
        return false;
    }


    public void buildBarn(int x, int y) {
        if (canBuildBarn(x, y)) {
            tiles[x][y] = new Barn(x, y);
            if (isPartOfFence(x, y)) {
                addBarnToFenceArea(x, y);
            }
            updateFenceAreas(); // 울타리 영역 업데이트
        } else {
            System.out.println("Cannot build barn at the specified location.");
        }

    }


    private void addBarnToFenceArea(int x, int y) {
        for (FenceArea area : managedFenceAreas) {
            if (area.containsTile(x, y)) {
                area.addBarn((Barn) tiles[x][y]);
//                return;
            }
        }
        // 해당 타일이 포함된 울타리 영역이 없으면 새로 생성
        FenceArea newArea = new FenceArea();
        newArea.addTile(x, y, tiles[x][y]);
        fenceAreas.add(newArea);
    }


    // 유효한 밭 위치를 반환하는 메서드
    public Set<int[]> getValidPlowPositions() {
        Set<int[]> validPositions = new HashSet<>();
        boolean hasExistingField = hasExistingField();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] == null && (!hasExistingField || isAdjacentToSameTypeTile(i, j, FieldTile.class))) {
                    validPositions.add(new int[]{i, j});
                }
            }
        }
        return validPositions;
    }

            // 유효한 외양간 위치를 반환하는 메서드
            public Set<int[]> getValidBarnPositions() {
                Set<int[]> validPositions = new LinkedHashSet<>();
                boolean hasExistingBarn = hasExistingBarn();
                for (int i = 0; i < tiles.length; i++) {
                    for (int j = 0; j < tiles[0].length; j++) {
                        if (tiles[i][j] == null && (!hasExistingBarn || isAdjacentToSameTypeTile(i, j, Barn.class))) {
                            validPositions.add(new int[]{i, j});
                        }
                    }
                }
                return validPositions;
            }

    // 기물이 인접한지 확인하는 메서드
    private boolean isAdjacentToSameTypeTile(int x, int y, Class<?> type) {
        if (x > 0 && type.isInstance(tiles[x - 1][y])) return true;
        if (x < tiles.length - 1 && type.isInstance(tiles[x + 1][y])) return true;
        if (y > 0 && type.isInstance(tiles[x][y - 1])) return true;
        if (y < tiles[0].length - 1 && type.isInstance(tiles[x][y + 1])) return true;
        return false;
    }

    // 기존 기물이 있는지 확인하는 메서드들
    private boolean hasExistingField() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof FieldTile) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasExistingBarn() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof Barn) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasExistingHouse() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof Room) {
                    return true;
                }
            }
        }
        return false;
    }

    public void buildFences(List<int[]> coordinates, Player player) {
        // 유효 좌표가 없는 경우
        if (coordinates == null || coordinates.isEmpty()) {
            System.out.println("No coordinates provided.");
            return;
        }

        // 자원이 부족한 경우
        int woodRequired = calculateRequiredWoodForFences(coordinates);
        if (player.getResource("wood") < woodRequired) {
            System.out.println("Not enough wood to build fences.");
            return;
        }

        // 유효한 좌표가 아닌 경우
        if (!validateCoordinates(coordinates)) {
            System.out.println("Invalid coordinates provided.");
            return;
        }

        // 이미 울타리가 있는지 확인
        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];
            if (isFenceArea(x, y)) {
                System.out.println("Fence already exists at (" + x + ", " + y + ").");
                return;
            }
        }

        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];

            // 울타리가 외양간이 있는 타일에 지어질 경우
            if (tiles[x][y] instanceof Barn) {
                Barn barn = (Barn) tiles[x][y];

                if (barn.hasAnimal()) {
                    // 외양간에 동물이 있는 경우, 울타리 영역에 동물을 추가
                    Animal animal = barn.getAnimal();
                    addAnimalToFenceArea(animal, x, y);
                    barn.setAnimal(null); // 외양간에서 동물 제거
                }
            }

            updateFence(x, y, coordinates);
        }

        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];

            updateAdjacentFences(x, y, coordinates);
        }

//                player.addResource("wood", -woodRequired);
        mergeFenceAreas(coordinates);
    }

    // 새로운 울타리 영역을 생성하거나 기존 영역에 병합하는 메서드
    private void mergeFenceAreas(List<int[]> coordinates) {
        Set<FenceArea> affectedAreas = new HashSet<>();
        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];
            for (FenceArea area : fenceAreas) {
                if (area.containsTile(x, y)) {
                    affectedAreas.add(area);
                }
            }
        }

        FenceArea newArea = new FenceArea();
        for (int[] coord : coordinates) {
            newArea.addTile(coord[0], coord[1], tiles[coord[0]][coord[1]]);
        }

        for (FenceArea area : affectedAreas) {
            for (int[] tile : area.getTiles()) {
                newArea.addTile(tile[0], tile[1], tiles[tile[0]][tile[1]]);
            }
            fenceAreas.remove(area);
        }

        fenceAreas.add(newArea);
        managedFenceAreas.add(newArea); // 새로운 컬렉션에 추가
    }


    private boolean validateCoordinates(List<int[]> coordinates) {
        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];
            if (!isValidFencePosition(x, y)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidFencePosition(int x, int y) {
        return (tiles[x][y] == null || tiles[x][y] instanceof Barn) && (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length);
    }


    private void updateFence(int x, int y, List<int[]> coordinates) {
        boolean isLeftEdge = coordinates.stream().noneMatch(coord -> coord[0] == x && coord[1] == y - 1);
        boolean isRightEdge = coordinates.stream().noneMatch(coord -> coord[0] == x && coord[1] == y + 1);
        boolean isTopEdge = coordinates.stream().noneMatch(coord -> coord[0] == x - 1 && coord[1] == y);
        boolean isBottomEdge = coordinates.stream().noneMatch(coord -> coord[0] == x + 1 && coord[1] == y);

        // 상단 울타리
        if (!fences[x][y][0] && isTopEdge) {
            fences[x][y][0] = true;
        }
        // 하단 울타리
        if (!fences[x][y][1] && isBottomEdge) {
            fences[x][y][1] = true;
        }
        // 좌측 울타리
        if (!fences[x][y][2] && isLeftEdge) {
            fences[x][y][2] = true;
        }
        // 우측 울타리
        if (!fences[x][y][3] && isRightEdge) {
            fences[x][y][3] = true;
        }
    }

    private void updateAdjacentFences(int x, int y, List<int[]> coordinates) {
        // 상단 인접 타일의 하단 울타리
        if (x > 0 && fences[x][y][0]) {
            fences[x - 1][y][1] = true;
        }
        // 하단 인접 타일의 상단 울타리
        if (x < tiles.length - 1 && fences[x][y][1]) {
            fences[x + 1][y][0] = true;
        }
        // 좌측 인접 타일의 우측 울타리
        if (y > 0 && fences[x][y][2]) {
            fences[x][y - 1][3] = true;
        }
        // 우측 인접 타일의 좌측 울타리
        if (y < tiles[0].length - 1 && fences[x][y][3]) {
            fences[x][y + 1][2] = true;
        }
    }

    public int calculateRequiredWoodForFences(List<int[]> coordinates) {
        Set<String> uniqueFences = new HashSet<>();

        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];

            // 상단 울타리
            if (fences[x][y][0] == false && (x == 0 || coordinates.stream().noneMatch(c -> c[0] == x - 1 && c[1] == y))) {
                uniqueFences.add(x + "," + y + ",0");
            }
            // 하단 울타리
            if (fences[x][y][1] == false && (x == tiles.length - 1 || coordinates.stream().noneMatch(c -> c[0] == x + 1 && c[1] == y))) {
                uniqueFences.add(x + "," + y + ",1");
            }
            // 좌측 울타리
            if (fences[x][y][2] == false && (y == 0 || coordinates.stream().noneMatch(c -> c[0] == x && c[1] == y - 1))) {
                uniqueFences.add(x + "," + y + ",2");
            }
            // 우측 울타리
            if (fences[x][y][3] == false && (y == tiles[0].length - 1 || coordinates.stream().noneMatch(c -> c[0] == x && c[1] == y + 1))) {
                uniqueFences.add(x + "," + y + ",3");
            }
        }

        return uniqueFences.size();
    }

//    private void updateFenceAreas() {
//        fenceAreas.clear();
//        boolean[][] visited = new boolean[tiles.length][tiles[0].length];
//        for (int i = 0; i < tiles.length; i++) {
//            for (int j = 0; j < tiles[0].length; j++) {
//                if (!visited[i][j] && isFenceArea(i, j)) {
//                    FenceArea area = new FenceArea();
//                    exploreFenceArea(i, j, visited, area);
//                    fenceAreas.add(area);
//                    managedFenceAreas.add(area); // 새로운 컬렉션에 추가
//                }
//            }
//        }
//    }

    //    private void updateFenceAreas() {
//        fenceAreas.clear();
//        boolean[][] visited = new boolean[tiles.length][tiles[0].length];
//        for (int i = 0; i < tiles.length; i++) {
//            for (int j = 0; j < tiles[0].length; j++) {
//                if (!visited[i][j] && isPartOfFence(i, j)) {
//                    FenceArea area = new FenceArea();
//                    exploreFenceArea(i, j, visited, area);
//                    fenceAreas.add(area);
//                }
//            }
//        }
//    }
    private void updateFenceAreas() {
        fenceAreas.clear();
        boolean[][] visited = new boolean[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (!visited[i][j] && isPartOfFence(i, j)) {
                    FenceArea area = new FenceArea();
                    exploreFenceArea(i, j, visited, area);
                    fenceAreas.add(area);
                }
            }
        }
    }

    private boolean isPartOfFence(int x, int y) {
        for (int i = 0; i < 4; i++) {
            if (fences[x][y][i]) {
                return true;
            }
        }
        return false;
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
                if (nx >= 0 && ny >= 0 && nx < tiles.length && ny < tiles[0].length && !visited[nx][ny] && isPartOfFence(nx, ny)) {
                    queue.add(new int[]{nx, ny});
                    visited[nx][ny] = true;
                }
            }
        }
    }


    public Set<int[]> getValidFencePositions() {
        Set<int[]> validPositions = new HashSet<>();
        if (noFencesBuilt()) {
            // 처음 울타리를 지을 때: 빈 타일 또는 외양간이 있는 타일이 유효한 위치
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    if (tiles[i][j] == null || tiles[i][j] instanceof Barn) {
                        validPositions.add(new int[]{i, j});
                    }
                }
            }
        } else {
            // 이후 울타리를 지을 때: 기존 울타리와 인접한 빈 타일 또는 외양간이 있는 타일이 유효한 위치
            boolean[][] visited = new boolean[tiles.length][tiles[0].length];
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    if ((tiles[i][j] == null || tiles[i][j] instanceof Barn) && !visited[i][j]) {
                        if (isAdjacentToFence(i, j)) {
                            validPositions.add(new int[]{i, j});
                        }
                    }
                    visited[i][j] = true;
                }
            }

            // 유효한 위치에서 울타리 내부의 타일들을 제외
            Iterator<int[]> iterator = validPositions.iterator();
            while (iterator.hasNext()) {
                int[] pos = iterator.next();
                if (isInsideFenceArea(pos[0], pos[1])) {
                    iterator.remove();
                }
            }
        }
        return validPositions;
    }


    private boolean isAdjacentToFence(int x, int y) {
        if (x > 0 && fences[x - 1][y][1]) return true; // 상단 인접 타일의 하단 울타리
        if (x < tiles.length - 1 && fences[x + 1][y][0]) return true; // 하단 인접 타일의 상단 울타리
        if (y > 0 && fences[x][y - 1][3]) return true; // 좌측 인접 타일의 우측 울타리
        if (y < tiles[0].length - 1 && fences[x][y + 1][2]) return true; // 우측 인접 타일의 좌측 울타리
        if (fences[x][y][0] || fences[x][y][1] || fences[x][y][2] || fences[x][y][3]) return true; // 현재 타일의 울타리
        return false;
    }


    private boolean noFencesBuilt() {
        for (boolean[][] row : fences) {
            for (boolean[] tile : row) {
                for (boolean hasFence : tile) {
                    if (hasFence) return false;
                }
            }
        }
        return true;
    }

    private boolean isInsideFenceArea(int x, int y) {
        for (FenceArea area : managedFenceAreas) {
            for (int[] tile : area.getTiles()) {
                if (tile[0] == x && tile[1] == y) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isEnclosedByFences(int x, int y, boolean[][] visited) {
        if (visited[x][y]) {
            return true;
        }

        visited[x][y] = true;

        // 울타리 내부인지 확인
        boolean enclosed = true;

        // 상단
        if (x > 0 && !fences[x - 1][y][1]) {
            enclosed = false;
        } else if (x > 0 && !visited[x - 1][y]) {
            enclosed = enclosed && isEnclosedByFences(x - 1, y, visited);
        }

        // 하단
        if (x < tiles.length - 1 && !fences[x + 1][y][0]) {
            enclosed = false;
        } else if (x < tiles.length - 1 && !visited[x + 1][y]) {
            enclosed = enclosed && isEnclosedByFences(x + 1, y, visited);
        }

        // 좌측
        if (y > 0 && !fences[x][y - 1][3]) {
            enclosed = false;
        } else if (y > 0 && !visited[x][y - 1]) {
            enclosed = enclosed && isEnclosedByFences(x, y - 1, visited);
        }

        // 우측
        if (y < tiles[0].length - 1 && !fences[x][y + 1][2]) {
            enclosed = false;
        } else if (y < tiles[0].length - 1 && !visited[x][y + 1]) {
            enclosed = enclosed && isEnclosedByFences(x, y + 1, visited);
        }

        return enclosed;
    }


    // 단독 외양간의 수용 능력을 계산하는 메서드
    private int calculateStandaloneBarnCapacity() {
        int capacity = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof Barn && !isFenceArea(i, j)) {
                    if (!((Barn) tiles[i][j]).hasAnimal()) {
                        capacity++;
                    }
                }
            }
        }
        return capacity;
    }

    // 집의 수용 능력을 계산하는 메서드
    private int calculateHouseCapacity() {
        boolean isAnimalInHouse = false;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof Room) {
                    if (((Room) tiles[i][j]).hasAnimal()) {
                        isAnimalInHouse = true;
                    }
                }
            }
        }
        // 집은 모두 합쳐서 한 마리만 수용 가능
        // 집이 없으면 0
        if (isAnimalInHouse) {
            return 0;
        }
        return 1;
    }

    public int getAnimalCapacity() {
        int totalCapacity = 0;
        for (FenceArea area : managedFenceAreas) {
            totalCapacity += area.getRemainingCapacity();
        }
        System.out.println("totalCapacity = " + totalCapacity);
        // 단독 외양간의 수용 능력 추가
        totalCapacity += calculateStandaloneBarnCapacity();
        System.out.println("totalCapacity = " + totalCapacity);

        // 집의 수용 능력 추가
        totalCapacity += calculateHouseCapacity();
        System.out.println("totalCapacity = " + totalCapacity);

        return totalCapacity;
    }



            // 밭 위치 확인 메서드
            public Set<int[]> getValidFieldPositions() {
                Set<int[]> validPositions = new HashSet<>();
                for (int i = 0; i < tiles.length; i++) {
                    for (int j = 0; j < tiles[0].length; j++) {
                        if (tiles[i][j] instanceof FieldTile) {
                            if (((FieldTile) tiles[i][j]).getCrops() == 0) {
                                validPositions.add(new int[]{i, j});
                            }
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
        for (int i = 0; i < familyMembers.length; i++) {
            for (int j = 0; j < familyMembers[i].length; j++) {
                if (familyMembers[i][j] != null) {
                    familyMembers[i][j].resetPosition();
                    Room room = (Room) tiles[familyMembers[i][j].getX()][familyMembers[i][j].getY()];
                    room.setFamilyMember(familyMembers[i][j]);
                }
            }
        }
    }


    public Tile[][] getTiles() {
        return tiles;
    }

    public Map<String, Object> getTileInfo() {
        Map<String, Object> boardInfo = new HashMap<>();

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                Map<String, Object> tileInfo = new HashMap<>();
                Tile tile = tiles[x][y];
                FamilyMember familyMember = familyMembers[x][y];
                Animal animal = animals[x][y];

                if (tile != null) {
                    tileInfo.put("tileType", tile.getClass().getSimpleName());
                    if (tile instanceof Room) {
                        tileInfo.put("roomType", ((Room) tile).getType().name());
                    } else if (tile instanceof Barn) {
                        tileInfo.put("hasAnimal", ((Barn) tile).hasAnimal());
                    } else if (tile instanceof FieldTile) {
                        tileInfo.put("crops", ((FieldTile) tile).getCrops());
                        tileInfo.put("cropType", ((FieldTile) tile).getCropType());
                    }
                } else {
                    tileInfo.put("tileType", "Empty");
                }

                if (familyMember != null) {
                    tileInfo.put("familyMember", familyMember.isAdult() ? "Adult" : "Child");
                }

                if (animal != null) {
                    tileInfo.put("animalType", animal.getType());
                }

                tileInfo.put("fences", new boolean[]{
                        fences[x][y][0], // 상단 울타리
                        fences[x][y][1], // 하단 울타리
                        fences[x][y][2], // 좌측 울타리
                        fences[x][y][3]  // 우측 울타리
                });

                boardInfo.put("tile_" + x + "_" + y, tileInfo);
            }
        }

        return boardInfo;
    }



    public boolean[][][] getFences() {
                return fences;
            }


    public FamilyMember[][] getFamilyMembers() {
        return familyMembers;
    }

    public FamilyMember getFamilyMember(int x, int y) {
        return familyMembers[x][y];
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

    // 방이 비어 있는지 확인하는 메서드
    public boolean isEmptyRoom(int x, int y) {
        return tiles[x][y] instanceof Room && !((Room) tiles[x][y]).hasFamilyMember();
    }

    // 보드에 가족 구성원을 추가하는 메서드
    public void addFamilyMemberToBoard(FamilyMember familyMember, int x, int y) {
        if (isEmptyRoom(x, y)) {
            ((Room) tiles[x][y]).setFamilyMember(familyMember);
            familyMembers[x][y] = familyMember;
            familyMember.setX(x);
            familyMember.setY(y);
        }
    }


    // 플레이어가 개인 보드에 동물을 배치할 수 있는지 확인해주는 메서드
    public boolean canPlaceAnimal(int x, int y, String animalType) {
        if (tiles[x][y] instanceof Room) {
            return canPlaceAnimalInHouse();
        } else if (tiles[x][y] instanceof Barn) {
            Barn barn = (Barn) tiles[x][y];
            if (isFenceArea(x, y)) {
                FenceArea area = getFenceArea(x, y);
                int capacity = area.getRemainingCapacity();
                return area.isSingleAnimalType(animalType) && capacity > 0;
            } else {
                return !barn.hasAnimal();
            }
        } else if (isFenceArea(x, y)) {
            FenceArea area = getFenceArea(x, y);
            int capacity = area.getRemainingCapacity();
            return area.isSingleAnimalType(animalType) && capacity > 0;
        } else {
            return false;
        }
    }

    //동물을 놓을 수 있는 빈방정보
    private boolean canPlaceAnimalInHouse() {
        // 모든 방을 통틀어 한 마리의 동물만 보관 가능
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof Room && ((Room) tiles[i][j]).hasAnimal()) {
                    return false; // 이미 동물이 있는 경우
                }
            }
        }
        return true; // 빈 방이 있는 경우
    }

    // 보드의 모든 집에 동물이 있는지 확인하는 메서드
    private boolean isAnyHouseContainingAnimal() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j] instanceof Room) {
                    Room room = (Room) tiles[i][j];
                    if (room.hasAnimal()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 보드에 동물을 추가하는 메서드
    public void addAnimalToBoard(Animal animal, int x, int y) {
        if (canPlaceAnimal(x, y, animal.getType())) {
            if (tiles[x][y] instanceof Room) {
                if (canPlaceAnimalInHouse()) {
                    ((Room) tiles[x][y]).setAnimal(animal);
                } else {
                    System.out.println("모든 방이 동물로 이미 차 있습니다.");
                    return;
                }
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
            System.out.println("(" + x + "," + y + ") 해당 위치에는 동물을 배치할 수 없습니다.");
        }
    }

    // 보드에 울타리 영역 내에 동물을 추가하는 메서드
    public void addAnimalToFenceArea(Animal animal, int x, int y) {
        for (FenceArea area : managedFenceAreas) {
            if (area.containsTile(x, y)) {
                if (area.isSingleAnimalType(animal.getType())) {
                    area.addAnimal(animal);
                    animals[x][y] = animal;
                    animal.setX(x);
                    animal.setY(y);
                    area.updateRemainingCapacity(); // 수용 능력 업데이트
                } else {
                    System.out.println("해당 울타리 영역에는 다른 종류의 동물이 이미 있습니다.");
                }
                return;
            }
        }
        System.out.println("해당 위치에 동물을 추가할 수 없습니다.");
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
        for (FenceArea area : managedFenceAreas) {
            if (area.containsTile(x, y)) {
                return true;
            }
        }
        return false;
    }

    public int calculateInitFenceCapacity() {
        int capacity = 0;
        for (FenceArea area : managedFenceAreas) {
            capacity += area.getInitalCapacity();
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

    public void printFenceAreas() {
        System.out.println("Printing all fence areas:");
        for (FenceArea area : managedFenceAreas) {
            area.printFenceAreaDetails();
        }
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

    private FenceArea getFenceArea(int x, int y) {
        for (FenceArea area : managedFenceAreas) {
            if (area.containsTile(x, y)) {
                return area;
            }
        }
        return null;
    }

    // 동물 번식 단계 구현
    public Animal breedAnimals() {
        Animal newAnimal = null;
        for (FenceArea area : fenceAreas) {
            int animalCount = getAnimalCount("sheep", area);
            if (animalCount >= 2) {
                newAnimal = new Animal(-1, -1, "sheep"); // 새끼 동물 추가
                break;
            }
        }
        return newAnimal;
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
                cost.put(newType.name().toLowerCase(), 2 * roomCount); // 방 수만큼 자원 필요
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

    public void printPlayerBoardWithFences(String message, Set<int[]> validPositions) {
        System.out.println(message);
        Tile[][] tiles = getTiles();
        boolean[][][] fences = getFences();
        int rows = tiles.length;
        int cols = tiles[0].length;
        int cellWidth = 5; // 각 셀의 너비를 고정

        // Print the top boundary of the board
        for (int i = 0; i < rows; i++) {
            // Print top fences
            for (int j = 0; j < cols; j++) {
                System.out.print("+");
                if (fences[i][j][0]) {
                    System.out.print("---");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println("+");

            // Print left fences and tiles
            for (int j = 0; j < cols; j++) {
                if (fences[i][j][2]) {
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }

                boolean isValidPosition = false;
                for (int[] pos : validPositions) {
                    if (pos[0] == i && pos[1] == j) {
                        isValidPosition = true;
                        break;
                    }
                }

                String tileString;
                if (tiles[i][j] == null) {
                    if (isValidPosition) {
                        tileString = "*";
                    } else {
                        tileString = "";
                    }
                } else if (tiles[i][j] instanceof Room) {
                    tileString = "r";
                } else if (tiles[i][j] instanceof Barn) {
                    tileString = "b";
                } else if (tiles[i][j] instanceof FieldTile) {
                    tileString = "f";
                } else {
                    tileString = "";
                }
                System.out.print(centerString(tileString, cellWidth));
            }
            if (fences[i][cols - 1][3]) {
                System.out.print("|");
            }
            System.out.println();
        }

        // Print the bottom boundary of the board
        for (int j = 0; j < cols; j++) {
            System.out.print("+");
            if (fences[rows - 1][j][1]) {
                System.out.print("---");
            } else {
                System.out.print("   ");
            }
        }
        System.out.println("+");
    }

    private String centerString(String str, int width) {
        int paddingSize = (width - str.length()) / 2;
        String padding = " ".repeat(paddingSize);
        return padding + str + padding;
    }

    public int getAdditionalRoomsCount() {
        int initialRooms = 2; // 초기 집의 방 수
        int currentRooms = 0;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof Room) {
                    currentRooms++;
                }
            }
        }
        return Math.max(0, currentRooms - initialRooms);
    }

    public void applyWaterTroughEffect(Player player) {
        for (FenceArea area : managedFenceAreas) {
            area.addWaterTrough(player);
        }
    }

    // getter and setter methods

    public void removeFences(List<int[]> coordinates, Player player) {
        // 유효 좌표가 없는 경우
        if (coordinates == null || coordinates.isEmpty()) {
            System.out.println("No coordinates provided.");
            return;
        }

        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];

            // 상단 울타리 제거
            if (fences[x][y][0]) {
                fences[x][y][0] = false;
                if (x > 0 && fences[x - 1][y][1]) {
                    fences[x - 1][y][1] = false;
                }
            }

            // 하단 울타리 제거
            if (fences[x][y][1]) {
                fences[x][y][1] = false;
                if (x < tiles.length - 1 && fences[x + 1][y][0]) {
                    fences[x + 1][y][0] = false;
                }
            }

            // 좌측 울타리 제거
            if (fences[x][y][2]) {
                fences[x][y][2] = false;
                if (y > 0 && fences[x][y - 1][3]) {
                    fences[x][y - 1][3] = false;
                }
            }

            // 우측 울타리 제거
            if (fences[x][y][3]) {
                fences[x][y][3] = false;
                if (y < tiles[0].length - 1 && fences[x][y + 1][2]) {
                    fences[x][y + 1][2] = false;
                }
            }
        }

        // 자원 반환 (울타리 제거 시 자원의 일부를 반환)
        int woodReturned = calculateReturnedWoodForFences(coordinates);
        player.addResource("wood", woodReturned);

        // 울타리 영역 업데이트
        updateFenceAreasAfterRemoval(coordinates);
    }

    private int calculateReturnedWoodForFences(List<int[]> coordinates) {
        // 각 울타리 조각 당 반환할 자원의 양 계산 (반환 비율 조정 가능)
        return coordinates.size(); // 간단히 좌표 수만큼 반환한다고 가정
    }

    private void updateFenceAreasAfterRemoval(List<int[]> coordinates) {
        for (int[] coord : coordinates) {
            int x = coord[0];
            int y = coord[1];

            // 각 좌표에 대해 울타리 영역 갱신
            Set<FenceArea> affectedAreas = new HashSet<>();
            for (FenceArea area : fenceAreas) {
                if (area.containsTile(x, y)) {
                    affectedAreas.add(area);
                }
            }

            for (FenceArea area : affectedAreas) {
                area.removeTile(x, y, tiles[x][y]);
                if (area.isEmpty()) {
                    fenceAreas.remove(area);
                    managedFenceAreas.remove(area);
                }
            }
        }

        // 울타리 영역을 다시 계산하여 업데이트
        updateFenceAreas();
    }
    /** //TODO
     * 한번에 보낼지 나눠서 보낼지 결정
     */

    /**
     * 특정 플레이어의 개인 보드판위의 기물정보를 나타내는 함수 (울타리 제외)
     *  0:가족구성원, 1:나무집, 2:흙집, 3:돌집, 4:밭, 5:곡식, 6:외양간, 7:양, null: 비어있음
     * ex) [["1":"7"], ["3"], ["4":"5"], "null"....] (나무집안에 양, 돌집, 밭위에 곡식, 빈칸, ...)
     * @param mainBoard
     * @return
     * 미완.
     */
    public List<String> playerboardObjectList(MainBoard mainBoard) {
        List<ActionRoundCard> actionRoundCards = mainBoard.getActionCards();
        List<String> sendToFront = new ArrayList<>();
        for (ActionRoundCard card : actionRoundCards) {
            if (card.isOccupied()) {
                sendToFront.add(card.getOccupiedPlayerId());
            } else {
                sendToFront.add("null");
            }
        }
        return sendToFront;
    }

    /**
     * 울타리정보 리스트로 바꾸는 함수 (나무만 있으면 지을수 있게 울타리개수제한x 울타리짓는건 fence클래스랑 manage클래스 찾아보기.)
     * Fence 3중배열관리 [x][y][상,하,좌,우 =4]
     */
    /**
     * Line18 Set<FenceArea> getManagedFenceAreas()
     */
    /**
     * 동물 놓을수 있는 위치 반환 867Line getValidAnimalPositions, 792Line 동물을 놓을 수 있는 빈방위치 반환
     */

    /**
     * 가족구성원 정보 리스트로 바꾸는함수
     */

    /**
     * 플레이어 개인보드위 타일정보 리스트로 바꾸는 함수
     */

    /**
     * 플레이어보드위 곡식과 동물정보 리스트 바꾸는 함수
     * 곡식이나 동물정보는 한번에 보낼수 잇게 구현해두셨다고합니다.
     */

    /**
     * 외양간 놓인곳 정보 187Line getValidBarnPositions()
     */
}
