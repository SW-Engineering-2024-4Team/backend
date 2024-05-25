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
    private boolean firstFenceBuilt = false; // 최초 울타리 여부를 저장하는 변수
    private List<int[]> fenceCoordinates = new ArrayList<>(); // 울타리 설치할 좌표 리스트
    private FamilyMember[][] familyMembers;

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

//    public void placeFamilyMember(ActionRoundCard card) {
//        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
//        // TODO 가족 선택 로직
//        for (int i = 0; i < familyMembers.length; i++) {
//            for (int j = 0; j < familyMembers[i].length; j++) {
//                if (familyMembers[i][j] != null && familyMembers[i][j].isAdult() && !familyMembers[i][j].isUsed()) {
//                    FamilyMember selectedMember = familyMembers[i][j];
//                    System.out.println("Placing family member at (" + i + ", " + j + ") for player " + this.id);
//                    card.execute(this);  // 카드 실행 로직 확인 필요
//                    selectedMember.setUsed(true); // 가족 구성원을 사용 상태로 설정
//                    gameController.getMainBoard().setOccupyingFamilyMember(card, selectedMember);
//                    gameController.getMainBoard().placeFamilyMember(card, selectedMember);
//                    System.out.println("Player " + this.id + " placed a family member on card: " + card.getName());
//                    System.out.println("Family member used status: " + selectedMember.isUsed());
//                    return;
//                }
//            }
//        }
//        System.out.println("No available family member found for player " + this.id);
//    }

    public void placeFamilyMember(ActionRoundCard card) {
        if (!gameController.getMainBoard().canPlaceFamilyMember(card)) {
            System.out.println("Card " + card.getName() + " is already occupied.");
            return;
        }

        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
        for (int i = 0; i < familyMembers.length; i++) {
            for (int j = 0; j < familyMembers[i].length; j++) {
                if (familyMembers[i][j] != null && familyMembers[i][j].isAdult() && !familyMembers[i][j].isUsed()) {
                    FamilyMember selectedMember = familyMembers[i][j];
                    System.out.println("Placing family member at (" + i + ", " + j + ") for player " + this.id);
                    card.execute(this);
                    selectedMember.setUsed(true);
                    gameController.getMainBoard().placeFamilyMember(card);
                    System.out.println("Player " + this.id + " placed a family member on card: " + card.getName());
                    System.out.println("Family member used status: " + selectedMember.isUsed());
                    return;
                }
            }
        }
        System.out.println("No available family member found for player " + this.id);
    }

    public void resetFamilyMembers() {
        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
        for (FamilyMember[] row : familyMembers) {
            for (FamilyMember familyMember : row) {
                if (familyMember != null) {
                    familyMember.setUsed(false);
                    familyMember.resetPosition();
                    System.out.println("Reset family member at (" + familyMember.getOriginalX() + ", " + familyMember.getOriginalY() + ") for player " + this.id);
                }
            }
        }
    }
    public boolean hasAvailableFamilyMembers() {
        for (FamilyMember[] row : playerBoard.getFamilyMembers()) {
            for (FamilyMember member : row) {
                if (member != null && member.isAdult() && !member.isUsed()) {
                    return true;
                }
            }
        }
        return false;
    }

