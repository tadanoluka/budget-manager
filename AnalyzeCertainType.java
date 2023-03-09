package budget;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class AnalyzeCertainType implements AnalysisStrategy{
    PurchaseType targetPurchaseType;

    public AnalyzeCertainType(PurchaseType purchaseType) {
        targetPurchaseType = purchaseType;
    }

    @Override
    public String getAnalysis(PurchaseList purchaseList) {
        Set<Purchase> purchases = new TreeSet<>(Collections.reverseOrder());
        purchases.addAll(purchaseList.getPurchaseMap().get(targetPurchaseType));
        double totalCost = purchaseList.getTotalCostMap().get(targetPurchaseType);
        if (totalCost == 0.00) {
            return "\nThe purchase list is empty!";
        }
        StringBuilder stringBuilder = new StringBuilder("\n" + targetPurchaseType + ":\n");
        for (Purchase purchase : purchases) {
            stringBuilder.append(purchase).append("\n");
        }
        stringBuilder.append("Total sum: $%.2f".formatted(totalCost));
        return stringBuilder.toString();
    }
}
