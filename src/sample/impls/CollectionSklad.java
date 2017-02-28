package sample.impls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.db.Connect;
import sample.interfaces.Sklad;
import sample.objects.Order;
import sample.objects.Product;
import sample.objects.Supplier;
import sample.objects.User;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Note-001 on 15.12.2016.
 */
public class CollectionSklad implements Sklad {

    private ObservableList<Product> productArrayList = FXCollections.observableArrayList();
    private ObservableList<User> usersArrayList = FXCollections.observableArrayList();
    private ObservableList<Supplier> suppliersArrayList = FXCollections.observableArrayList();
    private ObservableList<Order> ordersArrayList = FXCollections.observableArrayList();
    private ObservableList<Product> productSelectedListForOrder = FXCollections.observableArrayList();

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

    @Override
    public void create(Product product) throws SQLException {
        Connect.writeDB(product);
        product.setId(Connect.readIDDB(product));

        productArrayList.add(product);

    }

    @Override
    public void update(Product product) throws SQLException {
        Connect.updateDB(product);
    }

    @Override
    public void delete(Product product) {
        productArrayList.remove(product);
    }

    public void createSelected(Product product) {
        productSelectedListForOrder.add(product);
    }

    public void deleteSelected(Product product) {
        productSelectedListForOrder.remove(product);
        System.out.println(productSelectedListForOrder);
    }

    public void create(ObservableList<Product> productObservableList) throws SQLException {
        System.out.println("productSelectedListForOrder="+productSelectedListForOrder.size());
        for (Product product : productObservableList) {
//            Connect.writeDBOrderedProducts(product);
            productSelectedListForOrder.add(product);
        }
    }

    public void deleteProductSelectedListForOrder() {
        productSelectedListForOrder.removeAll();
    }

    public void delete(ObservableList<Product> productObservableList) {
        productSelectedListForOrder.clear();
//        for (Product product:productObservableList){
//            productSelectedListForOrder.remove(product);
//
//        }
    }

    @Override
    public void create(User user) {
        usersArrayList.add(user);
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {
        usersArrayList.remove(user);
    }

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
    public void delete(Supplier supplier) {
        suppliersArrayList.remove(supplier);
    }

    @Override
    public void create(Order order) throws SQLException {
        Connect.writeDB(order);
        order.setOrderNumber(Connect.readIDDB(order));
        ordersArrayList.add(order);

    }

    @Override
    public void update(Order order) {
        try {
            Connect.updateDB(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Order order) {
        ordersArrayList.remove(order);
    }

    //заполнение списков данными из таблиц БД
    public void fillInitFromDb() throws SQLException {
        Map<String, ResultSet> mapInitialDB = Connect.readAllDB();

        for(Map.Entry<String, ResultSet> entry : mapInitialDB.entrySet()){
            String tableName = entry.getKey();
            ResultSet resultSet = entry.getValue();
            while (resultSet!=null && resultSet.next()){
                if (tableName.equals("Supplier")){
                    suppliersArrayList.add(new Supplier(resultSet.getInt("Id"), resultSet.getString("Name"),resultSet.getString("Address"),resultSet.getString("Telephone")));
                } else if (tableName.equals("Product")){
                    productArrayList.add(new Product(resultSet.getInt("Id"),resultSet.getString("Name"), resultSet.getString("Amount"), resultSet.getString("Storage"), suppliersArrayList.get(resultSet.getInt("Supplier_id")-1)));
                }   else if (tableName.equals("Orders")){

                    ordersArrayList.add(new Order(resultSet.getInt("Id"), resultSet.getString("Date"),
                            productArrayList));
                }
            }
        }

    }

}
