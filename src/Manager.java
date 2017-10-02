/**
 * Created by Mark on 30.09.2017.
 */
import java.sql.*;
import java.util.*;

public class Manager {
    Connection co;
    static Manager manager;

    public static void main (String [] args) {
        manager = new Manager();
        manager.waitForCommand();
    }

    public void waitForCommand(){
        manager.connect();
        boolean stayInProgram = true;
        Scanner scanner = new Scanner(System.in);
        while (stayInProgram) {
            System.out.println();
            System.out.println("help - команда для виклику допомоги");
            System.out.print("Введіть команду: ");
            String command = scanner.next();
            switch (command) {
                case "get_games":
                    manager.getTable("games", "");
                    break;

                case "get_films":
                    manager.getTable("films", "");
                    break;

                case "get_books":
                    manager.getTable("books", "");
                    break;

                case "get_games_sort":
                    manager.getTable("games", "ORDER BY name");
                    break;

                case "get_films_sort":
                    manager.getTable("films", "ORDER BY name");
                    break;

                case "get_books_sort":
                    manager.getTable("books", "ORDER BY name");
                    break;

                case "add_game":
                    manager.addMultimedia("games");
                    break;

                case "add_film":
                    manager.addMultimedia("films");
                    break;

                case "add_book":
                    manager.addMultimedia("books");
                    break;

                case "exit":
                    stayInProgram = false;
                    break;

                case "help":
                    manager.help();
                    break;

                case "delete":
                    manager.delMultimedia();
                    break;

                default:
                    System.out.println("Введіть коректну команду");
                    break;
            }
        }
        manager.close();
    }

    public void connect() {

        try {

            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection ("jdbc:sqlite:D:\\Programs\\java\\manager\\src\\users.db");
            System.out.println("Connected");

        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }

    public void addMultimedia(String table) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter multimedia name : ");
        String name = scanner.nextLine();
        System.out.println("Enter multimedia status : ");
        String status = scanner.nextLine();

        String query = "INSERT INTO " + table + " (name, status) " + "VALUES ('" + name + "', '" + status + "') ";
        try {
            Statement statement = co.createStatement();
            statement.executeUpdate(query);
            System.out.println("Запит оброблено!");
            statement.close();
        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }

    public void getTable(String table, String order) {
        try {
            Statement statement = co.createStatement();

            String query = "SELECT id, name, status FROM " + table + " " + order;
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String status = rs.getString("status");
                System.out.println(id + " | " + name + " | " + status);
            }
            rs.close();
            statement.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delMultimedia() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть тип мультимедіа (команди games, films, books ) : ");
        String table = scanner.nextLine();
        switch (table) {
            case "games":
                System.out.println("Введіть назву гри: ");
                break;

            case "films":
                System.out.println("Введіть назву фільму: ");
                break;

            case "books":
                System.out.println("Введіть назву книги: ");
                break;
            default:
                table ="";
                break;
        }
        if(!table.equals("")) {
            String name = scanner.nextLine();

            String query = "DELETE FROM " + table + " WHERE name = '" + name + "';";
            try {
                Statement statement = co.createStatement();
                statement.executeUpdate(query);
                statement.close();
            } catch (Exception ex) {

                ex.printStackTrace();

            }
        } else {
            System.out.println("Невірно введений тип мультимедіа");
        }
    }

    public void help(){
        System.out.println("Список команд: ");
        System.out.println("exit - вихід з програми");
        System.out.println("add_book - додати книгу");
        System.out.println("add_film - додати фільм");
        System.out.println("add_game - додати гру");
        System.out.println("get_books - вивести список книг");
        System.out.println("get_films - вивести список фільмів");
        System.out.println("get_games - вивести список ігор");
        System.out.println("get_books_sort - вивести відсортований список книг");
        System.out.println("get_films_sort - вивести відсортований список фільмів");
        System.out.println("get_games_sort - вивести відсортований список ігор");
        System.out.println("delete - видалити мультимедыйний елемент \n");
    }

    public void close() {
        try {
            co.close();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
