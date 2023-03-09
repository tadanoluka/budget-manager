package budget;

import java.util.*;

public class AnalyzeAll implements AnalysisStrategy{
    List<Purchase> purchases = new ArrayList<>();
    @Override
    public String getAnalysis(PurchaseList purchaseList) {
        double totalCost = purchaseList.getTotalCostMap().get(PurchaseType.ALL);
        if (totalCost == 0.00) {
            return "\nThe purchase list is empty!";
        }
        purchases.addAll(purchaseList.getPurchaseMap().get(PurchaseType.ALL));
        purchases.sort(Collections.reverseOrder());
        StringBuilder stringBuilder = new StringBuilder("\nAll:\n");
        for (Purchase purchase : purchases) {
            stringBuilder.append(purchase).append("\n");
        }
        stringBuilder.append("Total: $%.2f".formatted(totalCost));
        return stringBuilder.toString();
    }
}
