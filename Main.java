import java.io.*;
import java.time.*;
import java.util.*;

public class Main {

    static void enterOneDay(String username, Scanner sc) throws IOException {

        String date = LocalDate.now().toString();
        String file = username + ".csv";

        class Item {
            String name;
            float grams, cal;
        }

        List<Item> items = new ArrayList<>();
        float total = 0;

        System.out.println("Enter foods (done to stop)");

        while (true) {

            System.out.print("Food: ");
            String food = sc.next();
            if (food.equals("done")) break;

            System.out.print("Grams: ");
            float grams;

            try {
                grams = sc.nextFloat();
            } catch (Exception e) {
                sc.nextLine();
                continue;
            }

            float cal = Food.getCalories(food, grams);

            if (cal < 0) {
                System.out.println("Food not found.");
                continue;
            }

            Item it = new Item();
            it.name = Food.normalize(food);
            it.grams = grams;
            it.cal = cal;

            items.add(it);
            total += cal;

            System.out.println("Added -> " + cal);
        }

        if (items.isEmpty()) return;

        System.out.println("Total Today: " + total);

        System.out.print("Save? (y/n): ");
        char ch = sc.next().charAt(0);

        if (ch == 'y' || ch == 'Y') {

            try (FileWriter fw = new FileWriter(file, true)) {

                fw.write(date + "," +
                        String.format("%.2f", total) + "\n");

                for (Item i : items)
                    fw.write(i.name + "," + (int) i.grams + "," +
                            String.format("%.2f", i.cal) + "\n");
            }

            CSVUtils.computeWeekTotalAndAppendIfNeeded(username);
            CSVUtils.computeMonthTotalAndAppendIfNeeded(username);

            System.out.println("Saved.");
        }
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n1.Register\n2.Login\n3.Exit");
            int ch = sc.nextInt();

            if (ch == 1)
                CSVUtils.registerUser(sc);

            else if (ch == 2) {

                StringBuilder user = new StringBuilder();

                if (!CSVUtils.loginUser(sc, user))
                    continue;

                boolean logged = true;

                while (logged) {

                    System.out.println("\n1.Enter Food");
                    System.out.println("2.View Data");
                    System.out.println("3.Logout");

                    int op = sc.nextInt();

                    if (op == 1)
                        enterOneDay(user.toString(), sc);

                    else if (op == 2)
                        CSVUtils.printUserData(user.toString());

                    else if (op == 3)
                        logged = false;
                }
            }

            else if (ch == 3)
                break;
        }
    }
}

