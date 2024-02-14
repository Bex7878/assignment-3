import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = ",trceknfy";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            createTableIfNotExists(connection);

            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("Choose operation:");
                System.out.println("1. Create");
                System.out.println("2. Read");
                System.out.println("3. Update");
                System.out.println("4. Delete");
                System.out.println("0. Exit");

                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        createStudent(connection, scanner);
                        break;
                    case 2:
                        readStudents(connection);
                        break;
                    case 3:
                        updateStudent(connection, scanner);
                        break;
                    case 4:
                        deleteStudent(connection, scanner);
                        break;
                }

            } while (choice != 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTableIfNotExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS students (id SERIAL PRIMARY KEY, name VARCHAR(255), age INT)");
        }
    }

    private static void createStudent(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter student name:");
        String name = scanner.nextLine();

        System.out.println("Enter student age:");
        int age = scanner.nextInt();
        scanner.nextLine();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students (name, age) VALUES (?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student created successfully!");
            } else {
                System.out.println("Failed to create student.");
            }
        }
    }

    private static void readStudents(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM students")) {

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Name: " + resultSet.getString("name") +
                        ", Age: " + resultSet.getInt("age"));
            }
        }
    }

    private static void updateStudent(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter student ID to update:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter new student name:");
        String name = scanner.nextLine();

        System.out.println("Enter new student age:");
        int age = scanner.nextInt();
        scanner.nextLine();

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE students SET name=?, age=? WHERE id=?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setInt(3, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Failed to update student.");
            }
        }
    }

    private static void deleteStudent(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter student ID to delete:");
        int id = scanner.nextInt();
        scanner.nextLine();

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM students WHERE id=?")) {
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Failed to delete student. Student not found.");
            }
        }
    }
}
