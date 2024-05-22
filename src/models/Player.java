package models;

import cards.common.*;
import cards.majorimprovement.MajorImprovementCard;
import controllers.GameController;
import enums.ExchangeTiming;
import enums.RoomType;

import java.util.*;

public class Player {
    private final ArrayList<CommonCard> majorImprovementCards;
    private String id;
    private String name;
    private Map<String, Integer> resources;
    private List<CommonCard> occupationCards;
    private List<CommonCard> minorImprovementCards;
    private List<CommonCard> activeCards;
    private PlayerBoard playerBoard;
    private int score;
    private boolean isFirstPlayer;
    private GameController gameController;
    private List<FamilyMember> newFamilyMembers; // 새로 추가된 가족 구성원 리스트
    private List<Animal> newAnimals; // 새로 추가된 동물 리스트


    public Player(String id, String name, GameController gameController) {
        this.id = id;
        this.name = name;
        this.resources = new HashMap<>();
        this.occupationCards = new ArrayList<>();
        this.minorImprovementCards = new ArrayList<>();
        this.majorImprovementCards = new ArrayList<>();
        this.activeCards = new ArrayList<>();
        this.playerBoard = new PlayerBoard();
        this.isFirstPlayer = false;
        this.gameController = gameController;
        this.newFamilyMembers = new ArrayList<>();
        this.newAnimals = new ArrayList<>();
        initializeResources();

    }

    private void initializeResources() {
        resources.put("wood", 0);
        resources.put("clay", 0);
        resources.put("stone", 0);
        resources.put("grain", 0);
        resources.put("food", 0);
        resources.put("beggingCard", 0);
        resources.put("sheep", 0);
    }

    public void addCard(CommonCard card, String type) {
        if (type.equals("occupation")) {
            occupationCards.add(card);
        } else if (type.equals("minorImprovement")) {
            minorImprovementCards.add(card);
        } else {
            activeCards.add(card);
        }
    }

    public List<CommonCard> getOccupationCards() {
        return occupationCards;
    }

    public List<CommonCard> getMinorImprovementCards() {
        return minorImprovementCards;
    }

    public List<CommonCard> getActiveCards() {
        return activeCards;
    }

