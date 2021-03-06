package sample.impls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.db.Connect;
import sample.interfaces.Sklad;
import sample.objects.*;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Note-001 on 15.12.2016.
 */
public class CollectionSklad implements Sklad {

    private ObservableList<Product> productArrayList = FXCollections.observableArrayList();
    private ObservableList<User> usersArrayList = FXCollections.observableArrayList();
    private ObservableList<Supplier> suppliersArrayList = FXCollections.observableArrayList();
    private ObservableList<Order> ordersArrayList = FXCollections.observableArrayList();
    private ObservableList<Product> productSelectedListForOrder = FXCollections.observableArrayList();
    private ObservableList<OrderedProduct> orderedProductArrayList = FXCollections.observableArrayList();


    //=====================CRUD PRODUCT LIST and DB===============================================
    @Override
    public void create(Product product) throws SQLException {
        try {
            Connect.writeDB(product);
            product.setId(Connect.readIDDB(product));
            productArrayList.add(product);
        } catch (Exception e) {
            //messageBox
        } finally {
            //закрыть Connect
        }
    }

    @Override
    public void update(Product product) throws SQLException {
        Connect.updateDB(product);
        List<String> list = new ArrayList<>();
    }

    @Override
    public void delete(Product product) {
        productArrayList.remove(product);
    }

    //=====================CRUD ORDER PRODUCTS SELECTED LIST and DB===============================================
    public void deleteProductSelectedListForOrder() {
        productSelectedListForOrder.removeAll();
    }

    public void delete(ObservableList<Product> productObservableList) {
        productSelectedListForOrder.clear();
    }

    //=====================CRUD USER LIST and DB===============================================
    @Override
    public void create(User user) {
        //TODO реализовать CRUD DB
        usersArrayList.add(user);
    }

    @Override
    public void update(User user) {
    }

    @Override
    public void delete(User user) {
        usersArrayList.remove(user);
    }

    //=====================CRUD SUPPLIER LIST and DB===============================================
    @Override
    public void create(Supplier supplier) throws SQLException {
        Connect.writeDB(supplier);
        supplier.setId(Connect.readIDDB(supplier));
        suppliersArrayList.add(supplier);
    }

    @Override
    public void update(Supplier supplier) {
        try {
            Connect.updateDB(supplier);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Supplier supplier) throws SQLException {
        Connect.deleteDB(supplier);
        suppliersArrayList.remove(supplier);
    }

    //=====================CRUD ORDER LIST and DB===============================================
    @Override
    public void create(Order order) throws SQLException {
        Connect.writeDB(order);
        order.setOrderNumber(Connect.readIDDB(order));
        ordersArrayList.add(order);

        Connect.writeDBOrderedProductsList(order); //пишем в БД список продуктов у заказа
        for (OrderedProduct orderedProduct : order.getOrderedProductList()) {
            //берем из БД айдишники и кидаем их в объекты
            orderedProduct.setId(Connect.readLastIdDbOrderedProducts(order));
        }
    }

    @Override
    public void update(OrderedProduct orderedProduct) {
    }

    @Override
    public void delete(Order order) throws SQLException {
        //удаляем заказ из БД
        Connect.deleteDB(order);
        //удаляем все заказанные продукты по этому заказу из БД
        if (order.getOrderedProductList().size() > 0) {
            Connect.deleteDB(order.getOrderNumber());
        }

        ordersArrayList.remove(order);
    }

    //=====================CLEAR ALL DATA LISTS===============================================
    public void clearLists() {
        productArrayList.clear();
        usersArrayList.clear();
        suppliersArrayList.clear();
        ordersArrayList.clear();
        orderedProductArrayList.clear();
        productSelectedListForOrder.clear();
    }

    //=====================READ ALL DATA FROM DB TO LISTS===============================================
    public void fillInitFromDb() throws SQLException {
        Map<String, ResultSet> mapInitialDB = Connect.readAllDB();

        for (Map.Entry<String, ResultSet> entry : mapInitialDB.entrySet()) {
            String tableName = entry.getKey();
            ResultSet resultSet = entry.getValue();
            while (resultSet != null && resultSet.next()) {
                if (tableName.equals("Supplier")) {
                    suppliersArrayList.add(new Supplier(resultSet.getInt("Id"), resultSet.getString("Name"), resultSet.getString("Address"), resultSet.getString("Telephone")));
                } else if (tableName.equals("Product")) {
                    //сначала ищем совпадение id из БД и id из списка поставщиков, потом уже добавляем новый продукт с
                    // этим поставщиком
                    int supIdDb = resultSet.getInt("supplier_id");
                    for (Supplier supplier : suppliersArrayList) {
                        if (supplier.getId() == supIdDb) {
                            productArrayList.add(new Product(resultSet.getInt("Id"), resultSet.getString("Name"), resultSet.getString("Amount"), resultSet.getString("Storage"), supplier));
                        }
                    }
                } else if (tableName.equals("Orders")) {
                    ordersArrayList.add(new Order(resultSet.getInt("Id"), resultSet.getString("Date")));
                }
            }
        }
        //отдельное чтение БД Ordered_Products, тк она содержит внешние ключи на другие таблицы
        //сначала грузим выше данные из других таблиц, после эту
        Map<String, ResultSet> mapInitialDB2 = Connect.readAllDB();
        for (Map.Entry<String, ResultSet> entry : mapInitialDB2.entrySet()) {
            String tableName = entry.getKey();
            ResultSet resultSet = entry.getValue();

            while (resultSet != null && resultSet.next()) {
                if (tableName.equals("Ordered_Products")) {
                    int orderedProductId = resultSet.getInt("id");
                    int orderId = resultSet.getInt("order_id");
                    int idProduct = resultSet.getInt("product_id");
                    int amount = resultSet.getInt("amount");
                    Product product = new Product();
                    System.out.println(orderedProductId + " " + orderId + " " + idProduct + " " + amount);
                    for (Product prod : productArrayList) {
                        if (prod.getId() == idProduct) {
                            product = prod;
                            System.out.println(product);
                        }
                    }

                    for (Order order : ordersArrayList) {
                        System.out.println(order.getOrderNumber());
                        if (order.getOrderNumber() == orderId) {
                            order.addOrderedProductToList(new OrderedProduct(orderedProductId, product, amount));
                        }
                    }
                }
            }
        }
    }

    //=====================================================================================================
    public ObservableList<Product> getProductSelectedListForOrder() {
        return productSelectedListForOrder;
    }

    public void setProductSelectedListForOrder(ObservableList<Product> productSelectedListForOrder) {

        this.productSelectedListForOrder = productSelectedListForOrder;
    }

    public ObservableList<Product> getProductArrayList() {
        return productArrayList;
    }

    public ObservableList<User> getUsersArrayList() {
        return usersArrayList;
    }

    public ObservableList getSuppliersArrayList() {
        return suppliersArrayList;
    }

    public ObservableList<Order> getOrdersArrayList() {
        return ordersArrayList;
    }

    public ObservableList<OrderedProduct> getOrderedProductArrayList() {
        return orderedProductArrayList;
    }

    public void setOrderedProductArrayList(ObservableList<OrderedProduct> orderedProductArrayList) {
        this.orderedProductArrayList = orderedProductArrayList;
    }

}
