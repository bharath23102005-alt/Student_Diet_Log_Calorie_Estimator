import java.io.*;
import java.time.*;
import java.util.*;

public class Main {

    static void clearScreen() {
        for (int i = 0; i < 40; i++) System.out.println();
    }

    static void showTitle() {
        clearScreen();
        System.out.println("=========================================");
        System.out.println("        CALORIE ESTIMATOR SYSTEM");
        System.out.println("=========================================\n");
    }

    static void showFoodList() {
        System.out.println("\nType food names (case-insensitive, spaces ignored). Example list:");
        System.out.println("rice chapati idli dosa upma biryani pulao pizza burger frenchfries friedchicken onionrings popcorn");
        System.out.println("cake doughnut brownie cheeseballs ladoo murukku namkeen cornflakes muesli biscuit potatochips potatowedge");
        System.out.println("egg chicken fish beef pork mutton paneer tofu dal sambar chutney");
        System.out.println("banana apple orange grapes pineapple mango avocado almond cashew walnut seeds");
        System.out.println("milk coffee tea juice soda beer wine whiskey vodka rum gin");
        System.out.println("chocolateicecream vanillaicecream strawberryicecream brownieicecream fruitcake halwa gulabjamun rasgulla kulfi\n");
    }

    static void enterOneDay(String username, Scanner sc) throws IOException {
        String date = LocalDate.now().toString();
        String file = username + ".csv";

        class Item { String name; float grams, cal; }
        List<Item> items = new ArrayList<>();
        float total = 0;

        System.out.println("Enter entries for date " + date + " (type 'done' to finish, 'list' to view foods)");
        while (true) {
            System.out.print("Food: ");
            String food = sc.next();
            if (food.equals("done")) break;
            if (food.equals("list")) { showFoodList(); continue; }

            System.out.print("Quantity in grams: ");
            float grams;
            try { grams = sc.nextFloat(); }
            catch (Exception e) { sc.nextLine(); System.out.println("Invalid qty; skipped."); continue; }

            float cal = Food.getCalories(food, grams);
            if (cal < 0) {
                System.out.println("Food not found. Type 'list'.");
                continue;
            }
            Item it = new Item();
            it.name = Food.normalize(food);
            it.grams = grams;
            it.cal = cal;
            items.add(it);
            total += cal;
            System.out.printf("Added: %s (%.0f g) -> %.2f kcal%n", it.name, grams, cal);
        }

        if (items.isEmpty()) {
            System.out.println("No valid items. Nothing saved.");
            return;
        }

        System.out.println("\nSummary for " + date);
        System.out.printf("%-20s %8s %12s%n", "Food", "Grams", "Calories");
        System.out.println("------------------------------------------------");
        for (Item i : items)
            System.out.printf("%-20s %8.0f %12.2f%n", i.name, i.grams, i.cal);
        System.out.println("------------------------------------------------");
        System.out.printf("%-20s %8s %12.2f%n", "Total", "", total);

        System.out.print("\nSave this day's entry? (y/n): ");
        char ans = sc.next().charAt(0);
        if (ans == 'y' || ans == 'Y') {
            try (FileWriter fw = new FileWriter(file, true)) {
                fw.write(date + "," + String.format("%.2f", total) + "\n");
                for (Item i : items)
                    fw.write(i.name + "," + (int)i.grams + "," + String.format("%.2f", i.cal) + "\n");
            }
            System.out.println("Saved " + date + " -> " + total + " kcal");
            CSVUtils.computeWeekTotalAndAppendIfNeeded(username);
        } else {
            System.out.println("Entry discarded.");
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            showTitle();
            System.out.println("1. Register\n2. Login\n3. Exit\nChoice: ");
            int ch;
            try { ch = sc.nextInt(); } catch (Exception e) { sc.nextLine(); continue; }

            if (ch == 1) {
                CSVUtils.registerUser(sc);
            } else if (ch == 2) {
                StringBuilder user = new StringBuilder();
                if (!CSVUtils.loginUser(sc, user)) continue;

                boolean logged = true;
                while (logged) {
                    showTitle();
                    System.out.println("Logged in as: " + user);
                    System.out.println("1. Enter today's food & calculate calories");
                    System.out.println("2. View my previous data");
                    System.out.println("3. View another user's data");
                    System.out.println("4. Logout");
                    System.out.println("5. Exit program");
                    System.out.print("Choice: ");
                    int op;
                    try { op = sc.nextInt(); } catch (Exception e) { sc.nextLine(); continue; }

                    if (op == 1) enterOneDay(user.toString(), sc);
                    else if (op == 2) CSVUtils.printUserData(user.toString());
                    else if (op == 3) {
                        System.out.print("Enter username to view: ");
                        CSVUtils.printUserData(sc.next());
                    }
                    else if (op == 4) logged = false;
                    else if (op == 5) System.exit(0);
                }
            } else if (ch == 3) {
                System.out.println("Goodbye!");
                break;
            }
        }
    }
}