    public List<CommonCard> getMajorImprovementCards() {
        return majorImprovementCards;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean hasAvailableFamilyMembers() {
        for (FamilyMember[] row : playerBoard.getFamilyMembers()) {
            for (FamilyMember member : row) {
                if (member != null && member.isAdult()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void resetFamilyMembers() {
        playerBoard.resetFamilyMembers();
    }

    public void placeFamilyMember(int x, int y, ActionRoundCard card) {
        if (playerBoard.getFamilyMembers()[x][y] != null) {
            card.execute(this);
            playerBoard.removeFamilyMember(x, y);
        }
    }

    public void addResource(String resource, int amount) {
        if (resource.equals("sheep")) {
            // 양 자원을 추가할 경우
            resources.put(resource, resources.getOrDefault(resource, 0) + amount);
        } else {
            // 기존 자원 처리
            resources.put(resource, resources.getOrDefault(resource, 0) + amount);
        }
    }

    public int getResource(String resource) {
        return resources.getOrDefault(resource, 0);
    }

    public Map<String, Integer> getResources() {
        return resources;
    }

    public void convertBabiesToAdults() {
        for (FamilyMember[] row : playerBoard.getFamilyMembers()) {
            for (FamilyMember member : row) {
                if (member != null && !member.isAdult()) {
                    member.setAdult(true);
                }
            }
        }
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean isFirstPlayer) {
        this.isFirstPlayer = isFirstPlayer;
    }

    public boolean chooseOption() {
        return new Random().nextBoolean();
    }

    public GameController getGameController() {
        return gameController;
    }

    public void moveToActiveCards(CommonCard card) {
        if (card instanceof MajorImprovementCard) {
            majorImprovementCards.remove(card);
        } else if (card instanceof UnifiedCard) {
            occupationCards.remove(card);
        } else if (card instanceof UnifiedCard) {
            minorImprovementCards.remove(card);
        }

        activeCards.add(card);
    }

    public List<ExchangeableCard> getExchangeableCards(ExchangeTiming timing) {
        List<ExchangeableCard> exchangeableCards = new ArrayList<>();
        for (CommonCard card : activeCards) {
            if (card instanceof ExchangeableCard) {
                ExchangeableCard exchangeableCard = (ExchangeableCard) card;
                if (exchangeableCard.canExchange(timing) || exchangeableCard.canExchange(ExchangeTiming.ANYTIME)) {
                    exchangeableCards.add(exchangeableCard);
                }
            }
        }
        return exchangeableCards;
    }

    public void executeExchange(ExchangeableCard card, String fromResource, String toResource, int amount) {
        card.executeExchange(this, fromResource, toResource, amount);
    }

    public void useBakingCard(BakingCard card) {
        card.triggerBreadBaking(this);
    }

    public void addMajorImprovementCard(CommonCard card) {
        majorImprovementCards.add(card);
    }

    public void removeMajorImprovementCard(CommonCard card) {
        majorImprovementCards.remove(card);
    }

    public boolean checkResources(Map<String, Integer> cost) {
        for (Map.Entry<String, Integer> entry : cost.entrySet()) {
            if (resources.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    public void payResources(Map<String, Integer> cost) {
        for (Map.Entry<String, Integer> entry : cost.entrySet()) {
            addResource(entry.getKey(), -entry.getValue());
        }
    }

    public void useUnifiedCard(UnifiedCard card) {
        card.execute(this);
        moveToActiveCards(card);
    }

    // 주요 설비 카드 선택 로직
    public BakingCard selectBakingCard(List<BakingCard> bakingCards) {
        // 플레이어가 선택하는 로직 (여기서는 예시로 랜덤 선택)
        Random random = new Random();
        return bakingCards.get(random.nextInt(bakingCards.size()));
    }

    // 집 짓기 메서드
    public void buildHouse(int x, int y, RoomType type) {
        if (playerBoard.canBuildHouse(x, y, type, resources)) {
            Map<String, Integer> cost = getHouseResourceCost(type);
            if (checkResources(cost)) {
                payResources(cost);
                playerBoard.buildHouse(x, y, type);
            } else {
                // 자원이 부족하다는 메시지 표시
                System.out.println("자원이 부족합니다.");
            }
        } else {
            // 집을 지을 수 없는 조건이라는 메시지 표시
            System.out.println("집을 지을 수 없습니다.");
        }
    }

    public Map<String, Integer> getHouseResourceCost(RoomType type) {
        Map<String, Integer> cost = new HashMap<>();
        switch (type) {
            case WOOD:
                cost.put("wood", 5);
                break;
            case CLAY:
                cost.put("clay", 5);
                break;
            case STONE:
                cost.put("stone", 5);
                break;
        }
        return cost;
    }

    // 가족 구성원 추가 메서드
    public void addFamilyMember() {
        if (playerBoard.hasEmptyRoom()) {
            FamilyMember newMember = new FamilyMember(-1, -1, false); // 보드 외부에 위치한 신생아
            newFamilyMembers.add(newMember);
            System.out.println("새로운 가족 구성원이 추가되었습니다. 빈 방에 배치하세요.");
        } else {
            System.out.println("빈 방이 없습니다.");
        }
    }

    // 새 가족 구성원을 빈 방에 배치하는 메서드
    public void placeFamilyMemberInRoom(FamilyMember familyMember, int x, int y) {
        if (playerBoard.isEmptyRoom(x, y)) {
            playerBoard.addFamilyMemberToBoard(familyMember, x, y);
            newFamilyMembers.remove(familyMember);
        } else {
            System.out.println("해당 방은 이미 사용 중입니다.");
        }
    }

    // 동물 추가 메서드
    public void addAnimal(String type) {
        Animal newAnimal = new Animal(-1, -1, type); // 보드 외부에 위치한 동물
        newAnimals.add(newAnimal);
        System.out.println("새로운 동물이 추가되었습니다. 울타리나 집에 배치하세요.");
    }

    // 새 동물을 추가하는 메서드
    public boolean addNewAnimal(Animal animal) {
        newAnimals.add(animal);
        System.out.println(animal.getType() + " 새끼 동물이 추가되었습니다. 울타리나 방에 배치하세요.");
        return true;
    }

    // 새 동물을 울타리나 방에 배치하는 메서드
    public void placeAnimalOnBoard(Animal animal, int x, int y) {
        if (playerBoard.canPlaceAnimal(x, y, animal.getType())) {
            playerBoard.addAnimalToBoard(animal, x, y);
            newAnimals.remove(animal);
        } else {
            System.out.println("해당 위치에는 동물을 배치할 수 없습니다.");
        }
    }


    // 울타리의 동물 수용 용량을 계산하는 메서드
    public int calculateTotalAnimalCapacity() {
        return playerBoard.getAnimalCapacity();
    }

    public void renovateHouse(RoomType newType) {
        playerBoard.renovateRooms(newType, this);
    }

    public RoomType chooseRoomTypeForRenovation() {
        // 게임 로직에 따라 플레이어가 업그레이드할 방 타입을 선택하는 로직
        // 예시로는 나무에서 흙, 흙에서 돌로 업그레이드 가능하도록 구현
        List<RoomType> upgradeOptions = new ArrayList<>();
        if (hasWoodRooms()) {
            upgradeOptions.add(RoomType.CLAY);
        }
        if (hasClayRooms()) {
            upgradeOptions.add(RoomType.STONE);
        }
        return upgradeOptions.get(0);
    }

    private boolean hasWoodRooms() {
        for (Tile[] row : playerBoard.getTiles()) {
            for (Tile tile : row) {
                if (tile instanceof Room && ((Room) tile).getType() == RoomType.WOOD) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasClayRooms() {
        for (Tile[] row : playerBoard.getTiles()) {
            for (Tile tile : row) {
                if (tile instanceof Room && ((Room) tile).getType() == RoomType.CLAY) {
                    return true;
                }
            }
        }
        return false;
    }


    // 울타리 설치할 좌표 리스트
    private List<int[]> fenceCoordinates = new ArrayList<>();

    // 울타리 한 칸을 선택하는 메서드
    public boolean selectFenceTile(int x, int y) {
        int requiredWood = calculateRequiredWoodForSingleTile(x, y);
        if (checkResources(Map.of("wood", requiredWood))) {
            fenceCoordinates.add(new int[]{x, y});
            return true;
        } else {
            System.out.println("나무 자원이 부족합니다.");
            return false;
        }
    }

    // 울타리 설치를 완료하고 자원을 지불하는 메서드
    public boolean finalizeFenceBuilding() {
        int totalRequiredWood = 0;
        for (int[] coord : fenceCoordinates) {
            totalRequiredWood += calculateRequiredWoodForSingleTile(coord[0], coord[1]);
        }

        if (checkResources(Map.of("wood", totalRequiredWood))) {
            payResources(Map.of("wood", totalRequiredWood));
            for (int[] coord : fenceCoordinates) {
                playerBoard.buildFence(coord[0], coord[1]);
            }
            fenceCoordinates.clear();
            return true;
        } else {
            System.out.println("나무 자원이 부족합니다.");
            return false;
        }
    }

    // 울타리 한 칸을 설치하는 데 필요한 나무 자원의 양을 계산하는 메서드
    private int calculateRequiredWoodForSingleTile(int x, int y) {
        int segments = 0;
        if (x > 0 && !playerBoard.hasFence(x - 1, y)) segments++;
        if (x < playerBoard.getTiles().length - 1 && !playerBoard.hasFence(x + 1, y)) segments++;
        if (y > 0 && !playerBoard.hasFence(x, y - 1)) segments++;
        if (y < playerBoard.getTiles()[0].length - 1 && !playerBoard.hasFence(x, y + 1)) segments++;
        return segments;
    }

}
