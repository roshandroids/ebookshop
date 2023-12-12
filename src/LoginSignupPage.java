import java.sql.*;
import java.util.Scanner;

public class LoginSignupPage {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ebookshop";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "rootroot";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("************** Welcome to our ebookshop **************");
        System.out.println("1. Signup");
        System.out.println("2. Login");
        System.out.print("Please choose an option: ");
        int choice = sc.nextInt();

        UserModel user = new UserModel();
        System.out.print("Please enter your username: ");
        user.setName(sc.next());

        System.out.print("Please enter your password: ");
        user.setPassword(sc.next());


        switch (choice) {
            case 1:
                signup(sc,user);
                break;
            case 2:
                login(sc,user);
                break;
            case 3:
                System.out.println("See you again, Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Wrong option selected. Try again!");
        }
    }

    private static void signup(Scanner sc,UserModel user) {
        try (Connection connection = connectToDatabase()) {
            String insertQuery = "INSERT INTO USER_SIGNUP (username, password) VALUES (?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, user.getName());
                insertStatement.setString(2, user.getPassword());

                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Signup successful!");
                    System.out.println("******************************");
                    System.out.println("Logining you in !");
                    login(sc,user);
                } else {
                    System.out.println("Signup failed. Please try again.");
                }
            }
        } catch (SQLException e) {
            handleException("Signup failed", e);
        }
    }

    private static void login(Scanner sc,UserModel user) {
        try (Connection connection = connectToDatabase()) {
            String query = "SELECT * FROM USER_SIGNUP WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getPassword());

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Login successful!");
                        performBookOperations(sc,user);
                    } else {
                        System.out.println("User not found");
                    }
                }
            }
        } catch (SQLException e) {
            handleException("Login failed", e);
        }
    }

    private static void performBookOperations(Scanner sc, UserModel user) {

        System.out.println("***** Hi, there " + user.getName() + " *****");
        while(true){
        System.out.println("What do you want to do ?");
        System.out.println("1. Add a book");
        System.out.println("2. Update a book");
        System.out.println("3. Search a book");
        System.out.println("4. Remove a book");
        System.out.println("5. Exit");
        System.out.print("Please choose an option: ");

        int continueChoice = sc.nextInt();
        switch (continueChoice) {
            case 1:
                addBook(sc);
                break;
            case 2:
                updateBook(sc);
                break;
            case 3:
                searchBook(sc);
                break;
            case 4:
                removeBook(sc);
                break;
            case 5:
                System.out.println("See you again, Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Wrong option selected. Try again!");
        }
            System.out.println("Do you want to continue (Y/N)");
        String choice = sc.next();
        if(choice.equals("N")){
            break;
        }
        }
    }

    private static void addBook(Scanner sc) {

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            BookModel book = new BookModel();
            String insertQuery = "INSERT INTO BOOKS (id, title, author, price, qty) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                System.out.print("Enter book ID: ");
                book.setId(sc.nextInt());

                System.out.print("Enter book title: ");
                book.setTitle( sc.next());

                System.out.print("Enter book author: ");
                book.setAuthor( sc.next());

                System.out.print("Enter book price: ");
                book.setPrice( sc.nextFloat());

                System.out.print("Enter book quantity: ");
                book.setQty( sc.nextInt());

                insertStatement.setInt(1, book.getId());
                insertStatement.setString(2, book.getTitle());
                insertStatement.setString(3, book.getAuthor());
                insertStatement.setFloat(4, book.getPrice());
                insertStatement.setInt(5, book.getQty());

                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Book added successfully!");
                } else {
                    System.out.println("Failed to add book. Please try again.");
                }
            }
        } catch (SQLException e) {
            handleException("Error adding book", e);
        }
    }

    private static void updateBook(Scanner sc) {
        BookModel newBook =new BookModel();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            System.out.print("Enter book ID to update: ");
            newBook.setId( sc.nextInt());

            String selectQuery = "SELECT * FROM BOOKS WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
                ps.setInt(1, newBook.getId());


                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {

                        System.out.print("Enter book new title: ");
                        newBook.setTitle( sc.next());

                        System.out.print("Enter book new author: ");
                        newBook.setAuthor( sc.next());

                        System.out.print("Enter book new price: ");
                        newBook.setPrice( sc.nextFloat());

                        System.out.print("Enter book new quantity: ");
                        newBook.setQty( sc.nextInt());

                        String updateQuery = "UPDATE BOOKS SET title = ?, author = ?, price = ?, qty = ? WHERE id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setString(1, newBook.getTitle());
                            updateStatement.setString(2, newBook.getAuthor());
                            updateStatement.setFloat(3, newBook.getPrice());
                            updateStatement.setInt(4, newBook.getQty());
                            updateStatement.setInt(5, newBook.getId());

                            int rowsAffected = updateStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("Book updated successfully!");
                            } else {
                                System.out.println("Failed to update book. Please try again.");
                            }
                        }
                    } else {
                        System.out.println("No book found for the given ID.");
                    }
                }
            }
        } catch (SQLException e) {
            handleException("Error updating book", e);
        }
    }

    private static void searchBook(Scanner sc) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            System.out.print("Enter book ID to search: ");
            int bookId = sc.nextInt();

            String selectQuery = "SELECT * FROM BOOKS WHERE id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setInt(1, bookId);

                try (ResultSet rs = selectStatement.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Hurray !, Book found !!!");
                        System.out.println("****************************");
                        System.out.println("Book ID: " + rs.getInt("id"));
                        System.out.println("Title: " + rs.getString("title"));
                        System.out.println("Author: " + rs.getString("author"));
                        System.out.println("Price: " + rs.getFloat("price"));
                        System.out.println("Quantity: " + rs.getInt("qty"));
                        System.out.println("****************************");
                    } else {
                        System.out.println("Book not found for the given ID and username.");
                    }
                }
            }
        } catch (SQLException e) {
            handleException("Error searching for book", e);
        }
    }

    private static void removeBook(Scanner sc) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            System.out.print("Enter book ID to remove: ");
            int bookId = sc.nextInt();

            String deleteQuery = "DELETE FROM BOOKS WHERE id = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setInt(1, bookId);

                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Book removed successfully!");
                } else {
                    System.out.println("Failed to remove book. Please try again.");
                }
            }
        } catch (SQLException e) {
            handleException("Error removing book", e);
        }
    }

    private static Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }

    private static void handleException(String message, SQLException e) {
        System.err.println(message + ". Error: " + e.getMessage());
        System.err.println(e.toString());
    }
}
