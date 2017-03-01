package sample.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Note-001 on 07.12.2016.
 */
public class Supplier {
    private SimpleIntegerProperty id = new SimpleIntegerProperty(1);
    private SimpleStringProperty company = new SimpleStringProperty("");
    private SimpleStringProperty address = new SimpleStringProperty("");
    private SimpleStringProperty phone = new SimpleStringProperty("");

    //===================================================================================
    public Supplier(int id, String company, String address, String phone) {
        this.id = new SimpleIntegerProperty(id);
        this.company = new SimpleStringProperty(company);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
    }

    //==================================id==============================================
    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    //===================================company==============================================
    public SimpleStringProperty companyProperty() { //для таблицы
        return company;
    }

    public String getCompany() {
        return company.get();
    }

    public void setCompany(String company) {
        this.company.set(company);
    }

    //====================================address========================================
    public SimpleStringProperty addressProperty() {
        return address;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    //=====================================phone=======================================
    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    //===================================================================================
    @Override
    public String toString() {
        return company.get();
    }

    public Supplier() {
    }
}
