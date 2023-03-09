package budget;

import java.util.*;


public class AnalyzeByType implements AnalysisStrategy{

    @Override
    public String getAnalysis(PurchaseList purchaseList) {
        Map<PurchaseType, Double> totalCostMap = new HashMap<>(purchaseList.getTotalCostMap());
        List<String> outputList = new ArrayList<>();
        PurchaseType highestTotalCostAtType = null;
        for (int i = 0; i < 4; i++){
            for (var entry : totalCostMap.entrySet()) {
                if (entry.getKey() != PurchaseType.ALL) {
                    if (highestTotalCostAtType == null) {
                        highestTotalCostAtType = entry.getKey();
                    } else if (totalCostMap.get(highestTotalCostAtType) < totalCostMap.get(entry.getKey())) {
                        highestTotalCostAtType = entry.getKey();
                    }
                }
            }
            double total = totalCostMap.get(highestTotalCostAtType);
            outputList.add(highestTotalCostAtType + " - $%.2f".formatted(total));
            totalCostMap.remove(highestTotalCostAtType);
            highestTotalCostAtType = null;
        }
        StringBuilder stringBuilder = new StringBuilder("\nTypes:\n");
        for (var string : outputList) {
            stringBuilder.append(string).append("\n");
        }
        double totalCost = purchaseList.getTotalCostMap().get(PurchaseType.ALL);
        stringBuilder.append("Total sum: $%.2f".formatted(totalCost));
        return stringBuilder.toString();
    }
}
