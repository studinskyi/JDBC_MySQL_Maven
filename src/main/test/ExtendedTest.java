import db.dao.ItemDao;
import db.dao.impl.ItemDaoImpl;
import db.model.Item;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class ExtendedTest {

    private ItemDao itemDao = new ItemDaoImpl();

    @Test
    public void testItemCRUD() {
        String testData = "test address";
        // Создать тестовый объект
        Item testItem = new Item();
        testItem.setName(testData);
        testItem.setWarehouse_id(1l);
        // Сохранить тестовый объект в базе данных
        itemDao.insert(testItem);
        // Вытащить тестовый объект из базы данных
        Item itemFromDb = itemDao.findById(testItem.getId());
        // Сравнить вытащенный объект из базы данных с тестовым объектом
        assertEquals(testItem.getName(), itemFromDb.getName());
        // Удалить тестовый объект в базе данных
        itemDao.delete(itemFromDb);
        // Найти удаленный объект в базе данных
        Item removedItem = itemDao.findById(itemFromDb.getId());
        // Сравнить вытащенный объект после удаления из базы данных на null
        assertNull(removedItem);
    }
}
