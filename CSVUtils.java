import java.io.*;
import java.time.*;
import java.util.*;

public class CSVUtils {

    // ---------------- REGISTER USER ----------------
    public static void registerUser(Scanner sc) throws IOException {
        File f = new File("login.csv");
        f.createNewFile();

        System.out.print("Enter new username: ");
        String username = sc.next();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 2 && p[0].equals(username)) {
                    System.out.println("Username exists.");
                    return;
                }
            }
        }

        System.out.print("Enter password: ");
        String password = sc.next();

        try (FileWriter fw = new FileWriter(f, true)) {
            fw.write(username + "," + password + "\n");
        }
        System.out.println("Registered successfully.");
    }

    // ---------------- LOGIN ----------------
    public static boolean loginUser(Scanner sc, StringBuilder outUser) throws IOException {
        File f = new File("login.csv");
        if (!f.exists()) {
            System.out.println("No users. Register first.");
            return false;
        }

        while (true) {
            System.out.print("Enter username: ");
            String user = sc.next();

            String stored = null;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split(",");
                    if (p.length >= 2 && p[0].equals(user)) {
                        stored = p[1];
                        break;
                    }
                }
            }

            if (stored == null) {
                System.out.println("Username not found. Try again.");
                continue;
            }

            while (true) {
                System.out.print("Enter password: ");
                String pass = sc.next();
                if (pass.equals(stored)) {
                    outUser.append(user);
                    return true;
                }
                System.out.println("Incorrect password. Try again.");
            }
        }
    }

    // ---------------- PRINT DATA ----------------
    public static void printUserData(String username) throws IOException {
        File f = new File(username + ".csv");
        if (!f.exists()) {
            System.out.println("No data for " + username);
            return;
        }
        System.out.println("---- Data for " + username + " ----");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null)
                System.out.println(line);
        }
        System.out.println("---- end ----");
    }

    // ---------------- WEEK TOTAL ----------------
    public static void computeWeekTotalAndAppendIfNeeded(String username) throws IOException {
        File f = new File(username + ".csv");
        if (!f.exists()) return;

        List<Double> daily = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 2 && !p[0].startsWith("Week") && !p[0].startsWith("Month")) {
                    daily.add(Double.parseDouble(p[1]));
                }
            }
        }

        if (daily.size() >= 7) {
            double sum = 0;
            for (int i = daily.size() - 7; i < daily.size(); i++)
                sum += daily.get(i);

            try (FileWriter fw = new FileWriter(f, true)) {
                fw.write("Week Total," + String.format("%.2f", sum) + "\n");
            }
            System.out.println("Weekly total appended: " + sum);
        }
    }

    // ---------------- MONTH TOTAL ----------------
    public static void computeMonthTotalAndAppendIfNeeded(String username) throws IOException {

        File f = new File(username + ".csv");
        if (!f.exists()) return;

        String currentMonth = YearMonth.now().toString();   // 2026-01

        double sum = 0;
        boolean alreadyAdded = false;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (line.startsWith("Month Total (" + currentMonth)) {
                    alreadyAdded = true;
                    break;
                }

                String[] p = line.split(",");
                if (p.length == 2 && p[0].matches("\\d{4}-\\d{2}-\\d{2}")) {
                    if (p[0].startsWith(currentMonth)) {
                        sum += Double.parseDouble(p[1]);
                    }
                }
            }
        }

        if (!alreadyAdded && sum > 0) {
            try (FileWriter fw = new FileWriter(f, true)) {
                fw.write("Month Total (" + currentMonth + ")," +
                        String.format("%.2f", sum) + "\n");
            }
            System.out.println("Monthly total appended: " + sum);
        }
    }
}

