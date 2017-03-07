package sample.db;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.collections.ObservableList;
import sample.objects.Order;
import sample.objects.OrderedProduct;
import sample.objects.Product;
import sample.objects.Supplier;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by Note-001 on 22.02.2017.
 */
public class Connect {
    public static Connection connection;
    //TODO Connection, Statement, ResultSet должны быть в пределах одного метода, не нужно объявлять их полями класса
    //объявлять их нужно до конструкции try catch finally
    //можно создать свой класс исключений, в который пробрасывать их из catch
    //method{ Connction, RS, Statement = null
    //try{ user.id=resultSet.readInt(1)
    // example http://pastebin.com/U1870kfH
    //но можно сделать один трай на метод, в финалле закрывать тоже чз трай
    public static Statement statement;
    public static ResultSet resultSet;

    //===== ПОКДЛЮЧЕНИЕ К БД ===========
    public static void conn() throws ClassNotFoundException, SQLException {
        connection = null;
        System.out.println(Class.forName("org.sqlite.JDBC"));
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:sklad_pilot.db");
        statement = connection.createStatement();
        System.out.println("БД подкоючена");
    }

    //=======Заполнение таблиц==============
    //Product
    public static void writeDB(Product product) throws SQLException {
        String name = product.getName();
        String amount = product.getAmount();
        int supplier_id = 1;
        String storage = product.getStorage();

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select Id from supplier where name='" + product.getSupplier().getCompany() + "'");
        while (rs.next()) {
            supplier_id = rs.getInt("Id");
        }

        st.execute("INSERT INTO Product (Name, Amount, Supplier_id, Storage) VALUES ( '" + name + "' , '" + amount + "' , '" + supplier_id + "' , '" + storage + "' ); ");
    }

    //Supplier
    public static void writeDB(Supplier supplier) throws SQLException {
        String name = supplier.getCompany();
        String address = supplier.getAddress();
        String telephone = supplier.getPhone();

        Statement st = connection.createStatement();
        st.execute("INSERT INTO Supplier (Name, Address, Telephone) VALUES ( '" + name + "' , '" + address + "' , '" + telephone + "' ); ");
    }

    //Orders
    public static void writeDB(Order order) throws SQLException {
        String orderDate = order.getDate();

        Statement st = connection.createStatement();
        st.execute("INSERT INTO Orders (Date) VALUES ( '" + orderDate + "' ); ");
    }

    //Ordered_Products
    public static void writeDBOrderedProductsList(Order order) throws SQLException {
        ObservableList<OrderedProduct> orderedProductArrayList = order.getOrderedProductList();
        int orderId = order.getOrderNumber();

        for (OrderedProduct orderedProduct:orderedProductArrayList) {
            Statement st = connection.createStatement();
            int amount = orderedProduct.getAmount();
            int productId = orderedProduct.getOrderedProduct().getId();
            st.execute("INSERT INTO Ordered_Products (order_id, product_id, amount) VALUES ( '" + orderId + "' , '" + productId + "' , '" + amount + "' ); ");
            st.close();
        }

    }

    //=================UPDATE DB=======================
    public static void updateDB(Product product) throws SQLException {
        statement.executeUpdate("update product set name='" + product.getName() + "',amount='" + product.getAmount() + "',supplier_id='" + product.getSupplier().getId() + "',storage=" + product.getStorage() + " where id=" + product.getId());
    }

    public static void updateDB(Supplier supplier) throws SQLException {
        statement.executeUpdate("update supplier set name='" + supplier.getCompany() + "',address='" + supplier.getAddress() + "',telephone='" + supplier.getPhone() + "' where id=" + supplier.getId());
    }

    public static void updateDB(OrderedProduct orderedProduct) throws SQLException {
        //сначала удаляем все записи заказанных продуктов по данному ордеру, потом пишем с нуля
        statement.executeUpdate("update ordered_products set product_id='" + orderedProduct.getOrderedProduct().getId() +"',amount='" + orderedProduct.getAmount() + "' where id=" + orderedProduct.getId());
    }

    //================DELETE row from DB===============
    public static void deleteDB(Product product) throws SQLException {
        statement.executeUpdate("delete from product where id=" + product.getId());
    }

    public static void deleteDB(Supplier supplier) throws SQLException {
        statement.executeUpdate("delete from supplier where id=" + supplier.getId());
    }

    public static void deleteDB(Order order) throws SQLException {
        statement.executeUpdate("delete from orders where id=" + order.getOrderNumber());
    }
    public static void deleteDB(int orderId) throws SQLException {
        statement.executeUpdate("delete from ordered_products where order_id=" + orderId);
    }
    public static void deleteDB(OrderedProduct orderedProduct, int orderId) throws SQLException {
        statement.executeUpdate("delete from ordered_products where order_id=" + orderId + " and product_id="+orderedProduct.getOrderedProduct().getId());
    }

    //======Вывод из БД======================
    //Грузим все содержимое БД
    public static Map<String, ResultSet> readAllDB() throws SQLException {
        Statement stateGetNames = connection.createStatement();
        Map<String, ResultSet> resultSetMap = new HashMap<>();
        //узнаем имена всех таблиц
        ResultSet resultSet = stateGetNames.executeQuery("select name from sqlite_master where type = 'table'");
        while (resultSet.next()) {
            String tableName = resultSet.getString("Name");
            //кидаем содержимое каждой таблицы в коллекцию, а ее в эррейлист
            if (!tableName.equals("sqlite_sequence")) { //это название служебной таблицы
                Statement statement = connection.createStatement();
                resultSetMap.put(tableName, statement.executeQuery("Select * from " + tableName));
            }
        }
        stateGetNames.close();
        resultSet.close();
        return resultSetMap;
    }

    //======READING FROM DB======================
    //Product
    public static int readIDDB(Product product) throws SQLException {
        int id = 0;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select id from product where rowid=last_insert_rowid()");
        while (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    //Supplier
    public static int readIDDB(Supplier supplier) throws SQLException {
        int id = 0;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select id from supplier where rowid=last_insert_rowid()");
        while (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    //Order
    public static int readIDDB(Order order) throws SQLException {
        int id = 0;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select id from orders where rowid=last_insert_rowid()");
        while (rs.next()) {
            id = rs.getInt(1);
        }
        st.close();
        rs.close();
        return id;
    }
    //OrderedProducts
    public static int readLastIdDbOrderedProducts(Order order) throws SQLException {
        int id = 0;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select id from ordered_products where rowid=last_insert_rowid()");
        while (rs.next()) {
            id = rs.getInt(1);
        }
        st.close();
        rs.close();
        return id;
    }
    public static List<Integer> readIdDbOrderedProducts(Order order) throws SQLException {
        //берем id продуктов у определенного заказа
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select id from ordered_products where order_id="+order.getOrderNumber());
        List<Integer> idList = new ArrayList<>();
        while (rs.next()) {
            idList.add(rs.getInt(1));
        }
        st.close();
        rs.close();
        return idList;
    }
    //======Close Connection==========================
    public static void closeDB() throws SQLException {
        connection.close();
        statement.close();
        resultSet.close();
    }
}
