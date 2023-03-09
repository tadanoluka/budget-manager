package budget;

enum PurchaseType {
    FOOD(1),
    CLOTHES(2),
    ENTERTAINMENT(3),
    OTHER(4),
    ALL(5),
    NO_TYPE(-1);
    private final int INDEX;
    PurchaseType(int typeIndex) {
        this.INDEX = typeIndex;
    }

    public int getIndex() {
        return INDEX;
    }

    @Override
    public String toString() {
        String tempName = this.name().toLowerCase().replace('_', ' ');
        tempName = tempName.substring(0,1).toUpperCase() + tempName.substring(1);
        return tempName;
    }
}
