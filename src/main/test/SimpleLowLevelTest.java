import com.mysql.cj.jdbc.result.ResultSetImpl;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SimpleLowLevelTest {

    final static String url = "jdbc:mysql://localhost:3306/warehouse?useSSL=false&" +
            "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //final static String url = "jdbc:mysql://localhost:3306/warehouse";
    //final static String url = "jdbc:mysql://localhost:3306/warehouse?useSSL=false&";
    final static String password = "root1";
    final static String login = "root";

    @Test
    public void testItemCRUD() throws SQLException {
        String testData = "test address";
        Long warehouse_Id = 1l;
        Long itemId = insertItem(testData, warehouse_Id);
        //Long itemId = insertItem(testData, 1l);
        System.out.println("testData = " + testData);
        System.out.println("itemId = " + itemId);

        // add assert to check that "testData" is present in response
        // Сравнить вытащенный объект из базы данных с тестовым объектом
        // 1-й вариант проверки (возврат name по переданному id товара)
        String nameSelectedItem = getNameItemById(itemId);
        System.out.println("assert getNameItemById(itemId) 1: nameSelectedItem = " + nameSelectedItem);
        assertEquals(testData, nameSelectedItem);

        // 2-й вариант проверки (возврат множества ключей со значениями полей, влючая name по переданному id товара)
        nameSelectedItem = "";
        LinkedHashMap resultsSelect = getFieldsItemById(itemId);
        if (resultsSelect.size() > 0)
            nameSelectedItem = resultsSelect.get("name").toString();
        System.out.println("assert getFieldsItemById(itemId) 2: nameSelectedItem = " + nameSelectedItem);
        assertEquals(testData, nameSelectedItem);

        // 3-й вариант проверки (возврат множества ключей со значениями полей, влючая name по переданному id товара и warehouseId склада)
        nameSelectedItem = "";
        resultsSelect = getFieldsItemByIdAndWarehouseId(itemId, warehouse_Id);
        if (resultsSelect.size() > 0)
            nameSelectedItem = resultsSelect.get("name").toString();
        System.out.println("assert 3: nameSelectedItem = " + nameSelectedItem);
        assertEquals(testData, nameSelectedItem);

        System.out.printf("ItemID: " + itemId);
        delete(itemId);
    }

    public String getNameItemById(Long itemIdFind) {
        Connection connection = null;
        final String SQL_SELECT = "select * from items where id = ?";
        //final String SQL_SELECT = "select items.name from " + "items" + " where items.id = ?";
        String nameItemId = "";

        try {
            connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, itemIdFind);
            statement.execute();

            ResultSet generatedkeys = statement.getResultSet();
            if (generatedkeys.next()) {
                nameItemId = generatedkeys.getString("name");
                //nameItemId = generatedkeys.getString(1);
                System.out.println("generatedkeys.getString(\"name\") = " + nameItemId);
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
        return nameItemId;
    }

    public LinkedHashMap<String, Object> getFieldsItemById(Long itemIdFind) {
        Connection connection = null;
        final String SQL_SELECT = "select * from items where id = ?";
        LinkedHashMap<String, Object> resultsSelect = new LinkedHashMap<String, Object>();

        try {
            connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, itemIdFind);
            statement.execute();

            ResultSet generatedkeys = statement.getResultSet();
            if (generatedkeys.next()) {
                resultsSelect.put("id", generatedkeys.getString("id"));
                resultsSelect.put("name", generatedkeys.getString("name"));
                resultsSelect.put("warehouse_id", generatedkeys.getString("warehouse_id"));
                //nameItemId = generatedkeys.getString("name");
                //itemId = generatedkeys.getLong(1);
                //nameItemId = generatedkeys.getString(1);
                System.out.println("resultsSelect.get(\"name\") = " + resultsSelect.get("name"));
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
        return resultsSelect;
    }

    public LinkedHashMap<String, Object> getFieldsItemByIdAndWarehouseId(Long itemIdFind, Long warehouseId) {
        Connection connection = null;
        final String SQL_SELECT = "select * from items where id = ? AND warehouse_id = ?";
        //final String SQL_SELECT = "select items.name from " + "items" + " where items.id = ?";
        //String nameItemId = "";
        LinkedHashMap<String, Object> resultsSelect = new LinkedHashMap<String, Object>();

        try {
            connection = DriverManager.getConnection(url, login, password);
            PreparedStatement statement = connection.prepareStatement(SQL_SELECT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, itemIdFind);
            statement.setLong(2, warehouseId);
            statement.execute();

            ResultSet generatedkeys = statement.getResultSet();
            if (generatedkeys.next()) {
                resultsSelect.put("id", generatedkeys.getString("id"));
                resultsSelect.put("name", generatedkeys.getString("name"));
                resultsSelect.put("warehouse_id", generatedkeys.getString("warehouse_id"));
                //nameItemId = generatedkeys.getString("name");
                //nameItemId = generatedkeys.getString(1);
                System.out.println("resultsSelect.get(\"name\") = " + resultsSelect.get("name"));
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
        return resultsSelect;
    }


    //    public Long selectItemByNameAndWerehous(String itemName, Long wireHouseId) {
    //        Connection connection = null;
    //        final String SQL_SELECT = "select items.id from " + "items" + " where items.name = ?" + " and items.warehouse_id = ?";
    //        Long itemId = null;
    //
    //        try {
    //            connection = DriverManager.getConnection(url, login, password);
    //            PreparedStatement statement = connection.prepareStatement(SQL_SELECT, Statement.RETURN_GENERATED_KEYS);
    //            statement.setString(1, itemName);
    //            statement.setLong(2, wireHouseId);
    //            statement.execute();
    //
    //            ResultSet generatedkeys = statement.getResultSet();
    //            if (generatedkeys.next()) {
    //                itemId = generatedkeys.getLong(1);
    //            }
    //            //            ResultSet generatedkeys = statement.getGeneratedKeys();
    //            //            if (generatedkeys.next()) {
    //            //                itemId = generatedkeys.getLong(1);
    //            //            }
    //        } catch (SQLException e) {
    //            e.printStackTrace();
    //        } finally {
    //            try {
    //                connection.close();
    //            } catch (SQLException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //        return itemId;
    //    }

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

        String URL = "jdbc:mysql://localhost:3306/warehouse";
        //        String URL = "jdbc:mysql://localhost:3306/warehouse?useSSL=false&" +
        //                "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
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
