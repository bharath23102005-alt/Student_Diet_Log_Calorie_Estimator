import java.io.*;
import java.util.*;

public class CSVUtils {

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

    public static void printUserData(String username) throws IOException {
        File f = new File(username + ".csv");
        if (!f.exists()) {
            System.out.println("No data for " + username);
            return;
        }
        System.out.println("---- Data for " + username + " ----");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) System.out.println(line);
        }
        System.out.println("---- end ----");
    }

    public static void computeWeekTotalAndAppendIfNeeded(String username) throws IOException {
        File f = new File(username + ".csv");
        if (!f.exists()) return;

        LinkedHashMap<String, Double> dayTotals = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 2) dayTotals.put(p[0], Double.parseDouble(p[1]));
            }
        }

        if (dayTotals.size() >= 7) {
            List<Double> v = new ArrayList<>(dayTotals.values());
            double sum = 0;
            for (int i = v.size() - 7; i < v.size(); i++) sum += v.get(i);

            try (FileWriter fw = new FileWriter(f, true)) {
                fw.write("Week Total," + String.format("%.2f", sum) + "\n");
            }
            System.out.println("Weekly total appended: " + sum);
        }
    }
}
