package budget;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Balance {
    private double balance;

    public Balance() {
        this.balance = 0.00;
    }

    // Returns "false" if the operation went wrong and "true" if everything went right
    public boolean add(double incomeDouble) {
        if (incomeDouble > 0) {
            balance += incomeDouble;
            return true;
        } else {
            return false;
        }
    }

    // Returns "false" if the operation went wrong and "true" if everything went right
    public boolean add(String incomeString) {
        try {
            return this.add(Double.parseDouble(incomeString));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean spend(double expense) {
        if (expense > 0) {
            balance -= expense;
            return true;
        } else {
            return false;
        }
    }

    public boolean load() {
        File file = new File("purchases.txt");
        try (Scanner fileScanner = new Scanner(file)) {
            String[] balanceString = fileScanner.nextLine().split("\\$");
            this.balance = Double.parseDouble(balanceString[1]);
            return true;
        } catch (IOException e) {
            System.out.println("\n" + getClass() + " load()\n" + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return "Balance: $%.2f".formatted(balance);
    }
}