//    public void resetFamilyMembers() {
//        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
//        for (FamilyMember[] row : familyMembers) {
//            for (FamilyMember familyMember : row) {
//                if (familyMember != null) {
//                    familyMember.setUsed(false);
//                    familyMember.resetPosition();
//                    System.out.println("Reset family member at (" + familyMember.getOriginalX() + ", " + familyMember.getOriginalY() + ") for player " + this.id);
//                }
//            }
//        }
//        gameController.getMainBoard().resetFamilyMembersOnCards();
//        System.out.println("All family members have returned home for player " + this.id);
//    }





    public void addResource(String resource, int amount) {
        resources.put(resource, resources.getOrDefault(resource, 0) + amount);
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
        // TODO 플레이어가 카드를 선택하는 로직 구현
        // 플레이어가 선택하는 로직 (여기서는 예시로 랜덤 선택)
        Random random = new Random();
        return bakingCards.get(random.nextInt(bakingCards.size()));
    }

    public int selectGrainForBaking(int maxAmount) {
        // 예를 들어, 최대값 이하의 랜덤한 수를 선택한다고 가정합니다.
        // TODO 실제 게임에서는 UI를 통해 플레이어가 선택할 수 있어야 합니다.
        Random random = new Random();
        return random.nextInt(maxAmount + 1); // 0부터 maxAmount 사이의 값을 반환
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


    // 새로 추가된 가족 구성원 중 하나를 반환하는 메서드
    public FamilyMember getNewFamilyMember() {
        if (!newFamilyMembers.isEmpty()) {
            return newFamilyMembers.get(0); // 예시로 첫 번째 가족 구성원을 반환
        }
        return null;
    }

    // 동물 추가 메서드
    public boolean addAnimal(Animal animal) {
//        Animal newAnimal = new Animal(-1, -1, type); // 보드 외부에 위치한 동물
        newAnimals.add(animal);
        System.out.println(animal.getType() + " 새끼 동물이 추가되었습니다. 울타리나 방에 배치하세요.");

        return true;
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

    // 새로 추가된 동물들을 플레이어 보드에 배치하는 메서드
    public int placeNewAnimals() {
        int placedCount = 0;
        Iterator<Animal> iterator = newAnimals.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            Set<int[]> validPositions = playerBoard.getValidAnimalPositions(animal.getType());
            if (!validPositions.isEmpty()) {
                // TODO 동물을 배치하는 로직 구현
                int[] position = validPositions.iterator().next(); // 예시로 첫 번째 유효 위치 선택
                placeAnimalOnBoard(animal, position[0], position[1]);
                placedCount++;
                iterator.remove();
            } else {
                System.out.println(animal.getType() + " 방생됨.");
                iterator.remove();
            }
        }
        return placedCount;
    }

    // 외양간 짓기 메서드
    public void buildBarn(int x, int y) {
        if (playerBoard.canBuildBarn(x, y)) {
            Map<String, Integer> cost = Map.of("wood", 2);
            if (checkResources(cost)) {
                payResources(cost);
                playerBoard.buildBarn(x, y);
            } else {
                System.out.println("자원이 부족합니다.");
            }
        } else {
            System.out.println("외양간을 지을 수 없습니다.");
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
        return upgradeOptions.isEmpty() ? null : upgradeOptions.get(0);
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

    // 울타리 한 칸을 선택하는 메서드
//    public boolean selectFenceTile(int x, int y) {
//        List<int[]> newFenceCoordinates = new ArrayList<>(fenceCoordinates);
//        newFenceCoordinates.add(new int[]{x, y});
//
//        int requiredWood = playerBoard.calculateRequiredWoodForFences(newFenceCoordinates);
//        if (checkResources(Map.of("wood", requiredWood))) {
//            fenceCoordinates = newFenceCoordinates;
//            return true;
//        } else {
//            System.out.println("나무 자원이 부족합니다.");
//            return false;
//        }
//
//    }
    public void selectFenceTile(int x, int y) {
        List<int[]> selectedPositions = new ArrayList<>();
        selectedPositions.add(new int[]{x, y});
        int requiredWood = playerBoard.calculateRequiredWoodForFences(selectedPositions);
        System.out.println("Selected position: (" + x + ", " + y + "), Wood needed: " + requiredWood);
        Map<String, Integer> cost = new HashMap<>();
        cost.put("wood", requiredWood);
        if (checkResources(cost)) {
            payResources(cost);
            playerBoard.buildFences(selectedPositions);
            System.out.println("Fence built at: (" + x + ", " + y + ")");
        } else {
            System.out.println("Not enough resources to build fence at: (" + x + ", " + y + ")");
        }
    }

//    public void buildFence() {
//        Set<int[]> validPositions;
//        if (firstFenceBuilt) {
//            validPositions = playerBoard.getValidFencePositions();
//        } else {
//            validPositions = playerBoard.getInitialFencePositions();
//        }
//
//        if (!validPositions.isEmpty()) {
//            // 예시로 유효한 첫 번째 위치를 선택
//            int[] position = validPositions.iterator().next();
//            if (selectFenceTile(position[0], position[1])) {
//                // 플레이어가 울타리를 선택한 이후 모든 울타리가 이어져야 함
//                boolean fenceBuildingComplete = false;
//                while (!fenceBuildingComplete) {
//                    validPositions = playerBoard.getValidFencePositions();
//                    if (!validPositions.isEmpty()) {
//                        position = validPositions.iterator().next();
//                        if (!selectFenceTile(position[0], position[1])) {
//                            fenceBuildingComplete = true;
//                        }
//                    } else {
//                        fenceBuildingComplete = true;
//                    }
//                }
//                finalizeFenceBuilding();
//            } else {
//                System.out.println("울타리를 선택하지 못했습니다.");
//            }
//        } else {
//            System.out.println("울타리를 지을 유효한 위치가 없습니다.");
//        }
//    }


    public boolean canContinueFenceBuilding() {
        // TODO: 실제 게임 로직에 따라 플레이어가 울타리 건설을 계속할지 선택하게 함
        // 예시로는 랜덤 선택
        return new Random().nextBoolean();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void resetResources() {
        for (String resource : resources.keySet()) {
            resources.put(resource, 0);
        }
    }

    public String getName() {
        return name;
    }
}
