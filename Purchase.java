package budget;

public class Purchase implements Comparable<Purchase>{

    static private int counter = 0;

    private String productInformation;
    private double cost;
    private int index;

    public Purchase(String productInformation, double productCost) {
        this.index = counter;
        counter++;
        this.productInformation = productInformation;
        this.cost = productCost;
    }

    public Double getCost() {
        return cost;
    }

    public String getName() {
        return productInformation;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "%s $%.2f".formatted(productInformation, cost);
    }

    @Override
    public int compareTo(Purchase otherPurchase) {
        int tempVal = getCost().compareTo(otherPurchase.getCost());
        if (tempVal == 0) {
            tempVal = getIndex().compareTo(otherPurchase.getIndex()) * -1;
        }
        return tempVal;
    }
}
