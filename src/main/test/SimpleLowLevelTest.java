import org.testng.annotations.Test;

import java.sql.*;

public class SimpleLowLevelTest {

    final static String url = "jdbc:mysql://localhost:3306/warehouse?useSSL=false&";
    final static String password = "root1";
    final static String login = "root";

    @Test
    public void testItemCRUD() {
        String testData = "test address";
        Long itemId = insertItem(testData, 1l);

        //TODO use findBy to get item from DB
        // add assert to check that "testData" is present in response

        System.out.printf("ItemID: " + itemId);
        delete(itemId);
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
}
