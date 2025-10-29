import java.io.*;
import java.util.*;

class Book {
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

    // Convert book details to text for saving
    String toFileString() {
        return id + "," + title + "," + author + "," + isIssued + "\n";
    }

    // Read one book record from text line
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
        loadData();
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

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    issueBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    saveData();
                    System.out.println("Data saved. Exiting program...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1 to 5.");
            }
        }
    }

    // Add a new book (no duplicate IDs)
    static void addBook() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine(); // consume newline

        // Check for duplicate ID
        for (Book b : books) {
            if (b.id == id) {
                System.out.println("Book ID already exists. Please use a different ID.");
                return;
            }
        }

        System.out.print("Enter Book Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Author Name: ");
        String author = sc.nextLine();

        books.add(new Book(id, title, author, false));
        saveData();
        System.out.println("Book added successfully.");
    }

    // Show all books
    static void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        System.out.println("\n--- Book List ---");
        for (Book b : books) {
            String status = b.isIssued ? "Issued" : "Available";
            System.out.println(b.id + " | " + b.title + " | " + b.author + " | " + status);
        }
    }

    // Issue a book by ID
    static void issueBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book ID to issue: ");
        int id = sc.nextInt();
        boolean found = false;

        for (Book b : books) {
            if (b.id == id) {
                found = true;
                if (!b.isIssued) {
                    b.isIssued = true;
                    System.out.println("Book issued successfully.");
                } else {
                    System.out.println("Book is already issued.");
                }
                break;
            }
        }

        if (!found) {
            System.out.println("Book not found.");
        }

        saveData();
    }

    // Return a book by ID
    static void returnBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book ID to return: ");
        int id = sc.nextInt();
        boolean found = false;

        for (Book b : books) {
            if (b.id == id) {
                found = true;
                if (b.isIssued) {
                    b.isIssued = false;
                    System.out.println("Book returned successfully.");
                } else {
                    System.out.println("Book was not issued.");
                }
                break;
            }
        }

        if (!found) {
            System.out.println("Book not found.");
        }

        saveData();
    }

    // Load all books from file using FileReader
    static void loadData() {
        try {
            FileReader fr = new FileReader(FILE_NAME);
            StringBuilder data = new StringBuilder();
            int ch;
            while ((ch = fr.read()) != -1) {
                data.append((char) ch);
            }
            fr.close();

            String[] lines = data.toString().split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    books.add(Book.fromFileString(line));
                }
            }

            System.out.println("Data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh...");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Save all books to file using FileWriter
    static void saveData() {
        try {
            FileWriter fw = new FileWriter(FILE_NAME);
            for (Book b : books) {
                fw.write(b.toFileString());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
