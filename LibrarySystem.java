import java.io.*;
import java.util.*;
class Book{
    int id;
    String title;
    String author;
    boolean isIssued;

    // Constructor
    Book(int id, String title, String author, boolean isIssued) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = isIssued;
    }

    // Convert book data to one line for saving in file
    String toFileString() {
        return id + "," + title + "," + author + "," + isIssued;
    }

    // Create a Book object from one line read from file
    static Book fromFileString(String line) {
        String[] parts = line.split(",");
        return new Book(
            Integer.parseInt(parts[0]),
            parts[1],
            parts[2],
            Boolean.parseBoolean(parts[3])
        );
    }
}
public class LibrarySystem {
    static final String FILE_NAME = "books.txt";
    static ArrayList<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        loadData();  // Load data from file when program starts
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addBook(sc);
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    issueBook(sc);
                    break;
                case 4:
                    returnBook(sc);
                    break;
                case 5:
                    saveData();
                    System.out.println("Data saved. Exiting program...");
                    return;
                default:
                    System.out.println("Invalid choice! Please enter 1–5.");
            }
        }
    }

    // Add new book
    static void addBook(Scanner sc) {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Book Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Author Name: ");
        String author = sc.nextLine();

        books.add(new Book(id, title, author, false));
        saveData();
        System.out.println("Book added successfully!");
    }

    // Display all books
    static void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available!");
            return;
        }
        System.out.println("\n--- Book List ---");
        for (Book b : books) {
            String status = b.isIssued ? "Issued" : "Available";
            System.out.println(b.id + " | " + b.title + " | " + b.author + " | Status: " + status);
        }
    }

    // Issue a book
    static void issueBook(Scanner sc) {
        System.out.print("Enter Book ID to issue: ");
        int id = sc.nextInt();
        boolean found = false;

        for (Book b : books) {
            if (b.id == id) {
                found = true;
                if (!b.isIssued) {
                    b.isIssued = true;
                    System.out.println("✅ Book issued successfully!");
                } else {
                    System.out.println("⚠ Book already issued!");
                }
                break;
            }
        }

        if (!found) System.out.println("❌ Book not found!");
        saveData();
    }

    // Return a book
    static void returnBook(Scanner sc) {
        System.out.print("Enter Book ID to return: ");
        int id = sc.nextInt();
        boolean found = false;

        for (Book b : books) {
            if (b.id == id) {
                found = true;
                if (b.isIssued) {
                    b.isIssued = false;
                    System.out.println("✅ Book returned successfully!");
                } else {
                    System.out.println("⚠ Book was not issued!");
                }
                break;
            }
        }

        if (!found) System.out.println("❌ Book not found!");
        saveData();
    }

    // Load data from file
    static void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                books.add(Book.fromFileString(line));
            }
            System.out.println("📂 Data loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("ℹ No previous data found. Starting fresh...");
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }
    }

    // Save data to file
    static void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book b : books) {
                bw.write(b.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }
}