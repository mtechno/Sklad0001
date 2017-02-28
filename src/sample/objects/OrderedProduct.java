package sample.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Created by Note-001 on 28.02.2017.
 */
public class OrderedProduct {

    private SimpleIntegerProperty id = new SimpleIntegerProperty(1);
    private SimpleObjectProperty<Product> orderedProduct;
    private SimpleIntegerProperty amount = new SimpleIntegerProperty(0);

    public OrderedProduct(int id, Product product, int amount) {
        this.id = new SimpleIntegerProperty(id);
        this.orderedProduct = new SimpleObjectProperty<>(product);
        this.amount = new SimpleIntegerProperty(amount);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public Product getOrderedProduct() {
        return orderedProduct.get();
    }

    public SimpleObjectProperty<Product> orderedProductProperty() {
        return orderedProduct;
    }

    public void setOrderedProduct(Product orderedProduct) {
        this.orderedProduct.set(orderedProduct);
    }

    public int getAmount() {
        return amount.get();
    }

    public SimpleIntegerProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    @Override
    public String toString() {
        return "OrderedProduct{" +
                "orderedProduct=" + orderedProduct.get() +
                '}';
    }
}
