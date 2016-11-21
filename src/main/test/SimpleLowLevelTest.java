import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.sql.*;

public class SimpleLowLevelTest {

    final static String url = "jdbc:mysql://localhost:3306/warehouse";
    //    final static String url = "jdbc:mysql://localhost:3306/warehouse?useSSL=false&" +
    //            "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //final static String url = "jdbc:mysql://localhost:3306/warehouse?useSSL=false&";
    final static String password = "root1";
    final static String login = "root";

    @Test
    public void testItemCRUD() {
        String testData = "test address";
        Long itemId = insertItem(testData, 1l);
        System.out.println("itemId = " + itemId);

        //TODO use findBy to get item from DB
        // add assert to check that "testData" is present in response
        // Сравнить вытащенный объект из базы данных с тестовым объектом
        Long selectedId = selectItemById(testData, 1l);
        System.out.println("selectedId = " + selectedId);
        assertEquals(itemId, selectedId);

        System.out.printf("ItemID: " + itemId);
        delete(itemId);
    }

    public Long selectItemById(String itemName, Long wireHouseId) {
        Connection connection = null;
        final String SQL_SELECT = "select items.id from " + "items"
                + " where items.name = ?"
                + " and items.warehouse_id = ?";
        Long itemId = null;

        try {
            connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, itemName);
            statement.setLong(2, wireHouseId);
            statement.execute();

            ResultSet generatedkeys = statement.getResultSet();
            if (generatedkeys.next()) {
                itemId = generatedkeys.getLong(1);
            }
            //            ResultSet generatedkeys = statement.getGeneratedKeys();
            //            if (generatedkeys.next()) {
            //                itemId = generatedkeys.getLong(1);
            //            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return itemId;
    }

    public Long insertItem(String itemName, Long wireHouseId) {
        Connection connection = null;
        final String SQL_INSERT = "insert into " + "items" + " (" + "name" + ", " + "warehouse_id" + ") values (?, ?)";
        Long itemId = null;

        try {
            connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, itemName);
            statement.setLong(2, wireHouseId);
            statement.execute();

            ResultSet generatedkeys = statement.getGeneratedKeys();
            if (generatedkeys.next()) {
                itemId = generatedkeys.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return itemId;
    }

    public void delete(Long itemId) {
        Connection connection = null;
        final String SQL_DELETE = "delete from " + "items" + " where " + "id" + " = ?";

        try {
            connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setLong(1, itemId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void jdbc_test() {

        String URL = "jdbc:mysql://localhost:3306/warehouse?useSSL=false&" +
                "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        //    private final static String URL = "jdbc:mysql://localhost:3306/db_JDBC_ConnectTest?useSSL=false&" +
        //            "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String USERNAME = "root";
        String PASSWORD = "root1";

        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            return;
        }

        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
    }
}
