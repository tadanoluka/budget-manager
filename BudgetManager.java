package budget;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BudgetManager {
    final private String MENU = """
            Choose your action:
            1) Add income
            2) Add purchase
            3) Show list of purchases
            4) Balance
            5) Save
            6) Load
            7) Analyze (Sort)
            0) Exit
            """.trim();
    final private String ANALYZE_SUBMENU = """
            How do you want to sort?
            1) Sort all purchases
            2) Sort by type
            3) Sort certain type
            4) Back
            """.trim();
    final private String CHOOSE_PURCHASE_TYPE_FOR_ADD_SUBMENU = """
            Choose the type of purchase
            1) Food
            2) Clothes
            3) Entertainment
            4) Other
            5) Back
            """.trim();
    final private String CHOOSE_PURCHASE_TYPE_FOR_SHOW_SUBMENU = """
            Choose the type of purchases
            1) Food
            2) Clothes
            3) Entertainment
            4) Other
            5) All
            6) Back
            """.trim();
    private Scanner scanner;
    private Balance balance;
    private PurchaseList purchaseList;

    private boolean isRunning;
    private boolean isBalanceChanged;
    private boolean isPurchaseAdd;

    public BudgetManager() {
        onCreate();
    }

    private void onCreate() {
        isRunning = false;
        scanner = new Scanner(System.in);
        balance = new Balance();
        purchaseList = new PurchaseList();
        isBalanceChanged = false;
        isPurchaseAdd = false;
    }

    public void launch() {
        isRunning = true;
        while (isRunning){
            navigationMenu();
        }
    }

    private void navigationMenu(){
        displayMenu(MENU);
        switch (getUserInput()) {
            case "1" -> addIncomeMenu();
            case "2" -> addPurchaseMenu();
            case "3" -> showPurchaseListMenu();
            case "4" -> showBalance();
            case "5" -> saveToFile();
            case "6" -> loadFromFile();
            case "7" -> analyzeMenu();
            case "0" -> onExit();
        }
    }

    private void displayMenu(String menuString) {
        System.out.println("\n" + menuString);
    }

    private String getUserInput() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        throw new RuntimeException("Couldn't get user input");
    }

    private void addIncomeMenu() {
        while (incomeNotAdded()) {
            addIncome();
        }
        isBalanceChanged = false;
    }

    private boolean incomeNotAdded() {
        return !isBalanceChanged;
    }

    private void addIncome() {
        System.out.println("\nEnter income:");
        isBalanceChanged = balance.add(getUserInput());
        System.out.println(isBalanceChanged ? "Income was added!" : "Invalid income");
    }

    private void addPurchaseMenu() {
        boolean isChoosingPurchaseType = true;
        while (isChoosingPurchaseType) {
            displayMenu(CHOOSE_PURCHASE_TYPE_FOR_ADD_SUBMENU);
            PurchaseType typeInput = getTypeOfPurchaseForAdd();
            if (typeInput != PurchaseType.NO_TYPE && typeInput != PurchaseType.ALL) {
                while (purchaseNotAdded()) {
                    addPurchase(typeInput);
                }
                isPurchaseAdd = false;
            } else {
                isChoosingPurchaseType = false;
            }
        }
    }

    private PurchaseType getTypeOfPurchaseForAdd() {
        int typeInput = Integer.parseInt(getUserInput());
        PurchaseType[] purchaseTypes = PurchaseType.values();
        for (PurchaseType purchaseType : purchaseTypes) {
            if (purchaseType.getIndex() == typeInput) {
                return purchaseType;
            }
        }
        return PurchaseType.NO_TYPE;
    }

    private boolean purchaseNotAdded() {
        return !isPurchaseAdd;
    }

    private void addPurchase(PurchaseType purchaseType) {
        System.out.println("\nEnter purchase name:");
        String purchaseName = getUserInput();
        System.out.println("Enter its price:");
        double purchasePrice = Double.parseDouble(getUserInput());
        isPurchaseAdd = purchaseList.add(purchaseName, purchasePrice, purchaseType);
        System.out.println(isPurchaseAdd ? "Purchase was added!" : "Invalid purchase");
        if (isPurchaseAdd) {
            boolean balanceWasSpend = balance.spend(purchasePrice);
        }
    }

    private void showPurchaseListMenu() {
        boolean isChoosingPurchaseType = true;
        while (isChoosingPurchaseType) {
            displayMenu(CHOOSE_PURCHASE_TYPE_FOR_SHOW_SUBMENU);
            PurchaseType typeInput = getTypeOfPurchaseForAdd();
            if (typeInput != PurchaseType.NO_TYPE) {
                System.out.println("\n" + purchaseList.getPurchaseListByType(typeInput));
            } else {
                isChoosingPurchaseType = false;
            }
        }
    }

    private void showBalance() {
        System.out.println("\n" + balance);
    }

    private void saveToFile() {
        File file = new File("purchases.txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(balance + "\n");
            writer.write(String.valueOf(purchaseList));
            System.out.println("\nPurchases were saved!");
        } catch (IOException e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File("purchases.txt");
        Map<Integer,PurchaseType> typeMap = new HashMap<>();
        for (PurchaseType type : PurchaseType.values()) {
            typeMap.put(type.getIndex(), type);
        }
        try (Scanner fileScanner = new Scanner(file)) {
            fileScanner.nextLine();
            boolean balanceWasLoaded = balance.load();
            while (fileScanner.hasNextLine()) {
                String[] purchase = fileScanner.nextLine().split("_");
                String purchaseName = purchase[0];
                double purchaseCost = Double.parseDouble(purchase[1]);
                PurchaseType purchaseType = typeMap.get(Integer.parseInt(purchase[2]));
                purchaseList.add(purchaseName, purchaseCost, purchaseType);
            }
            System.out.println("\nPurchases were loaded!");
        } catch (IOException e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    private void analyzeMenu() {
        boolean isChoosingAnalyzeType = true;
        while (isChoosingAnalyzeType) {
            displayMenu(ANALYZE_SUBMENU);
            AnalysisStrategy analysisStrategy = setupAnalyzeStrategy();
            if (analysisStrategy != null) {
                String outputString = analysisStrategy.getAnalysis(purchaseList);
                System.out.println(outputString);
            } else {
                isChoosingAnalyzeType = false;
            }
        }
    }

    private AnalysisStrategy setupAnalyzeStrategy() {
        String analyzeTypeInput = getUserInput();
        AnalysisStrategy analysisStrategy = null;
        switch (analyzeTypeInput) {
            case "1" -> analysisStrategy = new AnalyzeAll();
            case "2" ->  analysisStrategy = new AnalyzeByType();
            case "3" -> {
                PurchaseType purchaseType = setupPurchaseTypeForAnalyze();
                analysisStrategy = new AnalyzeCertainType(purchaseType);
            }
        }
        return analysisStrategy;
    }

    private PurchaseType setupPurchaseTypeForAnalyze() {
        System.out.println();
        System.out.println("""
            Choose the type of purchase
            1) Food
            2) Clothes
            3) Entertainment
            4) Other
            """.trim());
        int typeIndex = Integer.parseInt(getUserInput());
        for (PurchaseType purchaseType : PurchaseType.values()) {
            if (purchaseType.getIndex() == typeIndex) {
                return purchaseType;
            }
        }
        return null;
    }

    private void onExit() {
        isRunning = false;
        System.out.println("\nBye!");
    }
}
