package sample.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Note-001 on 07.12.2016.
 */
public class Order{
    private SimpleIntegerProperty orderNumber = new SimpleIntegerProperty(0);
    private ObservableList<Product> productList;
    private SimpleStringProperty date = new SimpleStringProperty("");

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", date=" + date +
                '}';
    }

    public Order(){

    }



    public SimpleIntegerProperty orderNumberProperty() {
        return orderNumber;
    }

    public ObservableList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ObservableList<Product> productList) {
        this.productList = productList;
    }

    public void addProductList(Product product){
        this.productList.add(product);
    }

    public void createProductList(ObservableList<Product> productObservableList){
        for (Product product:productObservableList){
            this.productList.add(product);
        }
    }

    public Order(int orderNumber, String date, ObservableList<Product> productList) {
        this.orderNumber = new SimpleIntegerProperty(orderNumber);
        this.date = new SimpleStringProperty(date);
    }

    public int getOrderNumber() {
        return orderNumber.get();
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber.set(orderNumber);
    }
}
