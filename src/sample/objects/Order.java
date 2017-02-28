package sample.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Note-001 on 07.12.2016.
 */
public class Order {
    private SimpleIntegerProperty orderNumber = new SimpleIntegerProperty(0);
    //    private ObservableList<Product> productList;
    private ObservableList<OrderedProduct> orderedProductList = FXCollections.observableArrayList();
    private SimpleStringProperty date = new SimpleStringProperty("");

    public Order() {

    }

    public Order(int orderNumber, String date) {
        this.orderNumber = new SimpleIntegerProperty(orderNumber);
        this.date = new SimpleStringProperty(date);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", date=" + date +
                '}';
    }

    public ObservableList<OrderedProduct> getOrderedProductList() {
        return orderedProductList;
    }

    public void setOrderedProductList(ObservableList<OrderedProduct> orderedProductList) {
        this.orderedProductList = orderedProductList;
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public SimpleIntegerProperty orderNumberProperty() {
        return orderNumber;
    }

    public void addOrderedProductToList(OrderedProduct orderedProduct) {
        //есть ли в списке продуктов у заказа Добавляемый продукт
        Boolean isAdd = true;
        for (OrderedProduct oProd : orderedProductList){
            if (oProd.getOrderedProduct().equals(orderedProduct.getOrderedProduct())){
                isAdd = false;
            }
        }

        if (isAdd) {
            this.orderedProductList.add(orderedProduct);
        }
    }

    public void removeOrderedProductFromList(OrderedProduct orderedProduct) {
        this.orderedProductList.remove(orderedProduct);
    }

    public void clearOrderedProductList() {
        this.orderedProductList.clear();
    }

    public int getOrderNumber() {
        return orderNumber.get();
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber.set(orderNumber);
    }
}
