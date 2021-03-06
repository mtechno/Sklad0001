package sample.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Note-001 on 07.12.2016.
 */
public class Product {
    private SimpleIntegerProperty id = new SimpleIntegerProperty(1);
    private SimpleStringProperty name = new SimpleStringProperty(""); //этот тип используем для таблиц javafx
    private SimpleStringProperty storage = new SimpleStringProperty("");//автоматическое обновление их данных
    private SimpleStringProperty amount = new SimpleStringProperty("");
    private SimpleObjectProperty<Supplier> supplier = new SimpleObjectProperty<>(new Supplier(0, null, null, null));

    //================================================================================================
    public Product(int id, String name, String amount, String storage, Supplier supplier) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.storage = new SimpleStringProperty(storage);
        this.amount = new SimpleStringProperty(amount);
        this.supplier = new SimpleObjectProperty(supplier);
    }

    //===================================id===========================================================
    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    //==================================name==========================================================
    public SimpleStringProperty nameProperty() { //для таблицы
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    //==================================amount==========================================================
    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public String getAmount() {
        return amount.get();
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    //==================================storage==========================================================
    public SimpleStringProperty storageProperty() {
        return storage;
    }

    public String getStorage() {
        return storage.get();
    }

    public void setStorage(String storage) {
        this.storage.set(storage);
    }

    //==================================supplier==========================================================
    public SimpleObjectProperty supplierProperty() {
        return supplier;
    }


    public Supplier getSupplier() {
        return supplier.get();
    }

    public void setSupplier(Supplier supplier) {
        this.supplier.set(supplier);
    }

    //================================================================================================
    @Override
    public String toString() {
        return name.get();
    }


    public Product() {
    }

}
