//package models;
//
//import cards.common.*;
//import cards.majorimprovement.MajorImprovementCard;
//import controllers.GameController;
//import enums.ExchangeTiming;
//import enums.RoomType;
//
//import java.util.*;
//
//public class Player {
//    private final ArrayList<CommonCard> majorImprovementCards;
//    private String id;
//    private String name;
//    private Map<String, Integer> resources;
//    private List<CommonCard> occupationCards;
//    private List<CommonCard> minorImprovementCards;
//    private List<CommonCard> activeCards;
//    private PlayerBoard playerBoard;
//    private int score;
//    private boolean isFirstPlayer;
//    private GameController gameController;
//    private List<FamilyMember> newFamilyMembers; // 새로 추가된 가족 구성원 리스트
//    private List<Animal> newAnimals; // 새로 추가된 동물 리스트
//    private boolean firstFenceBuilt = false; // 최초 울타리 여부를 저장하는 변수
//    private List<int[]> fenceCoordinates = new ArrayList<>(); // 울타리 설치할 좌표 리스트
//    private FamilyMember[][] familyMembers;
//    private List<Effect> activeEffects;
//    private boolean chooseOption;
//
//    public Player(String id, String name, GameController gameController) {
//        this.id = id;
//        this.name = name;
//        this.resources = new HashMap<>();
//        this.occupationCards = new ArrayList<>();
//        this.minorImprovementCards = new ArrayList<>();
//        this.majorImprovementCards = new ArrayList<>();
//        this.activeCards = new ArrayList<>();
//        this.playerBoard = new PlayerBoard();
//        this.isFirstPlayer = false;
//        this.gameController = gameController;
//        this.newFamilyMembers = new ArrayList<>();
//        this.newAnimals = new ArrayList<>();
//        this.activeEffects = new ArrayList<>();
//        initializeResources();
//    }
//
//    private void initializeResources() {
//        resources.put("wood", 0);
//        resources.put("clay", 0);
//        resources.put("stone", 0);
//        resources.put("grain", 0);
//        resources.put("food", 0);
//        resources.put("beggingCard", 0);
//        resources.put("sheep", 0);
//    }
//
//    public List<Effect> getActiveEffects() {
//        return activeEffects;
//    }
//
//    public void addActiveEffect(Effect effect) {
//        activeEffects.add(effect);
//    }
//
//    public void addCard(CommonCard card, String type) {
//        if (type.equals("occupation")) {
//            occupationCards.add(card);
//        } else if (type.equals("minorImprovement")) {
//            minorImprovementCards.add(card);
//        } else {
//            activeCards.add(card);
//        }
//    }
//
//    public List<CommonCard> getOccupationCards() {
//        return occupationCards;
//    }
//
//    public List<CommonCard> getMinorImprovementCards() {
//        return minorImprovementCards;
//    }
//
//    public List<CommonCard> getActiveCards() {
//        return activeCards;
//    }
//
//    public List<CommonCard> getMajorImprovementCards() {
//        return majorImprovementCards;
//    }
//
//    public PlayerBoard getPlayerBoard() {
//        return playerBoard;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//    public void setScore(int score) {
//        this.score = score;
//    }
//
////    public void placeFamilyMember(ActionRoundCard card) {
////        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
////        // TODO 가족 선택 로직
////        for (int i = 0; i < familyMembers.length; i++) {
////            for (int j = 0; j < familyMembers[i].length; j++) {
////                if (familyMembers[i][j] != null && familyMembers[i][j].isAdult() && !familyMembers[i][j].isUsed()) {
////                    FamilyMember selectedMember = familyMembers[i][j];
////                    System.out.println("Placing family member at (" + i + ", " + j + ") for player " + this.id);
////                    card.execute(this);  // 카드 실행 로직 확인 필요
////                    selectedMember.setUsed(true); // 가족 구성원을 사용 상태로 설정
////                    gameController.getMainBoard().setOccupyingFamilyMember(card, selectedMember);
////                    gameController.getMainBoard().placeFamilyMember(card, selectedMember);
////                    System.out.println("Player " + this.id + " placed a family member on card: " + card.getName());
////                    System.out.println("Family member used status: " + selectedMember.isUsed());
////                    return;
////                }
////            }
////        }
////        System.out.println("No available family member found for player " + this.id);
////    }
//
////    public void placeFamilyMember(ActionRoundCard card) {
////        if (!gameController.getMainBoard().canPlaceFamilyMember(card)) {
////            System.out.println("Card " + card.getName() + " is already occupied.");
////            return;
////        }
////
////        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
////        for (int i = 0; i < familyMembers.length; i++) {
////            for (int j = 0; j < familyMembers[i].length; j++) {
////                if (familyMembers[i][j] != null && familyMembers[i][j].isAdult() && !familyMembers[i][j].isUsed()) {
////                    FamilyMember selectedMember = familyMembers[i][j];
////                    System.out.println("Placing family member at (" + i + ", " + j + ") for player " + this.id);
////                    card.execute(this);
////                    selectedMember.setUsed(true);
////                    gameController.getMainBoard().placeFamilyMember(card);
////                    System.out.println("Player " + this.id + " placed a family member on card: " + card.getName());
////                    System.out.println("Family member used status: " + selectedMember.isUsed());
////                    return;
////                }
////            }
////        }
////        System.out.println("No available family member found for player " + this.id);
////    }
//
//    public void placeFamilyMember(ActionRoundCard card) {
//        if (!gameController.getMainBoard().canPlaceFamilyMember(card)) {
//            System.out.println("Card " + card.getName() + " is already occupied.");
//            return;
//        }
//
//        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
//        for (int i = 0; i < familyMembers.length; i++) {
//            for (int j = 0; j < familyMembers[i].length; j++) {
//                if (familyMembers[i][j] != null && familyMembers[i][j].isAdult() && !familyMembers[i][j].isUsed()) {
//                    FamilyMember selectedMember = familyMembers[i][j];
//                    System.out.println("Placing family member at (" + i + ", " + j + ") for player " + this.id);
//                    gameController.getMainBoard().placeFamilyMember(card); // 점유 상태로 먼저 설정
//                    card.execute(this); // 카드 실행 로직
//                    selectedMember.setUsed(true);
//                    System.out.println("Player " + this.id + " placed a family member on card: " + card.getName());
//                    System.out.println("Family member used status: " + selectedMember.isUsed());
//                    return;
//                }
//            }
//        }
//        System.out.println("No available family member found for player " + this.id);
//    }
//
////    public void placeFamilyMember(ActionRoundCard card) {
////        if (!gameController.getMainBoard().canPlaceFamilyMember(card)) {
////            System.out.println("Card " + card.getName() + " is already occupied.");
////            return;
////        }
////
////        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
////        for (int i = 0; i < familyMembers.length; i++) {
////            for (int j = 0; j < familyMembers[i].length; j++) {
////                if (familyMembers[i][j] != null && familyMembers[i][j].isAdult() && !familyMembers[i][j].isUsed()) {
////                    FamilyMember selectedMember = familyMembers[i][j];
////                    System.out.println("Placing family member at (" + i + ", " + j + ") for player " + this.id);
////
////                    // 카드 실행 성공 시 점유 상태로 설정
////                    boolean executedSuccessfully = card.executeWithCheck(this);
////                    if (executedSuccessfully) {
////                        gameController.getMainBoard().placeFamilyMember(card); // 점유 상태로 설정
////                        selectedMember.setUsed(true);
////                        System.out.println("Player " + this.id + " placed a family member on card: " + card.getName());
////                        System.out.println("Family member used status: " + selectedMember.isUsed());
////                    } else {
////                        System.out.println("Failed to execute card action.");
////                    }
////                    return;
////                }
////            }
////        }
////        System.out.println("No available family member found for player " + this.id);
////    }
//
//
//
//
//
//
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
//    }
//    public boolean hasAvailableFamilyMembers() {
//        for (FamilyMember[] row : playerBoard.getFamilyMembers()) {
//            for (FamilyMember member : row) {
//                if (member != null && member.isAdult() && !member.isUsed()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
////    public void resetFamilyMembers() {
////        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
////        for (FamilyMember[] row : familyMembers) {
////            for (FamilyMember familyMember : row) {
////                if (familyMember != null) {
////                    familyMember.setUsed(false);
////                    familyMember.resetPosition();
////                    System.out.println("Reset family member at (" + familyMember.getOriginalX() + ", " + familyMember.getOriginalY() + ") for player " + this.id);
////                }
////            }
////        }
////        gameController.getMainBoard().resetFamilyMembersOnCards();
////        System.out.println("All family members have returned home for player " + this.id);
////    }
//
//
//
//
//
//    public void addResource(String resource, int amount) {
//        resources.put(resource, resources.getOrDefault(resource, 0) + amount);
//    }
//
//    public int getResource(String resource) {
//        return resources.getOrDefault(resource, 0);
//    }
//
//    public Map<String, Integer> getResources() {
//        return resources;
//    }
//
//    public void convertBabiesToAdults() {
//        for (FamilyMember[] row : playerBoard.getFamilyMembers()) {
//            for (FamilyMember member : row) {
//                if (member != null && !member.isAdult()) {
//                    member.setAdult(true);
//                }
//            }
//        }
//    }
//
//    public boolean isFirstPlayer() {
//        return isFirstPlayer;
//    }
//
//    public void setFirstPlayer(boolean isFirstPlayer) {
//        this.isFirstPlayer = isFirstPlayer;
//    }
//
////    public boolean chooseOption() {
////        return new Random().nextBoolean();
////    }
//
//    public GameController getGameController() {
//        return gameController;
//    }
//
//    public void moveToActiveCards(CommonCard card) {
//        if (card instanceof MajorImprovementCard) {
//            majorImprovementCards.remove(card);
//        } else if (card instanceof UnifiedCard) {
//            occupationCards.remove(card);
//            minorImprovementCards.remove(card); // UnifiedCard는 occupationCards와 minorImprovementCards 둘 다에 있을 수 있습니다.
//        }
//        activeCards.add(card);
//    }
//
//
//    public List<ExchangeableCard> getExchangeableCards(ExchangeTiming timing) {
//        List<ExchangeableCard> exchangeableCards = new ArrayList<>();
//        for (CommonCard card : activeCards) {
//            if (card instanceof ExchangeableCard) {
//                ExchangeableCard exchangeableCard = (ExchangeableCard) card;
//                if (exchangeableCard.canExchange(timing) || exchangeableCard.canExchange(ExchangeTiming.ANYTIME)) {
//                    exchangeableCards.add(exchangeableCard);
//                }
//            }
//        }
//        return exchangeableCards;
//    }
//
//
//    public void executeExchange(ExchangeableCard card, String fromResource, String toResource, int amount) {
//        card.executeExchange(this, fromResource, toResource, amount);
//    }
//
//    public void useBakingCard(BakingCard card) {
//        card.triggerBreadBaking(this);
//    }
//
//    public void addMajorImprovementCard(CommonCard card) {
//        majorImprovementCards.add(card);
//    }
//
//    public void removeMajorImprovementCard(CommonCard card) {
//        majorImprovementCards.remove(card);
//    }
//
//    public boolean checkResources(Map<String, Integer> cost) {
//        for (Map.Entry<String, Integer> entry : cost.entrySet()) {
//            if (resources.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public void payResources(Map<String, Integer> cost) {
//        for (Map.Entry<String, Integer> entry : cost.entrySet()) {
//            addResource(entry.getKey(), -entry.getValue());
//        }
//    }
//
//    public void useUnifiedCard(UnifiedCard card) {
//        card.execute(this);
//        moveToActiveCards(card);
//    }
//
//    // 주요 설비 카드 선택 로직
//    public BakingCard selectBakingCard(List<BakingCard> bakingCards) {
//        // TODO 플레이어가 카드를 선택하는 로직 구현
//        // 플레이어가 선택하는 로직 (여기서는 예시로 랜덤 선택)
//        Random random = new Random();
//        return bakingCards.get(random.nextInt(bakingCards.size()));
//    }
//
//    public int selectGrainForBaking(int maxAmount) {
//        // 예를 들어, 최대값 이하의 랜덤한 수를 선택한다고 가정합니다.
//        // TODO 실제 게임에서는 UI를 통해 플레이어가 선택할 수 있어야 합니다.
//        Random random = new Random();
//        return random.nextInt(maxAmount + 1); // 0부터 maxAmount 사이의 값을 반환
//    }
//
//    // 집 짓기 메서드
//    public void buildHouse(int x, int y, RoomType type) {
//        if (playerBoard.canBuildHouse(x, y, type, resources)) {
//            Map<String, Integer> cost = getHouseResourceCost(type);
//            if (checkResources(cost)) {
//                payResources(cost);
//                playerBoard.buildHouse(x, y, type);
//            } else {
//                // 자원이 부족하다는 메시지 표시
//                System.out.println("자원이 부족합니다.");
//            }
//        } else {
//            // 집을 지을 수 없는 조건이라는 메시지 표시
//            System.out.println("집을 지을 수 없습니다.");
//        }
//    }
//
//    public Map<String, Integer> getHouseResourceCost(RoomType type) {
//        Map<String, Integer> cost = new HashMap<>();
//        switch (type) {
//            case WOOD:
//                cost.put("wood", 5);
//                break;
//            case CLAY:
//                cost.put("clay", 5);
//                break;
//            case STONE:
//                cost.put("stone", 5);
//                break;
//        }
//        return cost;
//    }
//
//    // 가족 구성원 추가 메서드
//    public boolean addFamilyMember() {
//        List<int[]> emptyRooms = playerBoard.getEmptyRoomPositions();
//        if (!emptyRooms.isEmpty()) {
//            FamilyMember newMember = new FamilyMember(-1, -1, false); // 보드 외부에 위치한 신생아
//            newFamilyMembers.add(newMember);
//            System.out.println("새로운 가족 구성원이 추가되었습니다. 빈 방에 배치하세요.");
//            return true;
//        } else {
//            System.out.println("빈 방이 없습니다.");
//            return false;
//        }
//    }
//
//    // 새 가족 구성원을 빈 방에 배치하는 메서드
//    public void placeFamilyMemberInRoom(FamilyMember familyMember, int x, int y) {
//        if (playerBoard.isEmptyRoom(x, y)) {
//            playerBoard.addFamilyMemberToBoard(familyMember, x, y);
//            newFamilyMembers.remove(familyMember);
//            System.out.println("가족 구성원이 방에 배치되었습니다: (" + x + ", " + y + ")");
//        } else {
//            System.out.println("해당 방은 이미 사용 중입니다.");
//        }
//    }
//
//    // 새로 추가된 가족 구성원 중 하나를 반환하는 메서드
//    public FamilyMember getNewFamilyMember() {
//        if (!newFamilyMembers.isEmpty()) {
//            return newFamilyMembers.get(0); // 예시로 첫 번째 가족 구성원을 반환
//        }
//        return null;
//    }
//
//    // 동물 추가 메서드
//    public boolean addAnimal(Animal animal) {
//        newAnimals.add(animal);
//        System.out.println(animal.getType() + " 새끼 동물이 추가되었습니다. 울타리나 방에 배치하세요.");
//
//        return true;
//    }
//
//    // 새 동물을 추가하는 메서드
//    public boolean addNewAnimal(Animal animal) {
//        newAnimals.add(animal);
//        System.out.println(animal.getType() + " 새끼 동물이 추가되었습니다. 빈 공간에 배치하세요.");
//        return true;
//    }
//
//    // 새 동물을 울타리나 방에 배치하는 메서드
//    public void placeAnimalOnBoard(Animal animal, int x, int y) {
//        if (playerBoard.canPlaceAnimal(x, y, animal.getType())) {
//            playerBoard.addAnimalToBoard(animal, x, y);
//            newAnimals.remove(animal);
//        } else {
//            System.out.println("해당 위치에는 동물을 배치할 수 없습니다.");
//        }
//    }
//
//    // 새로 추가된 동물들을 플레이어 보드에 배치하는 메서드
////    public int placeNewAnimals() {;
////        int placedCount = 0;
////        List<Animal> animalsToRemove = new ArrayList<>();
////        Iterator<Animal> iterator = newAnimals.iterator();
////        while (iterator.hasNext()) {
////            Animal animal = iterator.next();
////            Set<int[]> validPositions = playerBoard.getValidAnimalPositions(animal.getType());
////            if (!validPositions.isEmpty()) {
////                // TODO 동물을 배치하는 로직 구현
////                int[] position = validPositions.iterator().next(); // 예시로 첫 번째 유효 위치 선택
////                System.out.println("동물 배치 위치: (" + position[0] + ", " + position[1] + ")");
////                placeAnimalOnBoard(animal, position[0], position[1]);
////                placedCount++;
////                iterator.remove();
////            } else {
////                System.out.println(animal.getType() + " 방생됨.");
////                iterator.remove();
////            }
////        }
////        newAnimals.removeAll(animalsToRemove);
////        return placedCount;
////    }
//
//    // 새로 추가된 동물들을 플레이어 보드에 배치하는 메서드
//    public int placeNewAnimals() {
//        int placedCount = 0;
//        List<Animal> animalsToRemove = new ArrayList<>();
//        Iterator<Animal> iterator = newAnimals.iterator();
//
//        while (iterator.hasNext()) {
//            Animal animal = iterator.next();
//            Set<int[]> validPositions = playerBoard.getValidAnimalPositions(animal.getType());
//
//            if (!validPositions.isEmpty()) {
//                // 동물을 배치하는 로직 구현
//                int[] position = validPositions.iterator().next(); // 예시로 첫 번째 유효 위치 선택
//                System.out.println("동물 배치 위치: (" + position[0] + ", " + position[1] + ")");
//                placeAnimalOnBoard(animal, position[0], position[1]);
//                placedCount++;
//                animalsToRemove.add(animal); // 제거할 동물을 리스트에 추가
//            } else {
//                System.out.println(animal.getType() + " 방생됨.");
//                animalsToRemove.add(animal); // 제거할 동물을 리스트에 추가
//            }
//        }
//
//        // 동물을 한 번에 제거
//        newAnimals.removeAll(animalsToRemove);
//        return placedCount;
//    }
//
//
//    // 외양간 짓기 메서드
//    public void buildBarn(int x, int y) {
//        if (playerBoard.canBuildBarn(x, y)) {
//            Map<String, Integer> cost = Map.of("wood", 2);
//            if (checkResources(cost)) {
//                payResources(cost);
//                playerBoard.buildBarn(x, y);
//            } else {
//                System.out.println("자원이 부족합니다.");
//            }
//        } else {
//            System.out.println("외양간을 지을 수 없습니다.");
//        }
//    }
//
//    // 울타리의 동물 수용 용량을 계산하는 메서드
//    public int calculateTotalAnimalCapacity() {
//        return playerBoard.getAnimalCapacity();
//    }
//
//    public void renovateHouse(RoomType newType) {
//        playerBoard.renovateRooms(newType, this);
//    }
//
//    public RoomType chooseRoomTypeForRenovation() {
//        // 게임 로직에 따라 플레이어가 업그레이드할 방 타입을 선택하는 로직
//        // 예시로는 나무에서 흙, 흙에서 돌로 업그레이드 가능하도록 구현
//        List<RoomType> upgradeOptions = new ArrayList<>();
//        if (hasWoodRooms()) {
//            upgradeOptions.add(RoomType.CLAY);
//        }
//        if (hasClayRooms()) {
//            upgradeOptions.add(RoomType.STONE);
//        }
//        return upgradeOptions.isEmpty() ? null : upgradeOptions.get(0);
//    }
//
//    private boolean hasWoodRooms() {
//        for (Tile[] row : playerBoard.getTiles()) {
//            for (Tile tile : row) {
//                if (tile instanceof Room && ((Room) tile).getType() == RoomType.WOOD) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean hasClayRooms() {
//        for (Tile[] row : playerBoard.getTiles()) {
//            for (Tile tile : row) {
//                if (tile instanceof Room && ((Room) tile).getType() == RoomType.CLAY) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public void selectFenceTile(int x, int y) {
//        List<int[]> selectedPositions = new ArrayList<>();
//        selectedPositions.add(new int[]{x, y});
//        int requiredWood = playerBoard.calculateRequiredWoodForFences(selectedPositions);
//        System.out.println("Selected position: (" + x + ", " + y + "), Wood needed: " + requiredWood);
//        Map<String, Integer> cost = new HashMap<>();
//        cost.put("wood", requiredWood);
//        if (checkResources(cost)) {
//            payResources(cost);
//            playerBoard.buildFences(selectedPositions, this);
//            System.out.println("Fence built at: (" + x + ", " + y + ")");
//        } else {
//            System.out.println("Not enough resources to build fence at: (" + x + ", " + y + ")");
//        }
//    }
//
//    public boolean canContinueFenceBuilding() {
//        // TODO: 실제 게임 로직에 따라 플레이어가 울타리 건설을 계속할지 선택하게 함
//        // 예시로는 랜덤 선택
//        return new Random().nextBoolean();
//    }
//
//    public void setGameController(GameController gameController) {
//        this.gameController = gameController;
//    }
//
//    public void resetResources() {
//        for (String resource : resources.keySet()) {
//            resources.put(resource, 0);
//        }
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void printPlayerResources(String message) {
//        System.out.println(message);
//        for (Map.Entry<String, Integer> resource : getResources().entrySet()) {
//            System.out.println("  " + resource.getKey() + ": " + resource.getValue());
//        }
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Player player = (Player) o;
//        return Objects.equals(id, player.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//
//    public boolean chooseOption() {
//        return chooseOption;
//    }
//
//    public void setChooseOption(boolean chooseOption) {
//        this.chooseOption = chooseOption;
//    }
//
//}

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
    private List<Effect> activeEffects;
    private boolean chooseOption;

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
        this.activeEffects = new ArrayList<>();
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

    public List<Effect> getActiveEffects() {
        return activeEffects;
    }

    public void addActiveEffect(Effect effect) {
        activeEffects.add(effect);
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
//        // 카드에 올려놓을 수 있는지(점유가 됐는지 확인)
//        if (!gameController.getMainBoard().canPlaceFamilyMember(card)) {
//            System.out.println("Card " + card.getName() + " is already occupied.");
//            return;
//        }
//
//        // 이 부분이 카드 효과를 발동
//        // TODO: 프론트가 가족 구성원의 좌표, 카드명주면
//        // 가족 구성원 사용됨, 카드 점유, 카드 실행
//
//        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
//        for (int i = 0; i < familyMembers.length; i++) {
//            for (int j = 0; j < familyMembers[i].length; j++) {
//                if (familyMembers[i][j] != null && familyMembers[i][j].isAdult() && !familyMembers[i][j].isUsed()) {
//                    FamilyMember selectedMember = familyMembers[i][j];
//                    System.out.println("Placing family member at (" + i + ", " + j + ") for player " + this.id);
//
//                    // 카드가 사용되면 점유상태가 되어 있어야 함.
//                    gameController.getMainBoard().placeFamilyMember(card); // 점유 상태로 먼저 설정
//
//                    // 카드 효과를 발동
//                    card.execute(this); // 카드 실행 로직
//
//                    // 가족 구성원이 사용되었음 표시
//                    selectedMember.setUsed(true); //hasFamilyMember()
//
//                    System.out.println("Player " + this.id + " placed a family member on card: " + card.getName());
//                    System.out.println("Family member used status: " + selectedMember.isUsed());
//                    return;
//                }
//            }
//        }
//        System.out.println("No available family member found for player " + this.id);
//    }

    public void placeFamilyMember(ActionRoundCard card) {
        MainBoard mainBoard = gameController.getMainBoard();

        // 최신 카드 리스트에서 해당 카드를 찾음
        // 해당 액션, 라운드 카드에 데코레이션 기능이 적용된 카드가 있다면 null이 아님.
        ActionRoundCard latestCard = null;
        for (ActionRoundCard actionCard : mainBoard.getActionCards()) {
            if (actionCard.getId() == card.getId()) {
                latestCard = actionCard;
                break;
            }
        }

        if (latestCard == null) {
            for (ActionRoundCard roundCard : mainBoard.getRoundCards()) {
                if (roundCard.getId() == card.getId()) {
                    latestCard = roundCard;
                    break;
                }
            }
        }

        if (latestCard == null) {
            System.out.println("Card not found on the main board.");
            return;
        }

        if (!mainBoard.canPlaceFamilyMember(latestCard)) {
            System.out.println("Card " + latestCard.getName() + " is already occupied.");
            return;
        }

        // 플레이어 보드에서 랜덤한 가족 구성원을 데려옴.
        FamilyMember[][] familyMembers = playerBoard.getFamilyMembers();
        for (int i = 0; i < familyMembers.length; i++) {
            for (int j = 0; j < familyMembers[i].length; j++) {
                if (familyMembers[i][j] != null && familyMembers[i][j].isAdult() && !familyMembers[i][j].isUsed()) {
                    FamilyMember selectedMember = familyMembers[i][j];
                    System.out.println("Placing family member at (" + i + ", " + j + ") for player " + this.id);

                    mainBoard.placeFamilyMember(latestCard); // 점유 상태로 먼저 설정
                    latestCard.execute(this); // 카드 실행 로직
                    selectedMember.setUsed(true);

                    System.out.println("Player " + this.id + " placed a family member on card: " + latestCard.getName());
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

    public GameController getGameController() {
        return gameController;
    }

    public void moveToActiveCards(CommonCard card) {
        if (card instanceof MajorImprovementCard) {
            majorImprovementCards.remove(card);
        } else if (card instanceof UnifiedCard) {
            occupationCards.remove(card);
            minorImprovementCards.remove(card); // UnifiedCard는 occupationCards와 minorImprovementCards 둘 다에 있을 수 있습니다.
        }
        activeCards.add(card);
    }

    public List<ExchangeableCard> getExchangeableCards(ExchangeTiming timing) {
        List<ExchangeableCard> exchangeableCards = new ArrayList<>();
        for (CommonCard card : majorImprovementCards) {
            if (card instanceof ExchangeableCard) {
                ExchangeableCard exchangeableCard = (ExchangeableCard) card;
                if (exchangeableCard.canExchange(timing)) {
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
    Map<String, Integer> finalCost = getDiscountedCost(cost);
    for (Map.Entry<String, Integer> entry : finalCost.entrySet()) {
        addResource(entry.getKey(), -entry.getValue());
    }
}

    public void useUnifiedCard(CommonCard card) {
        if (card != null) {
            card.execute(this);
            moveToActiveCards(card);
        } else {
            System.out.println("UnifiedCard is null.");
        }
    }

    public BakingCard selectBakingCard(List<BakingCard> bakingCards) {
        Random random = new Random();
        return bakingCards.get(random.nextInt(bakingCards.size()));
    }

    public int selectGrainForBaking(int maxAmount) {
        Random random = new Random();
        return random.nextInt(maxAmount + 1);
    }

    public void buildHouse(int x, int y, RoomType type) {
        if (playerBoard.canBuildHouse(x, y, type, resources)) {
            Map<String, Integer> cost = getHouseResourceCost(type);
            if (checkResources(cost)) {
                payResources(cost);
                playerBoard.buildHouse(x, y, type);
            } else {
                System.out.println("자원이 부족합니다.");
            }
        } else {
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

    public boolean addFamilyMember() {
        List<int[]> emptyRooms = playerBoard.getEmptyRoomPositions();
        if (!emptyRooms.isEmpty()) {
            FamilyMember newMember = new FamilyMember(-1, -1, false);
            newFamilyMembers.add(newMember);
            System.out.println("새로운 가족 구성원이 추가되었습니다. 빈 방에 배치하세요.");
            return true;
        } else {
            System.out.println("빈 방이 없습니다.");
            return false;
        }
    }

    public void placeFamilyMemberInRoom(FamilyMember familyMember, int x, int y) {
        if (playerBoard.isEmptyRoom(x, y)) {
            playerBoard.addFamilyMemberToBoard(familyMember, x, y);
            newFamilyMembers.remove(familyMember);
            System.out.println("가족 구성원이 방에 배치되었습니다: (" + x + ", " + y + ")");
        } else {
            System.out.println("해당 방은 이미 사용 중입니다.");
        }
    }

    public FamilyMember getNewFamilyMember() {
        if (!newFamilyMembers.isEmpty()) {
            return newFamilyMembers.get(0);
        }
        return null;
    }

    public boolean addAnimal(Animal animal) {
        newAnimals.add(animal);
        System.out.println(animal.getType() + " 새끼 동물이 추가되었습니다. 울타리나 방에 배치하세요.");
        return true;
    }

    public boolean addNewAnimal(Animal animal) {
        newAnimals.add(animal);
        System.out.println(animal.getType() + " 새끼 동물이 추가되었습니다. 빈 공간에 배치하세요.");
        return true;
    }

    public void placeAnimalOnBoard(Animal animal, int x, int y) {
        if (playerBoard.canPlaceAnimal(x, y, animal.getType())) {
            playerBoard.addAnimalToBoard(animal, x, y);
            newAnimals.remove(animal);
        } else {
            System.out.println("해당 위치에는 동물을 배치할 수 없습니다.");
        }
    }

//    public int placeNewAnimals() {
//        int placedCount = 0;
//        List<Animal> animalsToRemove = new ArrayList<>();
//        Iterator<Animal> iterator = newAnimals.iterator();
//
//        // TODO 임의로 좌표를 설정한 것
//        // 더 배치할 수 없을 경우: 방생
//        while (iterator.hasNext()) {
//            Animal animal = iterator.next();
//            Set<int[]> validPositions = playerBoard.getValidAnimalPositions(animal.getType());
//
//            if (!validPositions.isEmpty()) {
//                int[] position = validPositions.iterator().next();
//                System.out.println("동물 배치 위치: (" + position[0] + ", " + position[1] + ")");
//
//                // TODO 프론트한테 좌표 받아서 배치
//                placeAnimalOnBoard(animal, position[0], position[1]);
//                placedCount++;
//                animalsToRemove.add(animal);
//            } else {
//                System.out.println(animal.getType() + " 방생됨.");
//                animalsToRemove.add(animal);
//            }
//        }
//
//        newAnimals.removeAll(animalsToRemove);
//        return placedCount;
//    }
public int placeNewAnimals() {
    int placedCount = 0;
    List<Animal> animalsToRemove = new ArrayList<>();
    List<Animal> newAnimalsCopy = new ArrayList<>(newAnimals); // Create a copy to iterate over

    // TODO 임의로 좌표를 설정한 것
    // 더 배치할 수 없을 경우: 방생
    for (Animal animal : newAnimalsCopy) {
        Set<int[]> validPositions = playerBoard.getValidAnimalPositions(animal.getType());

        if (!validPositions.isEmpty()) {
            int[] position = validPositions.iterator().next();
            System.out.println("동물 배치 위치: (" + position[0] + ", " + position[1] + ")");

            // TODO 프론트한테 좌표 받아서 배치
            placeAnimalOnBoard(animal, position[0], position[1]);
            placedCount++;
            animalsToRemove.add(animal);

            // 유효한 위치 리스트를 갱신
            validPositions = playerBoard.getValidAnimalPositions(animal.getType());
        } else {
            System.out.println(animal.getType() + " 방생됨.");
            animalsToRemove.add(animal);
        }
    }

    newAnimals.removeAll(animalsToRemove);
    return placedCount;
}


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

    public int calculateTotalAnimalCapacity() {
        return playerBoard.getAnimalCapacity();
    }

    public void renovateHouse(RoomType newType) {
        playerBoard.renovateRooms(newType, this);
    }

    public RoomType chooseRoomTypeForRenovation() {
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

    public void selectFenceTile(int x, int y) {
        List<int[]> selectedPositions = new ArrayList<>();
        selectedPositions.add(new int[]{x, y});
        int requiredWood = playerBoard.calculateRequiredWoodForFences(selectedPositions);
        System.out.println("Selected position: (" + x + ", " + y + "), Wood needed: " + requiredWood);
        Map<String, Integer> cost = new HashMap<>();
        cost.put("wood", requiredWood);
        if (checkResources(cost)) {
            payResources(cost);
            playerBoard.buildFences(selectedPositions, this);
            System.out.println("Fence built at: (" + x + ", " + y + ")");
        } else {
            System.out.println("Not enough resources to build fence at: (" + x + ", " + y + ")");
        }
    }

    public boolean canContinueFenceBuilding() {
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

    public void printPlayerResources(String message) {
        System.out.println(message);
        for (Map.Entry<String, Integer> resource : getResources().entrySet()) {
            System.out.println("  " + resource.getKey() + ": " + resource.getValue());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean chooseOption() {
        return chooseOption;
    }

    public void setChooseOption(boolean chooseOption) {
        this.chooseOption = chooseOption;
    }

    private boolean woodDiscountActive = false;
    public boolean isWoodDiscountActive() {
        return woodDiscountActive;
    }

    public void setWoodDiscountActive(boolean woodDiscountActive) {
        this.woodDiscountActive = woodDiscountActive;
    }

    public Map<String, Integer> getDiscountedCost(Map<String, Integer> originalCost) {
        if (!woodDiscountActive && stoneDiscount == 0) return originalCost;

        Map<String, Integer> discountedCost = new HashMap<>(originalCost);
        if (discountedCost.containsKey("wood")) {
            discountedCost.put("wood", Math.max(0, discountedCost.get("wood") - 1));
        }
        if (discountedCost.containsKey("stone")) {
            discountedCost.put("stone", Math.max(0, discountedCost.get("stone") - stoneDiscount));
        }
        return discountedCost;
    }

    private int stoneDiscount = 0;

    public void setStoneDiscount(int discount) {
        this.stoneDiscount = discount;
    }

    private boolean compressedSoilActive;

    // 기존 코드 생략

    public boolean isCompressedSoilActive() {
        return compressedSoilActive;
    }

    public void setCompressedSoilActive(boolean compressedSoilActive) {
        this.compressedSoilActive = compressedSoilActive;
    }

    //TODO
    public boolean chooseResource(String resource1, String resource2, int amount) {
        // 실제 게임에서는 사용자 입력을 받아 선택하지만, 여기서는 단순히 흙을 선택하도록 가정합니다.
        // 나중에 실제 사용자 입력 로직으로 대체할 수 있습니다.
        return getResource(resource2) >= amount;
    }

}

