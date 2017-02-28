package sample.db;

import com.sun.org.apache.xpath.internal.SourceTree;
import sample.objects.Order;
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
    public static Statement statement;
    public static ResultSet resultSet;

    //===== ПОКДЛЮЧЕНИЕ К БД ===========
    public static void conn() throws ClassNotFoundException, SQLException {

        connection = null;
        System.out.println(Class.forName("org.sqlite.JDBC"));
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:sklad_pilot.db");
        System.out.println("БД подкоючена");
    }

    //========Создание таблицы==============
    public static void createTable() throws SQLException {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");

        System.out.println("Таблица создана или уже есть");
    }

    //=======Заполнение таблиц==============
    //Product
    public static void writeDB(Product product) throws SQLException {
        String name = product.getName();
        String amount = product.getAmount();
        int supplier_id = 1;
        String storage = product.getStorage();
        Statement st = connection.createStatement();
        System.out.println("select Id from supplier where name=" + product.getSupplier().getCompany());
        ResultSet rs = st.executeQuery("select Id from supplier where name='" + product.getSupplier().getCompany()+"'");
        while (rs.next()) {
            supplier_id = rs.getInt("Id");
        }

        st.execute("INSERT INTO Product (Name, Amount, Supplier_id, Storage) VALUES ( '" + name + "' , '" + amount + "' , '" + supplier_id + "' , '" + storage + "' ); ");

        System.out.println("Таблица продуктов заполнена");
    }
    //Supplier
    public static void writeDB(Supplier supplier) throws SQLException {
        String name = supplier.getCompany();
        String address = supplier.getAddress();
        String telephone = supplier.getPhone();
        Statement st = connection.createStatement();
        st.execute("INSERT INTO Supplier (Name, Address, Telephone) VALUES ( '" + name + "' , '" + address + "' , '" + telephone + "' ); ");

        System.out.println("Таблица поставщиков заполнена");
    }
    //Orders
    public static void writeDB(Order order) throws SQLException {


        Statement st = connection.createStatement();
        st.execute("INSERT INTO Orders (Date) VALUES ( '" + order.getDate() + "' ); ");

        System.out.println("Таблица заказов заполнена");
    }
    //Ordered_Products
    public static void writeDBOrderedProducts(Product product) throws SQLException {

        Statement st = connection.createStatement();

//        st.execute("INSERT INTO Orders (Date) VALUES ( '" + date + "' ); ");

        System.out.println("Таблица заказов заполнена");
    }
    //=======UPDATE DB=======================
    public static void updateDB(Product product) throws SQLException {
        statement.executeUpdate("update product set name='"+product.getName()+"',amount='"+product.getAmount()+"',supplier_id='"+product.getSupplier().getId()+"',storage="+product.getStorage()+" where id="+product.getId());
    }
    public static void updateDB(Supplier supplier) throws SQLException {
        statement.executeUpdate("update supplier set name='"+supplier.getCompany()+"',address='"+supplier.getAddress()+"',telephone='"+supplier.getPhone()+"' where id="+supplier.getId());
    }
    public static void updateDB(Order order) throws SQLException {
        statement.executeUpdate("update orders set date='"+order.getDate()+"' where id="+order.getOrderNumber());
    }
//================Delete row DB===============
    public static void deleteDB(Product product) throws SQLException {
    statement.executeUpdate("delete from product where id="+product.getId());
}
    public static void deleteDB(Supplier supplier) throws SQLException {
    statement.executeUpdate("delete from supplier where id="+supplier.getId());
}
    public static void deleteDB(Order order) throws SQLException {
    statement.executeUpdate("delete from orders where id="+order.getOrderNumber());
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
        return resultSetMap;
    }

    //======Закрытие==========================
    public static void closeDB() throws SQLException {
        connection.close();
        statement.close();
        resultSet.close();

        System.out.println("Закрыли соединения");

    }

    public static int readIDDB(Product product) throws SQLException {
        int id = 0;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select id from product where rowid=last_insert_rowid()");
        while (rs.next()){
            id = rs.getInt(1);
        }
        return id;
    }
    public static int readIDDB(Supplier supplier) throws SQLException {
        int id = 0;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select id from supplier where rowid=last_insert_rowid()");
        while (rs.next()){
            id = rs.getInt(1);
        }
        return id;
    }
    public static int readIDDB(Order order) throws SQLException {
        int id = 0;
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select id from orders where rowid=last_insert_rowid()");
        while (rs.next()){
            id = rs.getInt(1);
        }
        return id;
    }


}
