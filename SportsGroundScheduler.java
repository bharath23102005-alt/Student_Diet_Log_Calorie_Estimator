import java.util.*;

class User {
    String username;
    String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Booking {
    String team;
    String sport;
    String date;
    String slot;

    Booking(String team, String sport, String date, String slot) {
        this.team = team;
        this.sport = sport;
        this.date = date;
        this.slot = slot;
    }
}

class Ground {
    String name;

    // Date -> (Slot -> Booking)
    Map<String, Map<String, Booking>> bookings = new HashMap<>();

    Ground(String name) {
        this.name = name;
    }

    boolean isAvailable(String date, String slot) {
        return !bookings.containsKey(date) ||
               !bookings.get(date).containsKey(slot);
    }

    void book(String date, String slot, Booking b) {
        bookings.putIfAbsent(date, new HashMap<>());
        bookings.get(date).put(slot, b);
    }

    void showSchedule(String date) {
        System.out.println("\nUsage Schedule for " + name + " on " + date);
        if (!bookings.containsKey(date)) {
            System.out.println("No bookings on this date.");
            return;
        }
        for (String s : bookings.get(date).keySet()) {
            Booking b = bookings.get(date).get(s);
            System.out.println(s + " -> Team: " + b.team + ", Sport: " + b.sport);
        }
    }
}

public class SportsGroundScheduler {

    static Scanner sc = new Scanner(System.in);

    static Map<String, User> users = new HashMap<>();

    // Permanent grounds (shared)
    static Ground rgm = new Ground("RGM");
    static Ground shanthiram = new Ground("Shanthiram");
    static Ground svr = new Ground("SVR");

    static String[] slots = {
        "06:00 - 07:00", "07:00 - 08:00", "08:00 - 09:00",
        "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
        "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00",
        "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00",
        "18:00 - 19:00", "19:00 - 20:00"
    };

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n======================================");
            System.out.println("   SPORTS GROUND USAGE SCHEDULER");
            System.out.println("======================================");

            if (!authenticateUser()) continue;

            Ground ground = selectGround();
            bookingProcess(ground);

            if (!postMenu()) break;
        }

        System.out.println("Application closed.");
    }

    // ---------- VALIDATION ----------
    static boolean validUsername(String u) {
        return u.matches("[a-zA-Z]+");
    }

    static boolean validPassword(String p) {
        if (p.length() < 8) return false;
        boolean up=false, lo=false, di=false, sp=false;

        for (char c : p.toCharArray()) {
            if (Character.isUpperCase(c)) up = true;
            else if (Character.isLowerCase(c)) lo = true;
            else if (Character.isDigit(c)) di = true;
            else if ("@#$%&*!".indexOf(c) != -1) sp = true;
        }
        return up && lo && di && sp;
    }

    static int safeInt() {
        while (!sc.hasNextInt()) {
            System.out.println("Please enter numbers only.");
            sc.nextLine();
        }
        int v = sc.nextInt();
        sc.nextLine();
        return v;
    }

    // ---------- AUTH ----------
    static boolean authenticateUser() {

        String ans;
        while (true) {
            System.out.print("\nAre you an existing user? (Yes/No): ");
            ans = sc.nextLine().toLowerCase();
            if (ans.matches("yes|y|no|n")) break;
            System.out.println("Enter only Yes / No / y / n");
        }

        if (ans.equals("no") || ans.equals("n")) {
            createUser();
        }

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Forgot Password");
            System.out.println("3. Delete User");
            System.out.println("4. Back to Menu");
            System.out.print("Choose option: ");
            int ch = safeInt();

            if (ch == 4) return false;
            if (ch == 2) { forgotPassword(); continue; }
            if (ch == 3) { deleteUser(); continue; }

            System.out.print("Username: ");
            String u = sc.nextLine();
            System.out.print("Password: ");
            String p = sc.nextLine();

            if (users.containsKey(u) && users.get(u).password.equals(p)) {
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Invalid credentials.");
            }
        }
    }

    static void createUser() {
        String u;
        do {
            System.out.print("Create Username (alphabets only): ");
            u = sc.nextLine();
        } while (!validUsername(u) || users.containsKey(u));

        String p;
        do {
            System.out.print("Create Password: ");
            p = sc.nextLine();
            if (!validPassword(p))
                System.out.println("Password must contain 8 chars, uppercase, lowercase, digit & special (@#$%&*!)");
        } while (!validPassword(p));

        users.put(u, new User(u, p));
        System.out.println("User created successfully!");
    }

    static void forgotPassword() {
        System.out.print("Enter username: ");
        String u = sc.nextLine();

        if (!users.containsKey(u)) {
            System.out.println("User not found.");
            return;
        }

        String p;
        do {
            System.out.print("Enter new password: ");
            p = sc.nextLine();
        } while (!validPassword(p));

        users.get(u).password = p;
        System.out.println("Password reset successful!");
    }

    static void deleteUser() {
        System.out.print("Enter username to delete: ");
        String u = sc.nextLine();

        if (!users.containsKey(u)) {
            System.out.println("User not found.");
            return;
        }

        users.remove(u);
        System.out.println("User deleted permanently.");
    }

    // ---------- GROUND ----------
    static Ground selectGround() {
        System.out.println("\n1. RGM");
        System.out.println("2. Shanthiram");
        System.out.println("3. SVR");
        System.out.print("Select ground: ");
        int c = safeInt();

        if (c == 1) return rgm;
        if (c == 2) return shanthiram;
        return svr;
    }

    // ---------- BOOKING (SINGLE SLOT ONLY) ----------
    static void bookingProcess(Ground g) {

        System.out.print("Enter Team Name: ");
        String team = sc.nextLine();

        System.out.print("Enter Sport Name: ");
        String sport = sc.nextLine();

        System.out.print("Enter Date (DD-MM-YYYY): ");
        String date = sc.nextLine();

        System.out.println("\nSlots for " + date + ":");
        for (int i = 0; i < slots.length; i++) {
            String s = slots[i];
            System.out.println((i + 1) + ". " + s +
                    (g.isAvailable(date, s) ? " (Available)" : " (Booked)"));
        }

        System.out.print("\nChoose slot number: ");
        int c = safeInt();

        if (c < 1 || c > slots.length) {
            System.out.println("Invalid slot number.");
            return;
        }

        String slot = slots[c - 1];

        if (!g.isAvailable(date, slot)) {
            System.out.println("Slot already booked!");
        } else {
            g.book(date, slot, new Booking(team, sport, date, slot));
            System.out.println("Slot booked successfully!");
        }

        g.showSchedule(date);
    }

    // ---------- POST ----------
    static boolean postMenu() {
        System.out.println("\n1. Back to Menu");
        System.out.println("2. Exit");
        System.out.print("Choose option: ");
        return safeInt() != 2;
    }
}