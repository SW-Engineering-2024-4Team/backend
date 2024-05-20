package enums;

public enum ExchangeTiming {
    ANYTIME, // 라운드 진행 단계와 수확 단계 모두 가능
    HARVEST, // 수확 단계에서만 가능
    BREAD_BAKING // 빵굽기 기능이 실행될 때 가능
}
