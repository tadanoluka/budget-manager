package budget;

import java.util.*;


public class PurchaseList {
    private Map<PurchaseType, Set<Purchase>> purchaseMap;

    private Map<PurchaseType, Double> totalCostMap;

    public PurchaseList() {
        purchaseMap = new HashMap<>();
        totalCostMap = new HashMap<>();
        initPurchaseList();
    }

    private void initPurchaseList() {
        for (PurchaseType purchaseType : PurchaseType.values()) {
            if (purchaseType != PurchaseType.NO_TYPE) {
                purchaseMap.put(purchaseType, new LinkedHashSet<>());
                totalCostMap.put(purchaseType, 0.00);
            }
        }
    }

    public boolean add(String purchaseName, Double purchaseCost, PurchaseType purchaseType) {
        // Checking for valid purchase type
        if (purchaseType == PurchaseType.ALL || purchaseType == PurchaseType.NO_TYPE) {
            return false;
        }
        // Update values at target Purchase Type
        Set<Purchase> purchases = purchaseMap.get(purchaseType);
        double currentTotalTypeCost = totalCostMap.get(purchaseType);
        double newTotalTypeCost = currentTotalTypeCost + purchaseCost;
        Purchase purchase = new Purchase(purchaseName, purchaseCost);
        purchases.add(purchase);
        purchaseMap.put(purchaseType, purchases);
        totalCostMap.put(purchaseType, newTotalTypeCost);
        // Update values at PurchaseType.ALL
        Set<Purchase> allPurchases = purchaseMap.get(PurchaseType.ALL);
        allPurchases.add(purchase);
        purchaseMap.put(PurchaseType.ALL, allPurchases);
        double currentTotalForAllType = totalCostMap.get(PurchaseType.ALL);
        double newTotalForAllType = currentTotalForAllType + purchaseCost;
        totalCostMap.put(PurchaseType.ALL, newTotalForAllType);
        return true;
    }

    public String getPurchaseListByType(PurchaseType purchaseType) {
        StringBuilder stringBuilder = new StringBuilder(purchaseType.toString() + ":\n");
        if (purchaseType == PurchaseType.ALL) {
            Set<Purchase> purchases = purchaseMap.get(PurchaseType.ALL);
                for (Purchase purchase : purchases) {
                    stringBuilder.append(purchase).append("\n");
                }
            double totalSum = totalCostMap.get(PurchaseType.ALL);
            stringBuilder.append("Total sum: $%.2f".formatted(totalSum));
            return stringBuilder.toString();
        } else if (purchaseMap.get(purchaseType) != null) {
            for (Purchase purchase : purchaseMap.get(purchaseType)) {
                stringBuilder.append(purchase).append("\n");
            }
            stringBuilder.append("Total sum: $%.2f".formatted(totalCostMap.get(purchaseType)));
            return stringBuilder.toString();
        }
        return stringBuilder + "The purchase list is empty!";
    }

    public Map<PurchaseType, Set<Purchase>> getPurchaseMap() {
        return purchaseMap;
    }

    public Map<PurchaseType, Double> getTotalCostMap() {
        return totalCostMap;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<PurchaseType, Set<Purchase>> entry : purchaseMap.entrySet()) {
            int purchaseIndex = entry.getKey().getIndex();
            for (Purchase purchase : entry.getValue()) {
                stringBuilder.append(purchase.getName()).append("_");
                stringBuilder.append(purchase.getCost()).append("_");
                stringBuilder.append(purchaseIndex).append("\n");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("\n"));
        return stringBuilder.toString();
    }
}
